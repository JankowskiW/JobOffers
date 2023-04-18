package pl.wj.joboffers.controller.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import pl.wj.joboffers.BaseIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerErrorsIntegrationTest extends BaseIntegrationTest {
    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;
    private static final String USERS_URL = "/users";
    private static final String REGISTER_URL = USERS_URL + "/register";
    private static final String LOGIN_URL = USERS_URL + "/login";
    private static final String REQUEST_BODY = """
                {
                    "username" : "usrname",
                    "password" : "pswd"
                }
                """.trim();

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void shouldReturnConflictWhenUserAlreadyExistsInDatabase() throws Exception {
        // step 1: Created first user
        // given
        RequestBuilder firstRegisterRequestBuilder = post(REGISTER_URL).content(REQUEST_BODY).contentType(CONTENT_TYPE);
        // when
        ResultActions firstRegisterResultAction = mockMvc.perform(firstRegisterRequestBuilder);
        // then
        firstRegisterResultAction.andExpect(status().isCreated());

        // step 2: Tried to create another user with the same username but get 409 CONFLICT
        // given
        RequestBuilder secondRegisterRequestBuilder = post(REGISTER_URL).content(REQUEST_BODY).contentType(CONTENT_TYPE);
        // when
        ResultActions secondRegisterResultAction = mockMvc.perform(secondRegisterRequestBuilder);
        // then
        secondRegisterResultAction.andExpect(status().isConflict());
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExistInDatabase() throws Exception {
        // given
        RequestBuilder loginRequestBuilder = post(LOGIN_URL).content(REQUEST_BODY).contentType(CONTENT_TYPE);
        // when
        ResultActions loginResultAction = mockMvc.perform(loginRequestBuilder);
        // then
        loginResultAction.andExpect(status().isNotFound());
    }
}
