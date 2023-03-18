package pl.wj.joboffers.domain.JobOffer.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("job-offers")
public record JobOffer (String id){}