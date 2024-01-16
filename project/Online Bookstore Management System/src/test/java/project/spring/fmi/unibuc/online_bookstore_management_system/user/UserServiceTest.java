package project.spring.fmi.unibuc.online_bookstore_management_system.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.spring.fmi.unibuc.online_bookstore_management_system.cart.CartService;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        UserEntity.signedInUser = null;
    }

    @Test
    void testFindByUsername() {
        String username = "testUser";
        UserEntity expectedUser = new UserEntity(1L, username, "password", "test@example.com");
        when(userRepository.findByUsername(username)).thenReturn(expectedUser);

        UserEntity result = userService.findByUsername(username);

        assertEquals(expectedUser, result);
    }

    @Test
    void testSaveUser() {
        UserEntity userToSave = new UserEntity("newUser", "password", "newuser@example.com");

        userService.saveUser(userToSave);

        verify(userRepository, times(1)).save(userToSave);
    }

    @Test
    void testLogInUserValidUser() {
        String username = "testUser";
        String password = "password";
        UserEntity existingUser = new UserEntity(1L, username, password, "test@example.com");
        UserEntity userToLogIn = new UserEntity(username, password, null);
        when(userRepository.findByUsername(username)).thenReturn(existingUser);

        boolean result = userService.logInUser(userToLogIn);

        assertTrue(result);
        assertEquals(existingUser, UserEntity.signedInUser);
    }

    @Test
    void testLogInUserInvalidUser() {
        String username = "testUser";
        String password = "password";
        UserEntity userToLogIn = new UserEntity(username, password, null);
        when(userRepository.findByUsername(username)).thenReturn(null);

        boolean result = userService.logInUser(userToLogIn);

        assertFalse(result);
        assertNull(UserEntity.signedInUser);
    }

    @Test
    void testFindUserById() {
        Long userId = 1L;
        UserEntity expectedUser = new UserEntity(userId, "testUser", "password", "test@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        UserEntity result = userService.findUserById(userId);

        assertEquals(expectedUser, result);
    }

    @Test
    void testFindUserByIdNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserEntity result = userService.findUserById(userId);

        assertNull(result);
    }

    @Test
    void testGetAllUsers() {
        UserEntity user1 = new UserEntity(1L, "user1", "password", "user1@example.com");
        UserEntity user2 = new UserEntity(2L, "user2", "password", "user2@example.com");
        List<UserEntity> expectedUsers = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<UserEntity> result = userService.getAllUsers();

        assertEquals(expectedUsers, result);
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;
        UserEntity userToDelete = new UserEntity(userId, "userToDelete", "password", "user@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(userToDelete));

        userService.deleteUser(userId);

        verify(cartService, times(1)).deleteCartAndCartItemsByUser(userToDelete);
        verify(userRepository, times(1)).delete(userToDelete);
        assertNull(UserEntity.signedInUser);
    }

    @Test
    void testDeleteUserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        userService.deleteUser(userId);

        verify(cartService, never()).deleteCartAndCartItemsByUser(any(UserEntity.class));
        verify(userRepository, never()).delete(any(UserEntity.class));
        assertNull(UserEntity.signedInUser);
    }
}
