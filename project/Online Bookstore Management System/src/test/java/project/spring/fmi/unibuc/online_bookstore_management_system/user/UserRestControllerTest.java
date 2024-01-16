package project.spring.fmi.unibuc.online_bookstore_management_system.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class UserRestControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserRestController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userController = new UserRestController(userService);
    }

    @Test
    void testAuthPage_UnauthenticatedUser() {
        UserEntity.signedInUser = null;
        ResponseEntity<Object> response = userController.authPage();
        assertEquals("Authentication page", response.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testAuthPage_AuthenticatedUser() {
        UserEntity.signedInUser = new UserEntity("authenticatedUser", "password", "");
        ResponseEntity<Object> response = userController.authPage();
        assertEquals("Already authenticated", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLogInPage_UnauthenticatedUser() {
        UserEntity.signedInUser = null;
        ResponseEntity<Object> response = userController.logInPage();
        assertEquals("Login page", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLogInPage_AuthenticatedUser() {
        UserEntity.signedInUser = new UserEntity("authenticatedUser", "password", " ");
        ResponseEntity<Object> response = userController.logInPage();
        assertEquals("Already logged in", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRegisterPage_UnauthenticatedUser() {
        UserEntity.signedInUser = null;
        ResponseEntity<Object> response = userController.registerPage();
        assertEquals("Register page", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRegisterPage_AuthenticatedUser() {
        UserEntity.signedInUser = new UserEntity("authenticatedUser", "password", " ");
        ResponseEntity<Object> response = userController.registerPage();
        assertEquals("Already registered", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLogInSubmit_AlreadyLoggedIn() {
        UserEntity.signedInUser = new UserEntity("authenticatedUser", "password", " ");
        ResponseEntity<Object> response = userController.logInSubmit(new UserEntity("newUser", "password", " "));
        assertEquals("Already logged in", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLogInSubmit_ValidCredentials() {
        UserEntity.signedInUser = null;
        UserEntity validUser = new UserEntity("validUsername", "validPassword", " ");

        Mockito.when(userService.logInUser(any(UserEntity.class))).thenReturn(true);

        ResponseEntity<Object> response = userController.logInSubmit(validUser);

        assertEquals("Login successful", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLogInSubmit_InvalidCredentials() {
        UserEntity.signedInUser = null;
        UserEntity invalidUser = new UserEntity("invalidUsername", "invalidPassword", " ");

        Mockito.when(userService.logInUser(any(UserEntity.class))).thenReturn(false);

        ResponseEntity<Object> response = userController.logInSubmit(invalidUser);

        assertEquals("Invalid credentials", response.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testRegisterSubmit_AlreadyRegistered() {
        UserEntity.signedInUser = new UserEntity("authenticatedUser", "password", " ");
        ResponseEntity<Object> response = userController.registerSubmit(new UserEntity("newUser", "password", ""), "password");
        assertEquals("Already registered", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRegisterSubmit_PasswordsMismatch() {
        UserEntity.signedInUser = null;
        UserEntity newUser = new UserEntity("userWithMismatchedPasswords", "password", "");

        ResponseEntity<Object> response = userController.registerSubmit(newUser, "differentPassword");

        assertEquals("Passwords do not match", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRegisterSubmit_ExistingUsername() {
        UserEntity.signedInUser = null;
        UserEntity existingUser = new UserEntity("existingUser", "password", "");

        Mockito.when(userService.findByUsername(existingUser.getUsername())).thenReturn(existingUser);

        ResponseEntity<Object> response = userController.registerSubmit(existingUser, "password");

        assertEquals("Username already exists", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRegisterSubmit_Success() {
        UserEntity.signedInUser = null;
        UserEntity newUser = new UserEntity("newUser", "password", " ");

        Mockito.when(userService.findByUsername(newUser.getUsername())).thenReturn(null);
        Mockito.doNothing().when(userService).saveUser(any(UserEntity.class));

        ResponseEntity<Object> response = userController.registerSubmit(newUser, "password");

        assertEquals("Registration successful", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLogout() {
        UserEntity.signedInUser = new UserEntity("authenticatedUser", "password", " ");
        ResponseEntity<Object> response = userController.logout();
        assertEquals("Logged out successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testManageUsers_UnauthorizedUser() {
        UserEntity.signedInUser = null;
        ResponseEntity<Object> response = userController.manageUsers();
        assertEquals("Unauthorized", response.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testManageUsers_AuthenticatedNonAdminUser() {
        UserEntity.signedInUser = new UserEntity("authenticatedUser", "password", " ");
        ResponseEntity<Object> response = userController.manageUsers();
        assertEquals("Unauthorized", response.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testManageUsers_AdminUser() {
        UserEntity.signedInUser = new UserEntity("adminUser", "password", " ");
        UserEntity adminUser = new UserEntity("adminUser", "password", " ");
        adminUser.setAdmin(true);
        UserEntity.signedInUser.setAdmin(true);

        List<UserEntity> users = new ArrayList<>();
        users.add(adminUser);

        Mockito.when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<Object> response = userController.manageUsers();

        assertEquals(users, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteUser_UnauthorizedUser() {
        UserEntity.signedInUser = null;
        ResponseEntity<Object> response = userController.deleteUser(1L);
        assertEquals("Unauthorized", response.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testDeleteUser_NonAdminUser() {
        UserEntity.signedInUser = new UserEntity("authenticatedUser", "password", " ");
        ResponseEntity<Object> response = userController.deleteUser(1L);
        assertEquals("Unauthorized", response.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

}