package pl.wj.joboffers.domain.joboffer.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JobOfferRequestDto(
        @NotBlank(message = "{validation.request.job-offer.title.not-blank}")
        String title,
        @NotBlank(message = "{validation.request.job-offer.company.not-blank}")
        String company,
        @NotNull(message = "{validation.request.job-offer.salary.not-null}")
        String salary,
        @NotBlank(message = "{validation.request.job-offer.offer-url.not-blank}")
        String offerUrl) {}
