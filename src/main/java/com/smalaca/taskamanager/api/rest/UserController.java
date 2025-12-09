package com.smalaca.taskamanager.api.rest;

import com.google.common.annotations.VisibleForTesting;
import com.smalaca.taskamanager.dto.UserDto;
import com.smalaca.taskamanager.email.EmailAddressValidator;
import com.smalaca.taskamanager.exception.UserNotFoundException;
import com.smalaca.taskamanager.model.embedded.EmailAddress;
import com.smalaca.taskamanager.model.embedded.PhoneNumber;
import com.smalaca.taskamanager.model.embedded.UserName;
import com.smalaca.taskamanager.model.entities.User;
import com.smalaca.taskamanager.model.enums.TeamRole;
import com.smalaca.taskamanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@SuppressWarnings("checkstyle:ClassFanOutComplexity")
public class UserController {
    private final UserRepository userRepository;
    private final EmailAddressValidator emailAddressValidator;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
        emailAddressValidator = new EmailAddressValidator();
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> usersDtos = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setFirstName(user.getUserName().getFirstName());
            userDto.setLastName(user.getUserName().getLastName());
            userDto.setLogin(user.getLogin());
            userDto.setPassword(user.getPassword());

            TeamRole teamRole = user.getTeamRole();
            if (teamRole != null) {
                userDto.setTeamRole(teamRole.name());
            }

            PhoneNumber phoneNumber = user.getPhoneNumber();
            if (phoneNumber != null) {
                userDto.setPhonePrefix(phoneNumber.getPrefix());
                userDto.setPhoneNumber(phoneNumber.getNumber());
            }

            EmailAddress emailAddress = user.getEmailAddress();
            if (emailAddress != null) {
                userDto.setEmailAddress(emailAddress.getEmailAddress());
            }

            usersDtos.add(userDto);
        }

        return new ResponseEntity<>(usersDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        try {
            User user = getUserById(id);

            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setFirstName(user.getUserName().getFirstName());
            userDto.setLastName(user.getUserName().getLastName());
            userDto.setLogin(user.getLogin());
            userDto.setPassword(user.getPassword());

            TeamRole teamRole = user.getTeamRole();
            if (teamRole != null) {
                userDto.setTeamRole(teamRole.name());
            }

            PhoneNumber phoneNumber = user.getPhoneNumber();
            if (phoneNumber != null) {
                userDto.setPhonePrefix(phoneNumber.getPrefix());
                userDto.setPhoneNumber(phoneNumber.getNumber());
            }

            EmailAddress emailAddress = user.getEmailAddress();
            if (emailAddress != null) {
                userDto.setEmailAddress(emailAddress.getEmailAddress());
            }

            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDto userDto, UriComponentsBuilder uriComponentsBuilder) {
        if (exists(userDto)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            User user = new User();
            updateUserTeamRole(userDto, user);
            UserName userName = new UserName();
            userName.setFirstName(userDto.getFirstName());
            userName.setLastName(userDto.getLastName());
            user.setUserName(userName);
            user.setLogin(userDto.getLogin());
            updatePassword(userDto, user);

            User saved = userRepository.save(user);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriComponentsBuilder.path("/user/{id}").buildAndExpand(saved.getId()).toUri());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }
    }

    @VisibleForTesting
    void updatePassword(UserDto userDto, User user) {
        String password = userDto.getPassword();
        String hashedPassword = String.valueOf(password.hashCode());
        user.setPassword(hashedPassword);
    }

    @VisibleForTesting
    void updateUserTeamRole(UserDto userDto, User user) {
        if (TeamRole.isSupported(userDto.getTeamRole())) {
            user.setTeamRole(TeamRole.valueOf(userDto.getTeamRole()));
        } else {
            user.setTeamRole(TeamRole.UNDEFINED);
        }
    }

    private boolean exists(UserDto userDto) {
        return !userRepository.findByUserNameFirstNameAndUserNameLastName(userDto.getFirstName(), userDto.getLastName()).isEmpty();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
        User user;

        try {
            user = getUserById(id);
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (userDto.getLogin() != null) {
            user.setLogin(userDto.getLogin());
        }

        if (userDto.getPassword() != null) {
            user.setPassword(userDto.getPassword());
        }

        if (userDto.getPhoneNumber() != null) {
            PhoneNumber phoneNumber = new PhoneNumber();
            phoneNumber.setPrefix(userDto.getPhonePrefix());
            phoneNumber.setNumber(userDto.getPhoneNumber());
            user.setPhoneNumber(phoneNumber);
        }

        updateEmailAddressWhenValid(userDto, user);
        updateTeamRoleIfSupported(userDto, user);

        User updated = userRepository.save(user);

        UserDto response = new UserDto();
        response.setId(updated.getId());
        response.setFirstName(updated.getUserName().getFirstName());
        response.setLastName(updated.getUserName().getLastName());
        response.setLogin(updated.getLogin());
        response.setPassword(updated.getPassword());

        TeamRole teamRole = updated.getTeamRole();
        if (teamRole != null) {
            response.setTeamRole(teamRole.name());
        }

        PhoneNumber phoneNumber = updated.getPhoneNumber();
        if (phoneNumber != null) {
            response.setPhonePrefix(phoneNumber.getPrefix());
            response.setPhoneNumber(phoneNumber.getNumber());
        }

        EmailAddress emailAddress = updated.getEmailAddress();
        if (emailAddress != null) {
            response.setEmailAddress(emailAddress.getEmailAddress());
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @VisibleForTesting
    void updateEmailAddressWhenValid(UserDto userDto, User user) {
        if (emailAddressValidator.isValid(userDto.getEmailAddress())) {
            EmailAddress emailAddress = new EmailAddress();
            emailAddress.setEmailAddress(userDto.getEmailAddress());
            user.setEmailAddress(emailAddress);
        }
    }

    @VisibleForTesting
    void updateTeamRoleIfSupported(UserDto userDto, User user) {
        if (TeamRole.isSupported(userDto.getTeamRole())) {
            user.setTeamRole(TeamRole.valueOf(userDto.getTeamRole()));
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        User user;

        try {
            user = getUserById(id);
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userRepository.delete(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private User getUserById(Long id) {
        Optional<User> user;
        user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        return user.get();
    }
}
