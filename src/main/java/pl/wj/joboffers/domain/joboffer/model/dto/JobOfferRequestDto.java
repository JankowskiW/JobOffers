package pl.wj.joboffers.domain.joboffer.model.dto;

import jakarta.validation.constraints.NotNull;

public record JobOfferRequestDto(
        @NotNull(message = "{validation.request.job-offer.title.not-null")
        String title,
        @NotNull(message = "{validation.request.job-offer.company.not-null")
        String company,
        String salary,
        @NotNull(message = "{validation.request.job-offer.offer-url.not-null")
        String offerUrl) {}
