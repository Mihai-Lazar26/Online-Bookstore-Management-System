package project.spring.fmi.unibuc.online_bookstore_management_system.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void testAuthPageSignedInUser() {
        // Given
        UserEntity.signedInUser = new UserEntity();

        // When
        String result = userController.authPage(mock(Model.class));

        // Then
        assertEquals("redirect:/books/all", result);
        UserEntity.signedInUser = null; // Reset signedInUser
    }

    @Test
    void testAuthPageNoSignedInUser() {
        // Given
        UserEntity.signedInUser = null;

        // When
        String result = userController.authPage(mock(Model.class));

        // Then
        assertEquals("authentication", result);
    }

    @Test
    void testLogInPageSignedInUser() {
        // Given
        UserEntity.signedInUser = new UserEntity();

        // When
        String result = userController.logInPage(mock(Model.class));

        // Then
        assertEquals("redirect:/", result);
        UserEntity.signedInUser = null; // Reset signedInUser
    }

    @Test
    void testLogInPageNoSignedInUser() {
        // Given
        UserEntity.signedInUser = null;

        // When
        String result = userController.logInPage(mock(Model.class));

        // Then
        assertEquals("login", result);
    }

    @Test
    void testRegisterPageSignedInUser() {
        // Given
        UserEntity.signedInUser = new UserEntity();

        // When
        String result = userController.registerPage(mock(Model.class));

        // Then
        assertEquals("redirect:/", result);
        UserEntity.signedInUser = null; // Reset signedInUser
    }

    @Test
    void testRegisterPageNoSignedInUser() {
        // Given
        UserEntity.signedInUser = null;

        // When
        String result = userController.registerPage(mock(Model.class));

        // Then
        assertEquals("register", result);
    }

    @Test
    void testLogInSubmitValidUser() {
        // Given
        UserEntity.signedInUser = null;
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setPassword("testPassword");

        when(userService.logInUser(any(UserEntity.class))).thenReturn(true);

        // When
        String result = userController.logInSubmit(mock(Model.class), user);

        // Then
        assertEquals("redirect:/", result);
        verify(userService, times(1)).logInUser(any(UserEntity.class));
    }

    @Test
    void testLogInSubmitInvalidUser() {
        // Given
        UserEntity.signedInUser = null;
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setPassword("testPassword");

        when(userService.logInUser(any(UserEntity.class))).thenReturn(false);

        // When
        String result = userController.logInSubmit(mock(Model.class), user);

        // Then
        assertEquals("login", result);
        verify(userService, times(1)).logInUser(any(UserEntity.class));
    }

    @Test
    void testRegisterSubmitPasswordsNotMatching() {
        // Given
        UserEntity.signedInUser = null;
        UserEntity user = new UserEntity();
        user.setPassword("password");
        String confirmPassword = "differentPassword";

        // When
        String result = userController.registerSubmit(mock(Model.class), user, confirmPassword);

        // Then
        assertEquals("register", result);
    }

    @Test
    void testRegisterSubmitExistingUser() {
        // Given
        UserEntity.signedInUser = null;
        UserEntity user = new UserEntity();
        user.setUsername("existingUser");
        user.setPassword("existingUserConfirm");
        String confirmPassword = "existingUserConfirm";

        when(userService.findByUsername(any(String.class))).thenReturn(user);

        // When
        String result = userController.registerSubmit(mock(Model.class), user, confirmPassword);

        // Then
        assertEquals("register", result);
        verify(userService, times(1)).findByUsername(any(String.class));
    }

    @Test
    void testRegisterSubmitValidUser() {
        // Given
        UserEntity.signedInUser = null;
        UserEntity user = new UserEntity();
        user.setUsername("newUser");
        user.setPassword("newUserConfirm");
        String confirmPassword = "newUserConfirm";

        when(userService.findByUsername(any(String.class))).thenReturn(null);

        // When
        String result = userController.registerSubmit(mock(Model.class), user, confirmPassword);

        // Then
        assertEquals("authentication", result);
        verify(userService, times(1)).saveUser(any(UserEntity.class));
    }

    @Test
    void testLogout() {
        // Given
        UserEntity.signedInUser = new UserEntity();

        // When
        String result = userController.logout(mock(Model.class));

        // Then
        assertEquals("redirect:/auth", result);
        assertEquals(null, UserEntity.signedInUser);
    }

    @Test
    void testManageUsersSignedInUser() {
        // Given
        UserEntity.signedInUser = new UserEntity();

        // When
        String result = userController.manageUsers(mock(Model.class));

        // Then
        assertEquals("redirect:/", result);
        UserEntity.signedInUser = null; // Reset signedInUser
    }

    @Test
    void testManageUsersNotAdmin() {
        // Given
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(false);

        // When
        String result = userController.manageUsers(mock(Model.class));

        // Then
        assertEquals("redirect:/", result);
        UserEntity.signedInUser = null; // Reset signedInUser
    }

    @Test
    void testManageUsersAdmin() {
        // Given
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(true);
        List<UserEntity> users = new ArrayList<>();
        users.add(new UserEntity());
        when(userService.getAllUsers()).thenReturn(users);

        // When
        String result = userController.manageUsers(mock(Model.class));

        // Then
        assertEquals("userManage", result);
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testDeleteUser() {
        // Given
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(true);

        // When
        String result = userController.deleteUser(123L);

        // Then
        assertEquals("redirect:/", result);
        verify(userService, times(1)).deleteUser(123L);
    }

    @Test
    void testMakeUserAdmin() {
        // Given
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(true);
        UserEntity userToUpdate = new UserEntity();
        userToUpdate.setUserID(456L);

        when(userService.findUserById(456L)).thenReturn(userToUpdate);

        // When
        String result = userController.makeUserAdmin(456L);

        // Then
        assertEquals("redirect:/", result);
        assertTrue(userToUpdate.getAdmin());
        verify(userService, times(1)).saveUser(userToUpdate);
    }

    @Test
    void testRemoveUserAdmin() {
        // Given
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(true);
        UserEntity userToUpdate = new UserEntity();
        userToUpdate.setUserID(789L);
        userToUpdate.setAdmin(true);

        when(userService.findUserById(789L)).thenReturn(userToUpdate);

        // When
        String result = userController.removeUserAdmin(789L);

        // Then
        assertEquals("redirect:/", result);
        assertFalse(userToUpdate.getAdmin());
        verify(userService, times(1)).saveUser(userToUpdate);
    }
}
