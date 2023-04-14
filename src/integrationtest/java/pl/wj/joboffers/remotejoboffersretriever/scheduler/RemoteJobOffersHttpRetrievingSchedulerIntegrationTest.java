package pl.wj.joboffers.remotejoboffersretriever.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import pl.wj.joboffers.BaseIntegrationTest;
import pl.wj.joboffers.JobOffersApplication;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.RemoteJobOffersRetriever;
import pl.wj.joboffers.remotejoboffersretriever.RemoteJobOffersRetrieverIntegrationTestHelper;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = JobOffersApplication.class, properties = "job-offers.http.client.config.scheduler.enabled=true")
public class RemoteJobOffersHttpRetrievingSchedulerIntegrationTest extends BaseIntegrationTest {
    @SpyBean
    private RemoteJobOffersRetriever remoteJobOffersRetriever;

    @Test
    void shouldInvokeRetrievingEverySpecifiedTime() {
        // given
        int invocationsNumber = 1;
        // when
        await().atMost(Duration.ofSeconds(2))
                .untilAsserted(() -> verify(remoteJobOffersRetriever, times(invocationsNumber))
                        .retrieveRemoteJobOfferDtos());
    }
}
