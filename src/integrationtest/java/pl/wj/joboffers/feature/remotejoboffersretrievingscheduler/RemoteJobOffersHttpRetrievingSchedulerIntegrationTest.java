package pl.wj.joboffers.feature.remotejoboffersretrievingscheduler;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.awaitility.Awaitility.await;

public class RemoteJobOffersHttpRetrievingSchedulerIntegrationTest {

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
