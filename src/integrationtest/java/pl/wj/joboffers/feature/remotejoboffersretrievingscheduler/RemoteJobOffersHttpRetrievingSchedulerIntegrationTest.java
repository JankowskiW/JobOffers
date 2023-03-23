package pl.wj.joboffers.feature.remotejoboffersretrievingscheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.wj.joboffers.BaseIntegrationTest;
import pl.wj.joboffers.feature.remotejoboffersretriever.RemoteJobOffersRetrieverIntegrationTestHelper;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.RemoteJobOffersRetriever;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class RemoteJobOffersHttpRetrievingSchedulerIntegrationTest extends BaseIntegrationTest {

    @SpyBean
    private RemoteJobOffersRetriever remoteJobOffersRetriever;

    @Autowired
    public RemoteJobOffersHttpRetrievingSchedulerIntegrationTest(ObjectMapper objectMapper, MockMvc mockMvc) {
        super(objectMapper, mockMvc);
    }

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
}
