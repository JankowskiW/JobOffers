package pl.wj.joboffers.domain.joboffer.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "job-offers")
public record JobOffer (
        @Id
        String id,
        String title,
        String company,
        String salary,
        @Indexed(unique = true)
        String offerUrl) {

}