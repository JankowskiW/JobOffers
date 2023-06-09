package pl.wj.joboffers.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.wj.joboffers.domain.user.model.User;
import pl.wj.joboffers.domain.user.model.UserMapper;
import pl.wj.joboffers.domain.user.model.dto.UserRegisterRequestDto;
import pl.wj.joboffers.domain.user.model.dto.UserResponseDto;
import pl.wj.joboffers.domain.user.model.dto.UserSecurityDto;
import pl.wj.joboffers.exception.definition.ResourceNotFoundException;

@RequiredArgsConstructor
@Component
@Log4j2
public class UserFacade {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSecurityDto getUserSecurityByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserMapper.toUserSecurityDto(user);
    }

    public UserResponseDto addUser(UserRegisterRequestDto userRegisterRequestDto) {
        String encodedPassword = passwordEncoder.encode(userRegisterRequestDto.password());
        User user = UserMapper.toUser(userRegisterRequestDto, encodedPassword);
        user = userRepository.save(user);
        return UserMapper.toUserResponseDto(user);
    }
}
