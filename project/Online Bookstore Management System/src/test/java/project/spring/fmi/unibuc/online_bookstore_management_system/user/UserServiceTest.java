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
        // Given
        String username = "testUser";
        UserEntity expectedUser = new UserEntity(1L, username, "password", "test@example.com");
        when(userRepository.findByUsername(username)).thenReturn(expectedUser);

        // When
        UserEntity result = userService.findByUsername(username);

        // Then
        assertEquals(expectedUser, result);
    }

    @Test
    void testSaveUser() {
        // Given
        UserEntity userToSave = new UserEntity("newUser", "password", "newuser@example.com");

        // When
        userService.saveUser(userToSave);

        // Then
        verify(userRepository, times(1)).save(userToSave);
    }

    @Test
    void testLogInUserValidUser() {
        // Given
        String username = "testUser";
        String password = "password";
        UserEntity existingUser = new UserEntity(1L, username, password, "test@example.com");
        UserEntity userToLogIn = new UserEntity(username, password, null);
        when(userRepository.findByUsername(username)).thenReturn(existingUser);

        // When
        boolean result = userService.logInUser(userToLogIn);

        // Then
        assertTrue(result);
        assertEquals(existingUser, UserEntity.signedInUser);
    }

    @Test
    void testLogInUserInvalidUser() {
        // Given
        String username = "testUser";
        String password = "password";
        UserEntity userToLogIn = new UserEntity(username, password, null);
        when(userRepository.findByUsername(username)).thenReturn(null);

        // When
        boolean result = userService.logInUser(userToLogIn);

        // Then
        assertFalse(result);
        assertNull(UserEntity.signedInUser);
    }

    @Test
    void testFindUserById() {
        // Given
        Long userId = 1L;
        UserEntity expectedUser = new UserEntity(userId, "testUser", "password", "test@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // When
        UserEntity result = userService.findUserById(userId);

        // Then
        assertEquals(expectedUser, result);
    }

    @Test
    void testFindUserByIdNotFound() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        UserEntity result = userService.findUserById(userId);

        // Then
        assertNull(result);
    }

    @Test
    void testGetAllUsers() {
        // Given
        UserEntity user1 = new UserEntity(1L, "user1", "password", "user1@example.com");
        UserEntity user2 = new UserEntity(2L, "user2", "password", "user2@example.com");
        List<UserEntity> expectedUsers = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // When
        List<UserEntity> result = userService.getAllUsers();

        // Then
        assertEquals(expectedUsers, result);
    }

    @Test
    void testDeleteUser() {
        // Given
        Long userId = 1L;
        UserEntity userToDelete = new UserEntity(userId, "userToDelete", "password", "user@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(userToDelete));

        // When
        userService.deleteUser(userId);

        // Then
        verify(cartService, times(1)).deleteCartAndCartItemsByUser(userToDelete);
        verify(userRepository, times(1)).delete(userToDelete);
        assertNull(UserEntity.signedInUser);
    }

    @Test
    void testDeleteUserNotFound() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        userService.deleteUser(userId);

        // Then
        verify(cartService, never()).deleteCartAndCartItemsByUser(any(UserEntity.class));
        verify(userRepository, never()).delete(any(UserEntity.class));
        assertNull(UserEntity.signedInUser);
    }
}
