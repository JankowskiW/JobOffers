package pl.wj.joboffers.domain.user.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.wj.joboffers.domain.user.model.dto.UserRegisterRequestDto;
import pl.wj.joboffers.domain.user.model.dto.UserResponseDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.id())
                .username(user.username())
                .password(user.password())
                .build();
    }

    public static User toUser(UserRegisterRequestDto userRegisterRequestDto) {
        return User.builder()
                .username(userRegisterRequestDto.username())
                .password(userRegisterRequestDto.password())
                .build();
    }
}
