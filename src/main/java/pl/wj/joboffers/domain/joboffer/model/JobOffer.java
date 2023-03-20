package pl.wj.joboffers.domain.joboffer.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("job-offers")
public record JobOffer (String id){}