package pl.wj.joboffers.domain.joboffer.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferDto;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferResponseDto;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.model.dto.RemoteJobOfferDto;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobOfferMapper {

    public static JobOffer toJobOffer(JobOfferDto jobOfferDto) {
        return JobOffer
                .builder()
                .id(jobOfferDto.id())
                .title(jobOfferDto.title())
                .company(jobOfferDto.company())
                .salary(jobOfferDto.salary())
                .offerUrl(jobOfferDto.offerUrl())
                .build();
    }

    public static Set<JobOffer> toJobOffersSet(Set<JobOfferDto> jobOfferDtos) {
        return jobOfferDtos.stream().map(JobOfferMapper::toJobOffer).collect(Collectors.toSet());
    }

    public static JobOfferDto toJobOfferDto(RemoteJobOfferDto remoteJobOfferDto) {
        return JobOfferDto
                .builder()
                .title(remoteJobOfferDto.title())
                .company(remoteJobOfferDto.company())
                .salary(remoteJobOfferDto.salary())
                .offerUrl(remoteJobOfferDto.offerUrl())
                .build();
    }

    public static Set<JobOfferDto> toJobOfferDtosSet(Set<RemoteJobOfferDto> remoteJobOfferDtos) {
        return remoteJobOfferDtos.stream().map(JobOfferMapper::toJobOfferDto).collect(Collectors.toSet());
    }

    public static JobOfferResponseDto toJobOfferResponseDto(JobOffer jobOffer) {
        return JobOfferResponseDto.builder()
                .id(jobOffer.id())
                .title(jobOffer.title())
                .company(jobOffer.company())
                .salary(jobOffer.salary())
                .offerUrl(jobOffer.offerUrl())
                .build();
    }
}
