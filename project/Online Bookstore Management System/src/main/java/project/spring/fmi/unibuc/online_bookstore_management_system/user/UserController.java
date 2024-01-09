package project.spring.fmi.unibuc.online_bookstore_management_system.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        return "authentication";
    }

    @GetMapping("/login")
    public String logInPage(Model model) {
        if (UserEntity.signedInUser != null)
            return "redirect:/app";
        model.addAttribute("userEntity", new UserEntity());
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        if (UserEntity.signedInUser != null)
            return "redirect:/app";
        model.addAttribute("userEntity", new UserEntity());
        return "register";
    }

    @PostMapping("/login/submit")
    public String logInSubmit(Model model, @ModelAttribute("userEntity") UserEntity user) {
        if (UserEntity.signedInUser != null)
            return "redirect:/app";

        boolean loggedIn = userService.logInUser(user);
        if (loggedIn) {
            return "redirect:/app";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }

    @PostMapping("/register/submit")
    public String registerSubmit(Model model, @ModelAttribute("userEntity") UserEntity user, @RequestParam("confirmPassword") String confirmPassword) {
        if (UserEntity.signedInUser!= null)
            return "redirect:/app";

        if (!user.getPassword().equals(confirmPassword)) {
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
}
