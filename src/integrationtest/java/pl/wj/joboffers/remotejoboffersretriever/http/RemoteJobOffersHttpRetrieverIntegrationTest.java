package pl.wj.joboffers.remotejoboffersretriever.http;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.wj.joboffers.BaseIntegrationTest;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.RemoteJobOffersRetriever;
import pl.wj.joboffers.domain.remotejoboffersretriever.dto.RemoteJobOfferDto;
import pl.wj.joboffers.remotejoboffersretriever.RemoteJobOffersRetrieverIntegrationTestHelper;

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
        String url = "/offers";
        Set<RemoteJobOfferDto> expectedResponse = helper.getRemoteJobOfferDtos();
        wireMockServer.stubFor(WireMock.get(url)
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
