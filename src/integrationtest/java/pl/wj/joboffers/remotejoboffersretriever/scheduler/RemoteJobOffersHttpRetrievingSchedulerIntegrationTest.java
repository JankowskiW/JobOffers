package pl.wj.joboffers.remotejoboffersretriever.scheduler;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import pl.wj.joboffers.BaseIntegrationTest;
import pl.wj.joboffers.JobOffersApplication;
import pl.wj.joboffers.domain.joboffer.JobOfferFacade;
import pl.wj.joboffers.remotejoboffersretriever.RemoteJobOffersRetrieverIntegrationTestHelper;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.RemoteJobOffersRetriever;
import pl.wj.joboffers.domain.remotejoboffersretriever.dto.RemoteJobOfferDto;

import java.time.Duration;
import java.util.Set;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = JobOffersApplication.class, properties = "job-offers.http.client.config.scheduler.enabled=true")
public class RemoteJobOffersHttpRetrievingSchedulerIntegrationTest extends BaseIntegrationTest {
    @SpyBean
    private RemoteJobOffersRetriever remoteJobOffersRetriever;
    @Autowired
    private RemoteJobOffersRetrieverIntegrationTestHelper helper;
    @Autowired
    private JobOfferFacade jobOfferFacade;

    @Test
    void shouldInvokeRetrievingEverySpecifiedTime() {
        // given
        int invocationsNumber = 2;
        // when
        await().atMost(Duration.ofSeconds(2))
                .untilAsserted(() -> verify(remoteJobOffersRetriever, times(invocationsNumber))
                        .retrieveRemoteJobOfferDtos());


//        await().atMost(Duration.ofSeconds(20))
//                .pollInterval(Duration.ofSeconds(1))
//                .until( () -> {try { return true; } catch(RuntimeException e) { return false; }});

    }



    @Test
    void shouldSaveJobOffersToDatabase() {
        // given
        String url = "/offers";
        Set<RemoteJobOfferDto> remoteJobOfferDtos = helper.getRemoteJobOfferDtos();
        wireMockServer.stubFor(WireMock.get(url)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(helper.createBodyWithSomeJobOffers())));

        int invocationsNumber = 2;
        // when
//        await().atMost(Duration.ofSeconds(2))
//                .untilAsserted(() -> verify(remoteJobOffersRetriever, times(invocationsNumber))
//                        .retrieveRemoteJobOfferDtos());

        // when
//        await()
//                .atMost(Duration.ofSeconds(20))
//                .pollInterval(Duration.ofSeconds(1))
//                .until(() -> jobOfferFacade.getAllJobOffers().size() == remoteJobOfferDtos.size());

        // then
    }
}
