package pl.wj.joboffers.controller.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import pl.wj.joboffers.BaseIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JobOfferControllerExceptionsIntegrationTest extends BaseIntegrationTest {
    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;
    private static final String OFFERS_URL = "/job-offers";

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    @WithMockUser
    void shouldReturnConflictWhenUrlAlreadyExistsInDatabase() throws Exception {
        // step 1: Created first job offer
        // given
        String firstJobOfferRequestBody = """
                {
                    "title" : "Offer title",
                    "company" : "Company Name",
                    "salary" : "6 000 - 10 000 GROSS",
                    "offerUrl" : "https://offers.com/offers/aaaa"
                }
                """.trim();
        RequestBuilder firstOfferRequestBuilder = post(OFFERS_URL).content(firstJobOfferRequestBody).contentType(CONTENT_TYPE);
        // when
        ResultActions firstOfferResultAction = mockMvc.perform(firstOfferRequestBuilder);
        // then
        firstOfferResultAction.andExpect(status().isCreated());

        // step 2: Tried to create another job offer with the same url but get 409 CONFLICT
        // given
        String secondJobOfferRequestBody = """
                {
                    "title" : "Another offer title",
                    "company" : "Another Company Name",
                    "salary" : "7 000 - 11 000 GROSS",
                    "offerUrl" : "https://offers.com/offers/aaaa"
                }
                """.trim();
        RequestBuilder secondOfferRequestBuilder = post(OFFERS_URL).content(secondJobOfferRequestBody).contentType(CONTENT_TYPE);
        // when
        ResultActions secondOfferResultAction = mockMvc.perform(secondOfferRequestBuilder);
        // then
        secondOfferResultAction.andExpect(status().isConflict());
    }

    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenOfferByIdDoesNotExistInDb() throws Exception {
        // given
        String id = "someId";
        RequestBuilder getOfferByIdRequestBuilder = get(OFFERS_URL+"/"+id).contentType(CONTENT_TYPE);
        // when
        ResultActions getOfferByIdResult = mockMvc.perform(getOfferByIdRequestBuilder);
        // then
        getOfferByIdResult.andExpect(status().isNotFound());
    }
}
