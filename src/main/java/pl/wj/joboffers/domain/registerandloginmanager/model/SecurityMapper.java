package pl.wj.joboffers.domain.registerandloginmanager.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;
import pl.wj.joboffers.domain.user.model.dto.UserResponseDto;

import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityMapper {

    public static User toSecurityUser(UserResponseDto userResponseDto) {
        return new User(userResponseDto.username(), userResponseDto.password(), Collections.emptyList());
    }
}
