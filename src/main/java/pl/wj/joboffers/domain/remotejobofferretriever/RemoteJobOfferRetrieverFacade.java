package pl.wj.joboffers.domain.remotejobofferretriever;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import pl.wj.joboffers.domain.joboffer.JobOfferFacade;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferDto;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.RemoteJobOffersRetriever;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.model.dto.RemoteJobOfferDto;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Log4j2
public class RemoteJobOfferRetrieverFacade {
    private final JobOfferFacade jobOfferFacade;
    private final RemoteJobOffersRetriever remoteJobOffersRetriever;

    public Set<RemoteJobOfferDto> retrieveRemoteJobOfferDtos() {
        return remoteJobOffersRetriever.retrieveRemoteJobOfferDtos();
    }

    public void saveRetrievedJobOffersIntoDatabase(Set<RemoteJobOfferDto> remoteJobOfferDtos) {
        Set<JobOfferDto> jobOfferDtos = new HashSet<>();   // TODO: map remoteJobOfferDtos to jobOffers
        jobOfferFacade.saveJobOffersIntoDatabase(jobOfferDtos);
    }
}
