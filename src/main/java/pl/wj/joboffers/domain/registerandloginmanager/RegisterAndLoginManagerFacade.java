package pl.wj.joboffers.domain.registerandloginmanager;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.wj.joboffers.domain.registerandloginmanager.model.dto.JwtResponseDto;
import pl.wj.joboffers.domain.user.UserFacade;
import pl.wj.joboffers.domain.user.model.User;
import pl.wj.joboffers.domain.user.model.UserMapper;
import pl.wj.joboffers.domain.user.model.dto.UserLoginRequestDto;
import pl.wj.joboffers.domain.user.model.dto.UserRegisterRequestDto;
import pl.wj.joboffers.domain.user.model.dto.UserResponseDto;
import pl.wj.joboffers.exception.definition.ResourceNotFoundException;

@RequiredArgsConstructor
@Component
public class RegisterAndLoginManagerFacade {
    private final UserFacade userFacade;
    private final RegisterAndLoginManagerRepository registerAndLoginManagerRepository;
    private final AuthenticationManager authenticationManager;


    public UserResponseDto getUserByUsername(String username) throws BadCredentialsException {
        try {
            return userFacade.getUserByUsername(username);
        } catch (ResourceNotFoundException e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    public UserResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        User user = UserMapper.toUser(userRegisterRequestDto);
        user = registerAndLoginManagerRepository.save(user);
        return UserMapper.toUserResponseDto(user);
    }

    public JwtResponseDto loginUser(UserLoginRequestDto userLoginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userLoginRequestDto.username(), userLoginRequestDto.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        User user = (User) authentication.getPrincipal();
        return JwtResponseDto.builder().build();
//        return JwtResponseDto.builder()
//                .token(createToken(user))
//                .username(user.username())
//                .build();
    }
}
