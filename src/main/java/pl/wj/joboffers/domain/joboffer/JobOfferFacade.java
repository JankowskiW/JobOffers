package pl.wj.joboffers.domain.joboffer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import pl.wj.joboffers.domain.joboffer.model.JobOffer;
import pl.wj.joboffers.domain.joboffer.model.JobOfferMapper;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferDto;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferResponseDto;
import pl.wj.joboffers.exception.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
@Log4j2
public class JobOfferFacade {
    private final JobOfferRepository jobOfferRepository;

    public void saveJobOffers(Set<JobOfferDto> jobOfferDtos) {
        Set<JobOffer> jobOffers = JobOfferMapper.toJobOffersSet(jobOfferDtos);
        log.info("Ilość zmapowanych obiektów w JobOfferFacade: " + jobOfferDtos.size());
        jobOfferRepository.saveAll(jobOffers);
    }

    public List<JobOffer> getAllJobOffers() {
        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        log.info("Ilość pobranych obiektów z mongoDB: " + jobOffers.size());
        return jobOffers;
    }

    public JobOfferResponseDto getJobOfferById(String id) {
        JobOffer jobOffer = jobOfferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found job offer with id:" + id));
        return JobOfferMapper.toJobOfferResponseDto(jobOffer);
    }
}
