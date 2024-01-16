package project.spring.fmi.unibuc.online_bookstore_management_system.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> authPage() {
        if (UserEntity.signedInUser != null) {
            return new ResponseEntity<>("Already authenticated", HttpStatus.OK);
        }
        return new ResponseEntity<>("Authentication page", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/login")
    public ResponseEntity<Object> logInPage() {
        if (UserEntity.signedInUser != null) {
            return new ResponseEntity<>("Already logged in", HttpStatus.OK);
        }
        return new ResponseEntity<>("Login page", HttpStatus.OK);
    }

    @GetMapping("/register")
    public ResponseEntity<Object> registerPage() {
        if (UserEntity.signedInUser != null) {
            return new ResponseEntity<>("Already registered", HttpStatus.OK);
        }
        return new ResponseEntity<>("Register page", HttpStatus.OK);
    }

    @PostMapping("/login/submit")
    public ResponseEntity<Object> logInSubmit(@RequestBody UserEntity user) {
        if (UserEntity.signedInUser != null) {
            return new ResponseEntity<>("Already logged in", HttpStatus.OK);
        }

        boolean loggedIn = userService.logInUser(user);
        if (loggedIn) {
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register/submit")
    public ResponseEntity<Object> registerSubmit(@RequestBody UserEntity user, @RequestParam("confirmPassword") String confirmPassword) {
        if (UserEntity.signedInUser != null) {
            return new ResponseEntity<>("Already registered", HttpStatus.OK);
        }

        String userPassword = user.getPassword();
        if (!confirmPassword.equals(userPassword)) {
            return new ResponseEntity<>("Passwords do not match", HttpStatus.BAD_REQUEST);
        }

        UserEntity existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }

        userService.saveUser(user);
        return new ResponseEntity<>("Registration successful", HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout() {
        UserEntity.signedInUser = null;
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }

    @GetMapping("/manage")
    public ResponseEntity<Object> manageUsers() {
        if (UserEntity.signedInUser == null || !UserEntity.signedInUser.getAdmin()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        List<UserEntity> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteUser(@RequestParam Long userId) {
        if (UserEntity.signedInUser != null && UserEntity.signedInUser.getAdmin()) {
            userService.deleteUser(userId);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/makeAdmin")
    public ResponseEntity<Object> makeUserAdmin(@RequestParam Long userId) {
        UserEntity currentUser = UserEntity.signedInUser;
        if (currentUser != null && currentUser.getAdmin()) {
            UserEntity userToUpdate = userService.findUserById(userId);
            if (userToUpdate != null) {
                userToUpdate.setAdmin(true);
                userService.saveUser(userToUpdate);
                return new ResponseEntity<>("User is now an admin", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/removeAdmin")
    public ResponseEntity<Object> removeUserAdmin(@RequestParam Long userId) {
        UserEntity currentUser = UserEntity.signedInUser;
        if (currentUser != null && currentUser.getAdmin()) {
            UserEntity userToUpdate = userService.findUserById(userId);
            if (userToUpdate != null) {
                userToUpdate.setAdmin(false);
                userService.saveUser(userToUpdate);
                return new ResponseEntity<>("Admin status removed", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/make-admin-forced/{userId}")
    public ResponseEntity<Object> makeUserAdminForced(@PathVariable Long userId) {
        UserEntity userToUpdate = userService.findUserById(userId);
        if (userToUpdate != null) {
            userToUpdate.setAdmin(true);
            userService.saveUser(userToUpdate);
            return new ResponseEntity<>("User is now an admin", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
}
