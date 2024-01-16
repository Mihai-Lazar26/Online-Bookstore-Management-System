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
        UserEntity.signedInUser = new UserEntity();

        String result = userController.authPage(mock(Model.class));

        assertEquals("redirect:/books/all", result);
        UserEntity.signedInUser = null;
    }

    @Test
    void testAuthPageNoSignedInUser() {
        UserEntity.signedInUser = null;

        String result = userController.authPage(mock(Model.class));

        assertEquals("authentication", result);
    }

    @Test
    void testLogInPageSignedInUser() {
        UserEntity.signedInUser = new UserEntity();

        String result = userController.logInPage(mock(Model.class));

        assertEquals("redirect:/", result);
        UserEntity.signedInUser = null;
    }

    @Test
    void testLogInPageNoSignedInUser() {
        UserEntity.signedInUser = null;

        String result = userController.logInPage(mock(Model.class));

        assertEquals("login", result);
    }

    @Test
    void testRegisterPageSignedInUser() {
        UserEntity.signedInUser = new UserEntity();

        String result = userController.registerPage(mock(Model.class));

        assertEquals("redirect:/", result);
        UserEntity.signedInUser = null;
    }

    @Test
    void testRegisterPageNoSignedInUser() {
        UserEntity.signedInUser = null;

        String result = userController.registerPage(mock(Model.class));

        assertEquals("register", result);
    }

    @Test
    void testLogInSubmitValidUser() {
        UserEntity.signedInUser = null;
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setPassword("testPassword");

        when(userService.logInUser(any(UserEntity.class))).thenReturn(true);

        String result = userController.logInSubmit(mock(Model.class), user);

        assertEquals("redirect:/", result);
        verify(userService, times(1)).logInUser(any(UserEntity.class));
    }

    @Test
    void testLogInSubmitInvalidUser() {
        UserEntity.signedInUser = null;
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setPassword("testPassword");

        when(userService.logInUser(any(UserEntity.class))).thenReturn(false);

        String result = userController.logInSubmit(mock(Model.class), user);

        assertEquals("login", result);
        verify(userService, times(1)).logInUser(any(UserEntity.class));
    }

    @Test
    void testRegisterSubmitPasswordsNotMatching() {
        UserEntity.signedInUser = null;
        UserEntity user = new UserEntity();
        user.setPassword("password");
        String confirmPassword = "differentPassword";

        String result = userController.registerSubmit(mock(Model.class), user, confirmPassword);

        assertEquals("register", result);
    }

    @Test
    void testRegisterSubmitExistingUser() {
        UserEntity.signedInUser = null;
        UserEntity user = new UserEntity();
        user.setUsername("existingUser");
        user.setPassword("existingUserConfirm");
        String confirmPassword = "existingUserConfirm";

        when(userService.findByUsername(any(String.class))).thenReturn(user);

        String result = userController.registerSubmit(mock(Model.class), user, confirmPassword);

        assertEquals("register", result);
        verify(userService, times(1)).findByUsername(any(String.class));
    }

    @Test
    void testRegisterSubmitValidUser() {
        UserEntity.signedInUser = null;
        UserEntity user = new UserEntity();
        user.setUsername("newUser");
        user.setPassword("newUserConfirm");
        String confirmPassword = "newUserConfirm";

        when(userService.findByUsername(any(String.class))).thenReturn(null);

        String result = userController.registerSubmit(mock(Model.class), user, confirmPassword);

        assertEquals("authentication", result);
        verify(userService, times(1)).saveUser(any(UserEntity.class));
    }

    @Test
    void testLogout() {
        UserEntity.signedInUser = new UserEntity();

        String result = userController.logout(mock(Model.class));

        assertEquals("redirect:/auth", result);
        assertEquals(null, UserEntity.signedInUser);
    }

    @Test
    void testManageUsersSignedInUser() {
        UserEntity.signedInUser = new UserEntity();

        String result = userController.manageUsers(mock(Model.class));

        assertEquals("redirect:/", result);
        UserEntity.signedInUser = null;
    }

    @Test
    void testManageUsersNotAdmin() {
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(false);

        String result = userController.manageUsers(mock(Model.class));

        assertEquals("redirect:/", result);
        UserEntity.signedInUser = null;
    }

    @Test
    void testManageUsersAdmin() {
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(true);
        List<UserEntity> users = new ArrayList<>();
        users.add(new UserEntity());
        when(userService.getAllUsers()).thenReturn(users);

        String result = userController.manageUsers(mock(Model.class));

        assertEquals("userManage", result);
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testDeleteUser() {
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(true);

        String result = userController.deleteUser(123L);

        assertEquals("redirect:/", result);
        verify(userService, times(1)).deleteUser(123L);
    }

    @Test
    void testMakeUserAdmin() {
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(true);
        UserEntity userToUpdate = new UserEntity();
        userToUpdate.setUserID(456L);

        when(userService.findUserById(456L)).thenReturn(userToUpdate);

        String result = userController.makeUserAdmin(456L);

        assertEquals("redirect:/", result);
        assertTrue(userToUpdate.getAdmin());
        verify(userService, times(1)).saveUser(userToUpdate);
    }

    @Test
    void testRemoveUserAdmin() {
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(true);
        UserEntity userToUpdate = new UserEntity();
        userToUpdate.setUserID(789L);
        userToUpdate.setAdmin(true);

        when(userService.findUserById(789L)).thenReturn(userToUpdate);

        String result = userController.removeUserAdmin(789L);

        assertEquals("redirect:/", result);
        assertFalse(userToUpdate.getAdmin());
        verify(userService, times(1)).saveUser(userToUpdate);
    }
}
