package pl.wj.joboffers.domain.joboffer.model.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record JobOfferResponseDto(
        String id,
        String title,
        String company,
        String salary,
        String offerUrl) implements Serializable {}
