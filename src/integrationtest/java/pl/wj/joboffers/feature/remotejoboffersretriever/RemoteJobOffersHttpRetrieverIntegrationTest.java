package pl.wj.joboffers.feature.remotejoboffersretriever;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import pl.wj.joboffers.BaseIntegrationTest;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.RemoteJobOffersRetriever;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.model.dto.RemoteJobOfferDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

public class RemoteJobOffersHttpRetrieverIntegrationTest extends BaseIntegrationTest {
    private final RemoteJobOffersRetrieverIntegrationTestHelper helper;
    private final RemoteJobOffersRetriever remoteJobOffersRetriever;

    @Autowired
    public RemoteJobOffersHttpRetrieverIntegrationTest(ObjectMapper objectMapper, MockMvc mockMvc,
                                                       RemoteJobOffersRetriever remoteJobOffersRetriever,
                                                       RemoteJobOffersRetrieverIntegrationTestHelper helper) {
        super(objectMapper, mockMvc);
        this.remoteJobOffersRetriever = remoteJobOffersRetriever;
        this.helper = helper;
    }

    @Test
    void shouldRetrieveAllRemoteJobOffers() {
        // given
        Set<RemoteJobOfferDto> expectedResponse = helper.getRemoteJobOfferDtos();
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(
                        WireMock.aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", "application/json")
                                .withBody(helper.createBodyWithSomeJobOffers())));

        // when
        Set<RemoteJobOfferDto> response = remoteJobOffersRetriever.retrieveRemoteJobOfferDtos();

        // then
        assertThat(response)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    void shouldInitializeRetrievingEverySpecifiedTime() {
        // given
        LocalDateTime jobOffersRetrievingTime = LocalDateTime.of(2023, 3, 23, 21,0,0);

        // when
        await().atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(1))
                .until( () -> {try { return true; } catch(RuntimeException e) { return false; }});


        // then
    }
}
