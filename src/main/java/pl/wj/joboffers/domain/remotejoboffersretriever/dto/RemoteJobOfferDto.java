package pl.wj.joboffers.domain.remotejoboffersretriever.dto;

import lombok.Builder;

import java.util.Objects;

@Builder
public record RemoteJobOfferDto(String title, String company, String salary, String offerUrl) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoteJobOfferDto that = (RemoteJobOfferDto) o;
        return offerUrl.equals(that.offerUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offerUrl);
    }
}
