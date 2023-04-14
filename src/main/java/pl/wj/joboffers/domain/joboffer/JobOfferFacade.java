package pl.wj.joboffers.domain.joboffer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import pl.wj.joboffers.domain.joboffer.model.JobOffer;
import pl.wj.joboffers.domain.joboffer.model.JobOfferMapper;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferDto;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferRequestDto;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferResponseDto;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferUrlDto;
import pl.wj.joboffers.exception.definition.ResourceNotFoundException;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
@Log4j2
public class JobOfferFacade {
    private final JobOfferRepository jobOfferRepository;

    public List<JobOffer> saveJobOffers(Set<JobOfferDto> jobOfferDtos) {
        Set<JobOffer> jobOffers = JobOfferMapper.toJobOffersSet(jobOfferDtos);
        log.info("Number of inserted job offers into database: " + jobOffers.size());
        return jobOfferRepository.saveAll(jobOffers);
    }

    @Cacheable("jobOffers")
    public List<JobOfferResponseDto> getAllJobOffers() {
        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        return JobOfferMapper.toJobOfferResponseDtoList(jobOffers);
    }

    public JobOfferResponseDto getJobOfferById(String id) {
        JobOffer jobOffer = jobOfferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found job offer with id:" + id));
        return JobOfferMapper.toJobOfferResponseDto(jobOffer);
    }

    public JobOfferResponseDto addJobOffer(JobOfferRequestDto jobOfferRequestDto) {
//        if (jobOfferRepository.existsByOfferUrl(jobOfferRequestDto.offerUrl()))
//            throw new ResourceAlreadyExistsException(
//                    String.format("Job offer with url %s already exists", jobOfferRequestDto.offerUrl()));
        JobOffer jobOffer = JobOfferMapper.toJobOffer(jobOfferRequestDto);
        return JobOfferMapper.toJobOfferResponseDto(jobOfferRepository.save(jobOffer));
    }

    public List<JobOfferUrlDto> getAllJobOfferUrls() {
        return jobOfferRepository.findAllOfferUrls();
    }
}
