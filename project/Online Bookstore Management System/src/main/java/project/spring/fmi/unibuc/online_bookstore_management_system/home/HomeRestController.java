package project.spring.fmi.unibuc.online_bookstore_management_system.home;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;

@RestController
public class HomeRestController {

    @GetMapping("/api/home")
    public ResponseEntity<Object> homePage() {
        if (UserEntity.signedInUser == null) {
            return new ResponseEntity<>("User not logged in", HttpStatus.UNAUTHORIZED);
        }

        // Pass user information to the response
        HomeResponse homeResponse = new HomeResponse(
                UserEntity.signedInUser.getUsername(),
                UserEntity.signedInUser.getAdmin(),
                UserEntity.signedInUser
        );

        return new ResponseEntity<>(homeResponse, HttpStatus.OK);
    }

    private static class HomeResponse {
        private final String username;
        private final boolean isAdmin;
        private final UserEntity signedInUser;

        public HomeResponse(String username, boolean isAdmin, UserEntity signedInUser) {
            this.username = username;
            this.isAdmin = isAdmin;
            this.signedInUser = signedInUser;
        }

        public String getUsername() {
            return username;
        }

        public boolean isAdmin() {
            return isAdmin;
        }

        public UserEntity getSignedInUser() {
            return signedInUser;
        }
    }
}
