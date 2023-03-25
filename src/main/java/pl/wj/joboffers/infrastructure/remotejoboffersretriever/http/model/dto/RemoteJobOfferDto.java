package pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.model.dto;

import lombok.Builder;

@Builder
public record RemoteJobOfferDto(String title, String company, String salary, String offerUrl) { }
