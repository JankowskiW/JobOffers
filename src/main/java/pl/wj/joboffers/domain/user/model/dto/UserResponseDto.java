package pl.wj.joboffers.domain.user.model.dto;

import lombok.Builder;

@Builder
public record UserResponseDto(String id, String username, String password) {
}
