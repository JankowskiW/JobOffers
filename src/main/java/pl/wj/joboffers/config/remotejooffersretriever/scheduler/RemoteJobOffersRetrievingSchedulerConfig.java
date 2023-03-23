package pl.wj.joboffers.config.remotejooffersretriever.scheduler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name="job-offers.http.client.config.scheduler.enabled", matchIfMissing = true)
public class RemoteJobOffersRetrievingSchedulerConfig {
}
