package pl.wj.joboffers.domain.remotejoboffersretriever;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import pl.wj.joboffers.domain.joboffer.JobOfferFacade;
import pl.wj.joboffers.domain.joboffer.model.JobOfferMapper;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferDto;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferUrlDto;
import pl.wj.joboffers.domain.remotejoboffersretriever.dto.RemoteJobOfferDto;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.RemoteJobOffersRetriever;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<JobOfferDto> jobOfferDtos = filterRemoteJobOfferDtos(JobOfferMapper.toJobOfferDtosSet(remoteJobOfferDtos));
        log.info("Number of filtered objects in RemoteJobOffersRetrieverFacade: " + jobOfferDtos.size());
        jobOfferFacade.saveJobOffers(jobOfferDtos);
        log.info("Objects have been saved into db");
    }

    private Set<JobOfferDto> filterRemoteJobOfferDtos(Set<JobOfferDto> jobOfferDtos) {
        List<JobOfferUrlDto> existingJobOfferUrls = jobOfferFacade.getAllJobOfferUrls();
        return jobOfferDtos.stream()
                .filter(jo -> !existingJobOfferUrls.stream()
                                .map(JobOfferUrlDto::offerUrl)
                                .toList()
                                .contains(jo.offerUrl()))
                .collect(Collectors.toSet());
    }
}
