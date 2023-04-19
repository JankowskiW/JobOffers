package pl.wj.joboffers.domain.user;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.wj.joboffers.domain.user.model.dto.UserRegisterRequestDto;
import pl.wj.joboffers.domain.user.model.dto.UserResponseDto;
import pl.wj.joboffers.domain.user.model.dto.UserSecurityDto;
import pl.wj.joboffers.exception.definition.ResourceNotFoundException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserFacadeTest {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository = new InMemoryUserRepository();
    private final UserFacade userFacade = new UserFacade(userRepository, passwordEncoder);

    @Test
    void shouldReturnUserByUsernameIfExists() {
        // given
        String username = "username";
        String password = "password";
        UserRegisterRequestDto registerUserDto = new UserRegisterRequestDto(username, password);
        userFacade.addUser(registerUserDto);

        // when
        UserSecurityDto response = userFacade.getUserSecurityByUsername(username);

        // then
        assertAll(
                () -> assertThat(response.username()).isEqualTo(username),
                () -> assertThat(passwordEncoder.matches(password, response.password())).isTrue()
        );
    }

    @Test
    public void shouldAddNewUserIfDoesNotExist() {
        // given
        UserRegisterRequestDto registerUserDto = new UserRegisterRequestDto("username", "pass");

        // when
        UserResponseDto response = userFacade.addUser(registerUserDto);

        // then
        assertThat(response.username()).isEqualTo(registerUserDto.username());
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionIfUserDoesNotExist() {
        // given
        String username = "username";

        // when
        Throwable thrown = catchThrowable(() -> userFacade.getUserSecurityByUsername(username));

        // then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }
}