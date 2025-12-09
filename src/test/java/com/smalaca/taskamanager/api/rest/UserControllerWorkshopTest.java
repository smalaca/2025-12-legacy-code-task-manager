package com.smalaca.taskamanager.api.rest;

import com.smalaca.taskamanager.dto.UserDto;
import com.smalaca.taskamanager.model.entities.User;
import com.smalaca.taskamanager.model.enums.TeamRole;
import com.smalaca.taskamanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.smalaca.taskamanager.model.enums.TeamRole.UNDEFINED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class UserControllerWorkshopTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserController userController = new UserController(userRepository);

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"INVALID", "developer", "D3veloper", "Coder"})
    void shouldCreateUserWithTeamRoleAsUndefinedWhenInvalidGiven(String invalidaTeamRole) {
        UserDto userDto = new UserDto();
        userDto.setTeamRole(invalidaTeamRole);
        User user = new User();

        userController.updateUserTeamRole(userDto, user);

        assertThat(user.getTeamRole()).isEqualTo(UNDEFINED);
    }

    @ParameterizedTest
    @EnumSource(TeamRole.class)
    void shouldCreateUserWithTeamRole(TeamRole teamRole) {
        UserDto userDto = new UserDto();
        userDto.setTeamRole(teamRole.name());
        User user = new User();

        userController.updateUserTeamRole(userDto, user);

        assertThat(user.getTeamRole()).isEqualTo(teamRole);
    }

    @Test
    void shouldCreateUserWithHashedPassword() {
        UserDto userDto = new UserDto();
        String password = UUID.randomUUID().toString();
        userDto.setPassword(password);
        User user = new User();

        userController.updatePassword(userDto, user);

        assertThat(user.getPassword()).isEqualTo(String.valueOf(password.hashCode()));
        assertThat(user.getPassword()).isNotEqualTo(password);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"INVALID", "developer", "D3veloper", "Coder"})
    void shouldNotUpdateUserTeamRoleWhenInvalidGiven(String invalidaTeamRole) {
        UserDto userDto = new UserDto();
        userDto.setTeamRole(invalidaTeamRole);
        User user = new User();

        userController.updateTeamRoleIfSupported(userDto, user);

        assertThat(user.getTeamRole()).isNull();
    }

    @ParameterizedTest
    @EnumSource(TeamRole.class)
    void shouldUpdateUserTeamRole(TeamRole teamRole) {
        UserDto userDto = new UserDto();
        userDto.setTeamRole(teamRole.name());
        User user = new User();

        userController.updateTeamRoleIfSupported(userDto, user);

        assertThat(user.getTeamRole()).isEqualTo(teamRole);
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    @ValueSource(strings = {"wrongmail.com", "invalid.mail", "broken one"})
    void shouldNotUpdateUserEmailWhenInvalidGiven(String invalidaEmailAddress) {
        UserDto userDto = new UserDto();
        userDto.setEmailAddress(invalidaEmailAddress);
        User user = new User();

        userController.updateEmailAddressWhenValid(userDto, user);

        assertThat(user.getEmailAddress()).isNull();
    }

    @Test
    void shouldUpdateUserEmailAddress() {
        UserDto userDto = new UserDto();
        String correctEmailAddress = "some@mail.address";
        userDto.setEmailAddress(correctEmailAddress);
        User user = new User();

        userController.updateEmailAddressWhenValid(userDto, user);

        assertThat(user.getEmailAddress().getEmailAddress()).isEqualTo(correctEmailAddress);
    }

    @Test
    void shouldUpdateUserModificationTime() {
        User user = new User();
        LocalDateTime past = LocalDateTime.now().minusSeconds(1);

        userController.updateUserModificationTime(user);

        assertThat(user.getModifiedAt())
                .isAfter(past)
                .isBefore(LocalDateTime.now().plusSeconds(1));
    }
}