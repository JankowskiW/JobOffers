package pl.wj.joboffers.domain.remotejoboffersretriever;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import pl.wj.joboffers.domain.joboffer.JobOfferFacade;
import pl.wj.joboffers.domain.joboffer.model.JobOfferMapper;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferDto;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.RemoteJobOffersRetriever;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.model.dto.RemoteJobOfferDto;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Log4j2
public class RemoteJobOffersRetrieverFacade {
    private final JobOfferFacade jobOfferFacade;
    private final RemoteJobOffersRetriever remoteJobOffersRetriever;

    public Set<RemoteJobOfferDto> retrieveRemoteJobOfferDtos() {
        return remoteJobOffersRetriever.retrieveRemoteJobOfferDtos();
    }

    public void saveRetrievedJobOffersIntoDatabase(Set<RemoteJobOfferDto> remoteJobOfferDtos) {
        Set<JobOfferDto> jobOfferDtos = JobOfferMapper.toJobOfferDtosSet(remoteJobOfferDtos);
        log.info("Ilość zmapowanych obiektów w RemoteJobOffersRetrieverFacade: " + jobOfferDtos.size());
        jobOfferFacade.saveJobOffers(jobOfferDtos);
        log.info("Zapisano obiekty do DB");
    }
}
