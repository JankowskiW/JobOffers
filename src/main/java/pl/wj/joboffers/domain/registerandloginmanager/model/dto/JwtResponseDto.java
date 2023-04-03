package pl.wj.joboffers.domain.registerandloginmanager.model.dto;

import lombok.Builder;

@Builder
public record JwtResponseDto(String username, String token) {
}
