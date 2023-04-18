package pl.wj.joboffers.cache.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import pl.wj.joboffers.BaseIntegrationTest;
import pl.wj.joboffers.domain.joboffer.JobOfferFacade;
import pl.wj.joboffers.infrastructure.security.model.dto.JwtResponseDto;

import java.time.Duration;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class RedisJobOffersCacheIntegrationTest extends BaseIntegrationTest {
    private static final String DOCKER_IMAGE_NAME = "redis";
    private static final int  REDIS_PORT = 6379;
    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;
    private static final String JWT_REGEX = "^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$";
    private static final String USERS_URL = "/users";
    private static final String REGISTER_URL = USERS_URL + "/register";
    private static final String LOGIN_URL = USERS_URL + "/login";
    private static final String OFFERS_URL = "/job-offers";
    private static final String CACHE_NAME = "jobOffers";
    private static final String REQUEST_BODY = """
                {
                    "username" : "usrname",
                    "password" : "pswd"
                }
                """.trim();

    @Container
    private static final GenericContainer<?> REDIS;

    @SpyBean
    private JobOfferFacade jobOfferFacade;

    @Autowired
    private CacheManager cacheManager;

    static {
        REDIS = new GenericContainer<>(DOCKER_IMAGE_NAME).withExposedPorts(REDIS_PORT);
        REDIS.start();
    }

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.redis.port", () -> REDIS.getFirstMappedPort().toString());
        registry.add("spring.cache.type", () -> CacheType.REDIS);
        registry.add("spring.cache.redis.time-to-live", () -> "PT1S");
    }

    @Test
    public void shouldCacheOffersAndInvalidateWhenTTLAchieve0() throws Exception {
        // step 1: User created an account
        // given && when
        ResultActions registerAction = mockMvc.perform(post(REGISTER_URL).content(REQUEST_BODY).contentType(CONTENT_TYPE));
        // then
        registerAction.andExpect(status().isCreated());

        // step 2: User logged in
        // given && when
        ResultActions loginAction = mockMvc.perform(post(LOGIN_URL).content(REQUEST_BODY).contentType(CONTENT_TYPE));
        // then
        MvcResult mvcResult = loginAction.andExpect(status().isOk()).andReturn();
        String jsonResult = mvcResult.getResponse().getContentAsString();
        JwtResponseDto jwtResponseDto = objectMapper.readValue(jsonResult, JwtResponseDto.class);
        String responseToken = jwtResponseDto.token();
        assertAll(
                () -> assertThat(jwtResponseDto.username()).isEqualTo("usrname"),
                () -> assertThat(responseToken).matches(Pattern.compile(JWT_REGEX))
        );

        // step 3: Cached job offers response
        // given
        String token = "Bearer " + responseToken;
        // when
        mockMvc.perform(get(OFFERS_URL)
                .header("Authorization", token)
                .contentType(CONTENT_TYPE));
        // then
        verify(jobOfferFacade, times(1)).getAllJobOffers();
        assertThat(cacheManager.getCacheNames().contains(CACHE_NAME)).isTrue();

        // step 4: Invalidated cache when TTL achieve 0
        // given
        int numberOfMethodCalling = 2;
        Duration awaitDuration = Duration.ofSeconds(5);
        Duration pollInterval = Duration.ofSeconds(1);
        RequestBuilder invalidateCacheRequestBuilder =
                get(OFFERS_URL)
                        .header("Authorization", token)
                        .contentType(CONTENT_TYPE);
        // when && then
        await()
                .atMost(awaitDuration)
                .pollInterval(pollInterval)
                .untilAsserted(() -> {
                    mockMvc.perform(invalidateCacheRequestBuilder);
                    verify(jobOfferFacade, atLeast(numberOfMethodCalling)).getAllJobOffers();
                });

    }
}
