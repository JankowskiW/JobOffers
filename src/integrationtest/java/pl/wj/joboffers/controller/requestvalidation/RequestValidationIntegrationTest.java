package pl.wj.joboffers.controller.requestvalidation;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import pl.wj.joboffers.BaseIntegrationTest;
import pl.wj.joboffers.exception.body.RequestValidationExceptionBody;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

public class RequestValidationIntegrationTest extends BaseIntegrationTest {

    @Test
    @WithMockUser
    public void shouldReturnBadRequestResponseWhenRequestBodyIsInvalid() throws Exception {
        // given
        String jsonRequestBody = """
                {
                }
                """.trim();
        RequestBuilder request = post("/job-offers")
                .content(jsonRequestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        // when
        ResultActions result = mockMvc.perform(request);
        // then
        MvcResult mvcResult = result.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        RequestValidationExceptionBody resultBody = objectMapper.readValue(json, RequestValidationExceptionBody.class);
        assertThat(resultBody.messages()).containsExactlyInAnyOrder(
                "company must not be blank",
                "title must not be blank",
                "salary must not be null",
                "offer url must not be blank");
    }
}
