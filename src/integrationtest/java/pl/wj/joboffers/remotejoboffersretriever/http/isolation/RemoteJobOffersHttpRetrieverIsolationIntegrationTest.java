package pl.wj.joboffers.remotejoboffersretriever.http.isolation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.HttpStatus;
import pl.wj.joboffers.config.IsolationIntegrationConfig;
import pl.wj.joboffers.domain.remotejoboffersretriever.dto.RemoteJobOfferDto;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.RemoteJobOffersRetriever;
import pl.wj.joboffers.remotejoboffersretriever.http.RemoteJobOffersRetrieverIntegrationTestHelper;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

public class RemoteJobOffersHttpRetrieverIsolationIntegrationTest {
    // it is necessary to create helper instance manually because we do not have Spring Context here,
    // so we can not inject bean into that field
    private final RemoteJobOffersRetrieverIntegrationTestHelper helper =
            new RemoteJobOffersRetrieverIntegrationTestHelper(new ObjectMapper());

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    RemoteJobOffersRetriever remoteJobOffersRetriever =
            new IsolationIntegrationConfig(wireMockServer.getPort(), 1000, 1000).remoteJobOfferRetriever();

    private static final String URL = "/offers";
    private static final Map.Entry<String, String> CONTENT_TYPE_HEADER =
            new AbstractMap.SimpleEntry<>("Content-Type", "application/json");

    @Test
    void shouldReturnStatus200AndAvailableRemoteOffers() {
        // given
        Set<RemoteJobOfferDto> expectedResponse = helper.getRemoteJobOfferDtos();
        wireMockServer.stubFor(WireMock.get(URL)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE_HEADER.getKey(), CONTENT_TYPE_HEADER.getValue())
                        .withBody(helper.createBodyWithSomeJobOffers())
                ));

        // when
        Set<RemoteJobOfferDto> response = remoteJobOffersRetriever.retrieveRemoteJobOfferDtos();

        // then
        assertThat(response)
                .isNotNull()
                .containsExactlyInAnyOrderElementsOf(expectedResponse);
    }

    @Test
    void shouldNotReturnAnyOffersWhenConnectionResetByPeer() {
        // given
        wireMockServer.stubFor(WireMock.get(URL)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE_HEADER.getKey(), CONTENT_TYPE_HEADER.getValue())
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)
                ));

        // when
        Set<RemoteJobOfferDto> response = remoteJobOffersRetriever.retrieveRemoteJobOfferDtos();

        // then
        assertThat(response)
                .isEqualTo(new HashSet<>());
    }

}