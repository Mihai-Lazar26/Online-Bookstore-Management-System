package project.spring.fmi.unibuc.online_bookstore_management_system.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String authPage(Model model) {
        if (UserEntity.signedInUser != null)
            return "redirect:/books/all";

        return "authentication";
    }

    @GetMapping("/login")
    public String logInPage(Model model) {
        if (UserEntity.signedInUser != null)
            return "redirect:/";
        model.addAttribute("userEntity", new UserEntity());
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        if (UserEntity.signedInUser != null)
            return "redirect:/";
        model.addAttribute("userEntity", new UserEntity());
        return "register";
    }

    @PostMapping("/login/submit")
    public String logInSubmit(Model model, @ModelAttribute("userEntity") UserEntity user) {
        if (UserEntity.signedInUser != null)
            return "redirect:/";

        boolean loggedIn = userService.logInUser(user);
        if (loggedIn) {
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }

    @PostMapping("/register/submit")
    public String registerSubmit(Model model, @ModelAttribute("userEntity") UserEntity user, @RequestParam("confirmPassword") String confirmPassword) {
        if (UserEntity.signedInUser != null)
            return "redirect:/";

        String userPassword = user.getPassword();
        if (!confirmPassword.equals(userPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        UserEntity existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        userService.saveUser(user);
        return "authentication";
    }

    @PostMapping("/logout")
    public String logout(Model model) {
        UserEntity.signedInUser = null;
        return "redirect:/auth";
    }

    @GetMapping("/manage")
    public String manageUsers(Model model) {
        if (UserEntity.signedInUser == null || !UserEntity.signedInUser.getAdmin())
            return "redirect:/";

        List<UserEntity> users = userService.getAllUsers();

        model.addAttribute("users", users);
        return "userManage";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long userId) {
        if (UserEntity.signedInUser != null && UserEntity.signedInUser.getAdmin()) {
            userService.deleteUser(userId);
        }
        return "redirect:/";
    }

    @PostMapping("/makeAdmin")
    public String makeUserAdmin(@RequestParam Long userId) {
        UserEntity currentUser = UserEntity.signedInUser;
        if (currentUser != null && currentUser.getAdmin()) {
            UserEntity userToUpdate = userService.findUserById(userId);
            if (userToUpdate != null) {
                userToUpdate.setAdmin(true);
                userService.saveUser(userToUpdate);
            }
        }
        return "redirect:/";
    }

    @PostMapping("/removeAdmin")
    public String removeUserAdmin(@RequestParam Long userId) {
        UserEntity currentUser = UserEntity.signedInUser;
        if (currentUser != null && currentUser.getAdmin()) {
            UserEntity userToUpdate = userService.findUserById(userId);
            if (userToUpdate != null) {
                userToUpdate.setAdmin(false);
                userService.saveUser(userToUpdate);
            }
        }
        return "redirect:/";
    }

    @GetMapping("/make-admin-forced/{userId}")
    public String makeUserAdminForced(@PathVariable Long userId) {
        UserEntity userToUpdate = userService.findUserById(userId);
        if (userToUpdate != null) {
            userToUpdate.setAdmin(true);
            userService.saveUser(userToUpdate);
        }
        return "redirect:/";
    }

}
