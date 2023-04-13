package pl.wj.joboffers.domain.remotejoboffersretriever;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import pl.wj.joboffers.domain.joboffer.JobOfferFacade;
import pl.wj.joboffers.domain.joboffer.model.JobOfferMapper;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferDto;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferUrlDto;
import pl.wj.joboffers.domain.remotejoboffersretriever.dto.RemoteJobOfferDto;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.RemoteJobOffersRetriever;

import java.util.HashSet;
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
        try {
            return remoteJobOffersRetriever.retrieveRemoteJobOfferDtos();
        } catch (ResponseStatusException e) {
            log.error("Cannot retrieve job offers from remote service: " + e.getMessage());
            return new HashSet<>();
        }
    }

    public List<JobOfferDto> saveRetrievedJobOffersIntoDatabase(Set<RemoteJobOfferDto> remoteJobOfferDtos) {
        Set<JobOfferDto> jobOfferDtos = filterRemoteJobOfferDtos(JobOfferMapper.toJobOfferDtoSet(remoteJobOfferDtos));
        log.info("Number of filtered objects in RemoteJobOffersRetrieverFacade: " + jobOfferDtos.size());
        return JobOfferMapper.toJobOfferDtoList(jobOfferFacade.saveJobOffers(jobOfferDtos));
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
