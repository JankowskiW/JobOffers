package pl.wj.joboffers.infrastructure.remotejoboffersretriever.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.wj.joboffers.domain.remotejobofferretriever.RemoteJobOfferRetrieverFacade;

@Log4j2
@Component
@RequiredArgsConstructor
public class RemoteJobOffersRetrievingScheduler {
    private final RemoteJobOfferRetrieverFacade remoteJobOfferRetrieverFacade;

    @Scheduled(fixedDelayString = "${job-offers.http.client.config.scheduler.delay}")
    public void retrieveRemoteJobOfferDtos() {
        log.warn("Retrieved remote job offers");
        remoteJobOfferRetrieverFacade.retrieveRemoteJobOfferDtos();
    }
}
