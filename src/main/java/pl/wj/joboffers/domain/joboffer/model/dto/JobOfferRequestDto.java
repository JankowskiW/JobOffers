package pl.wj.joboffers.domain.joboffer.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record JobOfferRequestDto(
        @NotBlank(message = "{job-offer.title.not-blank}")
        String title,
        @NotBlank(message = "{job-offer.company.not-blank}")
        String company,
        @NotNull(message = "{job-offer.salary.not-null}")
        String salary,
        @NotBlank(message = "{job-offer.offer-url.not-blank}")
        String offerUrl) {}
