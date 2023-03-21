package pl.wj.joboffers.domain.remotejobofferretriever.model.dto;

import lombok.ToString;

public record RemoteJobOfferDto(String title, String company, String salary, String offerUrl) { }
