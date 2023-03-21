package pl.wj.joboffers.feature.remotejobofferretriever;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import pl.wj.joboffers.BaseIntegrationTest;
import pl.wj.joboffers.domain.remotejobofferretriever.RemoteJobOfferRetriever;
import pl.wj.joboffers.domain.remotejobofferretriever.model.dto.RemoteJobOfferDto;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RemoteJobOfferHttpRetrieverIntegrationTest extends BaseIntegrationTest {
    private final RemoteJobOfferRetrieverIntegrationTestHelper helper;
    private final RemoteJobOfferRetriever remoteJobOfferRetriever;

    @Autowired
    public RemoteJobOfferHttpRetrieverIntegrationTest(ObjectMapper objectMapper, MockMvc mockMvc, RemoteJobOfferRetriever remoteJobOfferRetriever, RemoteJobOfferRetrieverIntegrationTestHelper helper) {
        super(objectMapper, mockMvc);
        this.remoteJobOfferRetriever = remoteJobOfferRetriever;
        this.helper = helper;
    }

    @Test
    public void shouldRetrieveAllRemoteJobOffers() {
        // given
        Set<RemoteJobOfferDto> expectedResponse = helper.getSetOfRemoteJobOfferDtos();
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(
                        WireMock.aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", "application/json")
                                .withBody(helper.createBodyWithSomeJobOffers())));

        // when
        Set<RemoteJobOfferDto> response = remoteJobOfferRetriever.retrieveRemoteJobOffers();

        // then
        assertThat(response)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }
}
