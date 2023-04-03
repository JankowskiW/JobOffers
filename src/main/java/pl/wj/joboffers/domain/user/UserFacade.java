package pl.wj.joboffers.domain.user;

import lombok.RequiredArgsConstructor;
import pl.wj.joboffers.domain.user.model.User;
import pl.wj.joboffers.domain.user.model.UserMapper;
import pl.wj.joboffers.domain.user.model.dto.UserResponseDto;
import pl.wj.joboffers.exception.definition.ResourceNotFoundException;

@RequiredArgsConstructor
public class UserFacade {
    private final UserRepository userRepository;

    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserMapper.toUserResponseDto(user);
    }
}
