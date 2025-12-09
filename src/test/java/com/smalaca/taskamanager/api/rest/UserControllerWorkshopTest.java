package com.smalaca.taskamanager.api.rest;

import com.smalaca.taskamanager.dto.UserDto;
import com.smalaca.taskamanager.model.entities.User;
import com.smalaca.taskamanager.model.enums.TeamRole;
import com.smalaca.taskamanager.repository.UserRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

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
}