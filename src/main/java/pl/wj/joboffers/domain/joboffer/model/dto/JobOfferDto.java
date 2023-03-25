package pl.wj.joboffers.domain.joboffer.model.dto;

import lombok.Builder;

@Builder
public record JobOfferDto (String id, String title, String company, String salary, String offerUrl){
}
