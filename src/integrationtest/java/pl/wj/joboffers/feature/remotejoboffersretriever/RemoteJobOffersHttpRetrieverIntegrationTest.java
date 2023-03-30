package pl.wj.joboffers.feature.remotejoboffersretriever;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.wj.joboffers.BaseIntegrationTest;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.RemoteJobOffersRetriever;
import pl.wj.joboffers.domain.remotejoboffersretriever.dto.RemoteJobOfferDto;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RemoteJobOffersHttpRetrieverIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private RemoteJobOffersRetrieverIntegrationTestHelper helper;
    @Autowired
    private RemoteJobOffersRetriever remoteJobOffersRetriever;

    @Test
    void shouldRetrieveAllRemoteJobOffers() {
        // given
        Set<RemoteJobOfferDto> expectedResponse = helper.getRemoteJobOfferDtos();
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
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
}
