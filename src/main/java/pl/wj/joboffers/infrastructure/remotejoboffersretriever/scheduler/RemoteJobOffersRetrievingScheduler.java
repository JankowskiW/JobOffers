package pl.wj.joboffers.infrastructure.remotejoboffersretriever.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import pl.wj.joboffers.domain.remotejobofferretriever.RemoteJobOfferRetrieverFacade;

@Log4j2
@RequiredArgsConstructor
public class RemoteJobOffersRetrievingScheduler {
    private final RemoteJobOfferRetrieverFacade remoteJobOfferRetrieverFacade;

    @Scheduled(cron = "0 0 */3 * * *")
    public void retrieveRemoteJobOfferDtos() {
        log.info("Retrieved remote job offers");
        remoteJobOfferRetrieverFacade.retrieveRemoteJobOfferDtos();
    }
}
