package pl.wj.joboffers.feature.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.wj.joboffers.BaseIntegrationTest;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferResponseDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestControllerTest extends BaseIntegrationTest {

    @Test
    void someTest() throws Exception {
        String path = "/job-offers";
        ResultActions result = mockMvc.perform(post(path)
                .content("""
                        {
                            "sth":"sth"
                        }
                        """)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isMethodNotAllowed());
    }

    @Test
    void someTest2() throws Exception {
        String path = "/job-offers";
        String id = "identifier";

        JobOfferResponseDto response = JobOfferResponseDto.builder()
                .id(id)
                .title("some title")
                .company("some company")
                .salary("some salary")
                .offerUrl("some offer url")
                .build();

        System.out.println((new ObjectMapper())
                .writeValueAsString(response));

        ResultActions result = mockMvc.perform(get(path + "/" + id)
                .contentType(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = result.andExpect(status().isOk()).andReturn();
        String jsonResult = mvcResult.getResponse().getContentAsString();
        assertThat(jsonResult).isEqualTo((new ObjectMapper()).writeValueAsString(response));

//        result.andExpect(status().isOk()).andExpect(content().json((new ObjectMapper()).writeValueAsString(response)));

    }
}