package pl.wj.joboffers.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.wj.joboffers.domain.user.model.User;
import pl.wj.joboffers.domain.user.model.UserMapper;
import pl.wj.joboffers.domain.user.model.dto.UserRegisterRequestDto;
import pl.wj.joboffers.domain.user.model.dto.UserResponseDto;
import pl.wj.joboffers.exception.definition.ResourceNotFoundException;

@RequiredArgsConstructor
@Component
public class UserFacade {
    private final UserRepository userRepository;

    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserMapper.toUserResponseDto(user);
    }

    public UserResponseDto addUser(UserRegisterRequestDto userRegisterRequestDto) {
        User user = UserMapper.toUser(userRegisterRequestDto);
        user = userRepository.save(user);
        return UserMapper.toUserResponseDto(user);
    }
}
