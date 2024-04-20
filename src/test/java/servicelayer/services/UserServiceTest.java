package servicelayer.services;

import org.example.restaurant.datalayer.dto.user.AddUserDto;
import org.example.restaurant.datalayer.dto.user.UpdateUserDto;
import org.example.restaurant.datalayer.dto.user.UserDto;
import org.example.restaurant.datalayer.entities.User;
import org.example.restaurant.datalayer.mappers.UserMapper;
import org.example.restaurant.datalayer.repositories.UserRepository;
import org.example.restaurant.servicelayer.OperationResult;
import org.example.restaurant.servicelayer.PasswordHasherImpl;
import org.example.restaurant.servicelayer.services.UserService;
import org.example.restaurant.servicelayer.validators.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    PasswordHasherImpl passwordHasher;
    @Mock
    UserMapper userMapper;
    @Mock
    UserValidator userValidator;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userService;

    User user;
    UserDto userDto;
    AddUserDto addUserDto;
    UpdateUserDto updateUserDto;

    @BeforeEach
    void prepareEntities() {
        user = new User(1L, "user", "pass", "phone", "user");
        userDto = new UserDto(1L, "user", "phone", "user");
        addUserDto = new AddUserDto("addUser", "pass", "phone");
        updateUserDto = new UpdateUserDto(1L, "updateUser", "phone");
    }

    @Test
    void loginTest() {
        String login = user.getLogin();
        String pass = user.getPassword();

        when(userRepository.getByLogin(login)).thenReturn(user);
        when(userMapper.map(user)).thenReturn(userDto);
        when(passwordHasher.check(eq(pass), anyString(), eq(user.getPassword())))
                .thenReturn(true);

        OperationResult<UserDto> result = userService.login(login, pass);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(userDto);
    }

    @Test
    void loginWithUnexistingLoginTest() {
        when(userRepository.getByLogin(anyString())).thenReturn(null);

        OperationResult<UserDto> result = userService.login("abc", "abc");

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void loginWithUncorrectPasswordTest() {
        when(userRepository.getByLogin(user.getLogin())).thenReturn(user);
        when(passwordHasher.check(anyString(), anyString(), eq(user.getPassword())))
                .thenReturn(false);

        OperationResult<UserDto> result = userService.login(user.getLogin(), "abc");

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void registerTest() {
        when(userValidator.isAddValid(addUserDto)).thenReturn(true);
        when(userRepository.getByLogin(addUserDto.getLogin())).thenReturn(null);
        when(userMapper.map(addUserDto)).thenReturn(user);
        when(passwordHasher.hash(eq(user.getPassword()), anyString()))
                .thenReturn(user.getPassword());
        when(userRepository.add(user)).thenReturn(user);
        when(userMapper.map(user)).thenReturn(userDto);

        OperationResult<UserDto> result = userService.register(addUserDto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(userDto);
    }

    @Test
    void registerAddFailTest() {
        when(userValidator.isAddValid(addUserDto)).thenReturn(true);
        when(userRepository.getByLogin(addUserDto.getLogin())).thenReturn(null);
        when(userMapper.map(addUserDto)).thenReturn(user);
        when(passwordHasher.hash(eq(user.getPassword()), anyString()))
                .thenReturn(user.getPassword());
        when(userRepository.add(user)).thenReturn(null);

        OperationResult<UserDto> result = userService.register(addUserDto);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void registerWithIncorrectDataTest() {
        when(userValidator.isAddValid(addUserDto)).thenReturn(false);

        OperationResult<UserDto> result = userService.register(addUserDto);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void registerWithExistingLoginTest() {
        when(userValidator.isAddValid(addUserDto)).thenReturn(true);
        when(userRepository.getByLogin(addUserDto.getLogin())).thenReturn(user);

        OperationResult<UserDto> result = userService.register(addUserDto);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void getUserByIdTest() {
        when(userRepository.getById(anyLong())).thenReturn(user);
        when(userMapper.map(user)).thenReturn(userDto);

        OperationResult<UserDto> result = userService.getById(user.getId());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(userDto);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1L})
    void getUserByInvalidIdTest(Long userId) {
        OperationResult<UserDto> result = userService.getById(userId);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void getUnexistingUserByIdTest() {
        when(userRepository.getById(anyLong())).thenReturn(null);

        OperationResult<UserDto> result = userService.getById(user.getId());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void updateUserTest() {
        when(userValidator.isUpdateValid(updateUserDto)).thenReturn(true);
        when(userRepository.getById(updateUserDto.getId())).thenReturn(user);
        when(userMapper.map(updateUserDto)).thenReturn(user);
        when(userRepository.update(user)).thenReturn(user);
        when(userMapper.map(user)).thenReturn(userDto);

        OperationResult<UserDto> result = userService.update(updateUserDto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(userDto);
    }

    @Test
    void updateUserFailTest() {
        when(userValidator.isUpdateValid(updateUserDto)).thenReturn(true);
        when(userRepository.getById(updateUserDto.getId())).thenReturn(user);
        when(userMapper.map(updateUserDto)).thenReturn(user);
        when(userRepository.update(user)).thenReturn(null);

        OperationResult<UserDto> result = userService.update(updateUserDto);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void updateUserWithIncorrectDataTest() {
        when(userValidator.isUpdateValid(updateUserDto)).thenReturn(false);

        OperationResult<UserDto> result = userService.update(updateUserDto);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void updateUnexistingUserTest() {
        when(userValidator.isUpdateValid(updateUserDto)).thenReturn(true);
        when(userRepository.getById(updateUserDto.getId())).thenReturn(null);

        OperationResult<UserDto> result = userService.update(updateUserDto);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }
}
