package pl.wj.joboffers.domain.user.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.wj.joboffers.domain.user.model.dto.UserRegisterRequestDto;
import pl.wj.joboffers.domain.user.model.dto.UserResponseDto;
import pl.wj.joboffers.domain.user.model.dto.UserSecurityDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.id())
                .username(user.username())
                .build();
    }

    public static User toUser(UserRegisterRequestDto userRegisterRequestDto, String encodedPassword) {
        return User.builder()
                .username(userRegisterRequestDto.username())
                .password(encodedPassword)
                .build();
    }

    public static UserSecurityDto toUserSecurityDto(User user) {
        return UserSecurityDto.builder()
                .username(user.username())
                .password(user.password())
                .build();
    }
}
