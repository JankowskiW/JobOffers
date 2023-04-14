package pl.wj.joboffers.features;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.wj.joboffers.BaseIntegrationTest;
import pl.wj.joboffers.domain.joboffer.model.JobOfferMapper;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferDto;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferResponseDto;
import pl.wj.joboffers.domain.remotejoboffersretriever.RemoteJobOffersRetrieverFacade;
import pl.wj.joboffers.domain.remotejoboffersretriever.dto.RemoteJobOfferDto;
import pl.wj.joboffers.domain.user.model.dto.UserResponseDto;
import pl.wj.joboffers.infrastructure.security.model.dto.JwtResponseDto;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserWantToGetJobOffersIntegrationTest  extends BaseIntegrationTest {
    @Autowired
    private UserWantToGetJobOffersHelper helper;
//    @Autowired
//    private RemoteJobOffersRetrievingScheduler remoteJobOffersRetrievingScheduler;
    @Autowired
    private RemoteJobOffersRetrieverFacade remoteJobOffersRetrieverFacade;

    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;
    private static final String JWT_REGEX = "^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$";
    private static final String REGISTER_URL = "/user/register";
    private static final String LOGIN_URL = "/user/login";
    private static final String OFFERS_URL = "/job-offers";
    private static final String REMOTE_OFFERS_URL = "/offers";
    private static final String USER_NAME = "user";
    private static final String CREDENTIALS_BODY = """
                {
                    "username":"user",
                    "password":"somePassword"
                }
                """.trim();

    @Test
    void shouldGetJobOffersWhenUserIsAuthenticatedAndExternalServiceHasSomeOffers() throws Exception {
        // step 1: Unauthenticated user tried to get job offers and system should return FORBIDDEN(403)
        // given && when
        ResultActions failedOffersRequest = mockMvc.perform(get(OFFERS_URL).contentType(CONTENT_TYPE));
        // then
        failedOffersRequest.andExpect(status().isForbidden());


        // step 2: User tried to log in with wrong credentials and system should return NOT_FOUND(404)
        // given
        String badLoginCredentialsBody = """
                {
                    "username":"notExistingUser",
                    "password":"somePassword"
                }
                """.trim();
        // when
        ResultActions failedLoginResult = mockMvc.perform(
                post(LOGIN_URL)
                        .content(badLoginCredentialsBody)
                        .contentType(CONTENT_TYPE));
        // then
        failedLoginResult.andExpect(status().isNotFound());


        // step 3: User created an account and system should return CREATED(201) and username
        // given
        UserResponseDto expectedResponse = UserResponseDto.builder()
                .id("someId")
                .username(USER_NAME)
                .build();
        // when
        ResultActions registerResult = mockMvc.perform(
                post(REGISTER_URL)
                        .content(CREDENTIALS_BODY)
                        .contentType(CONTENT_TYPE));
        // then
        MvcResult registerResponse = registerResult.andExpect(status().isCreated()).andReturn();
        String registerResponseJson = registerResponse.getResponse().getContentAsString();
        UserResponseDto userResponseDto = objectMapper.readValue(registerResponseJson, UserResponseDto.class);
        assertAll(
                () -> assertThat(userResponseDto.username()).isEqualTo(expectedResponse.username()),
                () -> assertThat(userResponseDto.id()).isNotNull()
        );


        // step 4: User tried to log in with existing credentials and system should return OK(200) and JWT Token
        // given && when
        ResultActions loginResult = mockMvc.perform(
                post(LOGIN_URL)
                        .content(CREDENTIALS_BODY)
                        .contentType(CONTENT_TYPE));
        // then
        MvcResult loginResponse = loginResult.andExpect(status().isOk()).andReturn();
        String loginResponseJson = loginResponse.getResponse().getContentAsString();
        JwtResponseDto jwtResponseDto = objectMapper.readValue(loginResponseJson, JwtResponseDto.class);
        String token = "Bearer " + jwtResponseDto.token();
        assertAll(
                () -> assertThat(jwtResponseDto.username()).isEqualTo(USER_NAME),
                () -> assertThat(jwtResponseDto.token()).matches(Pattern.compile(JWT_REGEX))
        );

        // step 5: Scheduler tried to retrieve data from external service but there is none
        // given
        int numberOfOffers = 0;
        wireMockServer.stubFor(WireMock.get(REMOTE_OFFERS_URL)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", CONTENT_TYPE)
                        .withBody(helper.createOffersBody(numberOfOffers))));
        // when
//        List<JobOfferDto> remoteJobOffersResponse = remoteJobOffersRetrievingScheduler.retrieveRemoteJobOfferDtos();
        Set<RemoteJobOfferDto> retrievedJobOffers = remoteJobOffersRetrieverFacade.retrieveRemoteJobOfferDtos();
        List<JobOfferDto> remoteJobOffersResponse = remoteJobOffersRetrieverFacade.saveRetrievedJobOffersIntoDatabase(retrievedJobOffers);
        // then
        assertThat(remoteJobOffersResponse).isEmpty();

        // step 6: Authenticated user (with valid JWT) tried to get job offers before scheduler retrieve any offers
        //         from external service and system should return OK(200) with empty list of job offers
        // given && when
        ResultActions zeroOffersRequest =
                mockMvc.perform(get(OFFERS_URL)
                        .header("Authorization", token)
                        .contentType(CONTENT_TYPE));
        // then
        MvcResult zeroOffersResponse = zeroOffersRequest.andExpect(status().isOk()).andReturn();
        String zeroOffersResponseJson = zeroOffersResponse.getResponse().getContentAsString();
        List<JobOfferResponseDto> jobOfferResponseDtoList = objectMapper.readValue(zeroOffersResponseJson, new TypeReference<>(){});
        assertThat(jobOfferResponseDtoList)
                .isNotNull()
                .isEmpty();

        // step 7: Scheduler retrieved data from external service and there is 2 new job offers
        // given
        int offset = 0;
        numberOfOffers = 2;
        List<RemoteJobOfferDto> remoteJobOffersExpectedResponse = helper.getRemoteJobOfferDtos(offset, numberOfOffers);
        wireMockServer.stubFor(WireMock.get(REMOTE_OFFERS_URL)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", CONTENT_TYPE)
                        .withBody(helper.createOffersBody(numberOfOffers))));
        // when
//        remoteJobOffersResponse = remoteJobOffersRetrievingScheduler.retrieveRemoteJobOfferDtos();
        retrievedJobOffers = remoteJobOffersRetrieverFacade.retrieveRemoteJobOfferDtos();
        remoteJobOffersResponse = remoteJobOffersRetrieverFacade.saveRetrievedJobOffersIntoDatabase(retrievedJobOffers);
        // then
        assertThat(JobOfferMapper.toRemoteJobOfferDtoList(remoteJobOffersResponse))
                .isNotNull()
                .hasSize(remoteJobOffersExpectedResponse.size())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(remoteJobOffersExpectedResponse);

        // step 8: Authenticated user tried to get job offers after scheduler retrieved data and system
        //         should return OK(200) and list with 2 offers
        // given && when
        ResultActions twoOffersRequest =
                mockMvc.perform(get(OFFERS_URL)
                        .header("Authorization", token)
                        .contentType(CONTENT_TYPE));
        // then
        MvcResult twoOffersResponse = twoOffersRequest.andExpect(status().isOk()).andReturn();
        String twoOffersResponseJson = twoOffersResponse.getResponse().getContentAsString();
        jobOfferResponseDtoList = objectMapper.readValue(twoOffersResponseJson, new TypeReference<>(){});
        assertThat(jobOfferResponseDtoList)
                .isNotNull()
                .hasSize(numberOfOffers);

        // step 9: Scheduler retrieved 4 job offers from external service but stored only 2 because there is only 2 new job offers
        // given
        offset = 2;
        numberOfOffers = 4;
        remoteJobOffersExpectedResponse = helper.getRemoteJobOfferDtos(offset, numberOfOffers);
        wireMockServer.stubFor(WireMock.get(REMOTE_OFFERS_URL)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", CONTENT_TYPE)
                        .withBody(helper.createOffersBody(numberOfOffers))));
        // when
//        remoteJobOffersResponse = remoteJobOffersRetrievingScheduler.retrieveRemoteJobOfferDtos();
        retrievedJobOffers = remoteJobOffersRetrieverFacade.retrieveRemoteJobOfferDtos();
        remoteJobOffersResponse = remoteJobOffersRetrieverFacade.saveRetrievedJobOffersIntoDatabase(retrievedJobOffers);
        // then
        assertThat(JobOfferMapper.toRemoteJobOfferDtoList(remoteJobOffersResponse))
                .isNotNull()
                .hasSize(remoteJobOffersExpectedResponse.size())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(remoteJobOffersExpectedResponse);

        // step 10: Authenticated user tried to get job offers after scheduler retrieved another part of data
        //          from external service and system should return OK(200) and list with 4 offers
        // given && when
        ResultActions fourOffersRequest =
                mockMvc.perform(get(OFFERS_URL)
                        .header("Authorization", token)
                        .contentType(CONTENT_TYPE));
        // then
        MvcResult fourOffersResponse = fourOffersRequest.andExpect(status().isOk()).andReturn();
        String fourOffersResponseJson = fourOffersResponse.getResponse().getContentAsString();
        jobOfferResponseDtoList = objectMapper.readValue(fourOffersResponseJson, new TypeReference<>(){});
        assertThat(jobOfferResponseDtoList)
                .isNotNull()
                .hasSize(numberOfOffers);

        // step 11: Authenticated user tried to get specific job offer details but the id he sent does not exist in db
        //          and system should return NOT_FOUND(404)
        // given
        String notExistingId = "someid";
        // when
        ResultActions notExistingOfferRequest =
                mockMvc.perform(get(OFFERS_URL+"/"+notExistingId)
                        .header("Authorization", token)
                        .contentType(CONTENT_TYPE));
        // then
        notExistingOfferRequest.andExpect(status().isNotFound());

        // step 12: Authenticated user tried to get specific job offer details and the id he sent does exist in db
        //          and system should return OK(200) and job offer details
        // given
        JobOfferResponseDto expectedJobOfferResponseDto = jobOfferResponseDtoList.get(0);
        String existingId = expectedJobOfferResponseDto.id();
        // when
        ResultActions specificOfferRequest =
                mockMvc.perform(get(OFFERS_URL+"/"+existingId)
                        .header("Authorization", token)
                        .contentType(CONTENT_TYPE));
        // then
        MvcResult specificOfferResponse = specificOfferRequest.andExpect(status().isOk()).andReturn();
        String specificOfferResponseJson = specificOfferResponse.getResponse().getContentAsString();
        JobOfferResponseDto specificOfferResponseDto = objectMapper.readValue(specificOfferResponseJson, JobOfferResponseDto.class);
        assertThat(specificOfferResponseDto)
                .isNotNull()
                .isEqualTo(expectedJobOfferResponseDto);

        // step 13: Authenticated user tried to create new job offer but url he sent already exists in db and
        //          system should return CONFLICT(409)
        // given
        String newOfferBodyWithExistingUrl = helper.createNewOfferBody(0);
        // when
        ResultActions addOfferFailedResult = mockMvc.perform(
                post(OFFERS_URL)
                        .content(newOfferBodyWithExistingUrl)
                        .header("Authorization", token)
                        .contentType(CONTENT_TYPE));
        // then
        addOfferFailedResult.andExpect(status().isConflict());

        // step 14: Authenticated user tried to create new job offer and he sent correct body with url that does not
        //          exist in db yet and server should return CREATED(201) and body with created offer
        // given
        int index = 4;
        String newOfferBody = helper.createNewOfferBody(index);
        JobOfferResponseDto expectedNewJobOfferResponse = helper.createNewJobOfferResponseDto(index);
        // when
        ResultActions addOfferResult = mockMvc.perform(
                post(OFFERS_URL)
                        .content(newOfferBody)
                        .header("Authorization", token)
                        .contentType(CONTENT_TYPE));
        // then
        MvcResult addOfferResponse = addOfferResult.andExpect(status().isCreated()).andReturn();
        String addOfferResponseJson = addOfferResponse.getResponse().getContentAsString();
        JobOfferResponseDto newJobOfferResponse = objectMapper.readValue(addOfferResponseJson, JobOfferResponseDto.class);
        assertAll(
                () -> assertThat(newJobOfferResponse.title()).isEqualTo(expectedNewJobOfferResponse.title()),
                () -> assertThat(newJobOfferResponse.company()).isEqualTo(expectedNewJobOfferResponse.company()),
                () -> assertThat(newJobOfferResponse.salary()).isEqualTo(expectedNewJobOfferResponse.salary()),
                () -> assertThat(newJobOfferResponse.offerUrl()).isEqualTo(expectedNewJobOfferResponse.offerUrl())
        );

        // step 15: Authenticated user tried to get job offers after he added new offer and system should return OK(200)
        //          and list with 5 offers
        // given
        numberOfOffers = 5;
        // when
        ResultActions fiveOffersRequest =
                mockMvc.perform(get(OFFERS_URL)
                        .header("Authorization", token)
                        .contentType(CONTENT_TYPE));
        // then
        MvcResult fiveOffersResponse = fiveOffersRequest.andExpect(status().isOk()).andReturn();
        String fiveOffersResponseJson = fiveOffersResponse.getResponse().getContentAsString();
        jobOfferResponseDtoList = objectMapper.readValue(fiveOffersResponseJson, new TypeReference<>(){});
        assertThat(jobOfferResponseDtoList)
                .isNotNull()
                .hasSize(numberOfOffers);

        // step 16: User with expired JWT token tried to get job offers and system should return FORBIDDEN(403)



        // step 17: User tried to create account with username that already exists and system should return CONFLICT(409)
        // given && when
        ResultActions failedRegisterRequest = mockMvc.perform(
                post(REGISTER_URL)
                        .content(CREDENTIALS_BODY)
                        .contentType(CONTENT_TYPE));
        // then
        failedRegisterRequest.andExpect(status().isConflict());
    }
}
