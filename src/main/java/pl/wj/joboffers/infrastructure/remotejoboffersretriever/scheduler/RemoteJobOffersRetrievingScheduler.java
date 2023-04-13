package pl.wj.joboffers.infrastructure.remotejoboffersretriever.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.wj.joboffers.domain.remotejoboffersretriever.RemoteJobOffersRetrieverFacade;
import pl.wj.joboffers.domain.remotejoboffersretriever.dto.RemoteJobOfferDto;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Log4j2
public class RemoteJobOffersRetrievingScheduler {
    private final RemoteJobOffersRetrieverFacade remoteJobOffersRetrieverFacade;

    @Scheduled(fixedDelayString = "${job-offers.http.client.config.scheduler.delay}")
//    public List<JobOfferDto> retrieveRemoteJobOfferDtos() {
    public void retrieveRemoteJobOfferDtos() {
        Set<RemoteJobOfferDto> remoteJobOfferDtos = remoteJobOffersRetrieverFacade.retrieveRemoteJobOfferDtos();
        log.info("Number of retrieved records from remote repository: " + remoteJobOfferDtos.size());
        remoteJobOffersRetrieverFacade.saveRetrievedJobOffersIntoDatabase(remoteJobOfferDtos);
    }
}
