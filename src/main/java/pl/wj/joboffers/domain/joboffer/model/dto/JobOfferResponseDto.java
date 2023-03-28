package pl.wj.joboffers.domain.joboffer.model.dto;

import lombok.Builder;

@Builder
public record JobOfferResponseDto(
        String id,
        String title,
        String company,
        String salary,
        String offerUrl) {}
