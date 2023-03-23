package pl.wj.joboffers.domain.joboffer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.wj.joboffers.domain.joboffer.model.JobOffer;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferDto;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class JobOfferFacade {
    private final JobOfferRepository jobOfferRepository;

    public void saveJobOffersIntoDatabase(Set<JobOfferDto> jobOfferDtos) {
        Set<JobOffer> jobOffers = new HashSet<>(); // TODO: map jobOfferDtos to jobOffers
        jobOfferRepository.saveAll(jobOffers);
    }
}
