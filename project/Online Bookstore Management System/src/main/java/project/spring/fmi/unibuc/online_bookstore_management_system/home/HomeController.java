package project.spring.fmi.unibuc.online_bookstore_management_system.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;

@Controller
public class HomeController {

    @GetMapping
    public String homePage(Model model) {
        // Check if the user is logged in
        if (UserEntity.signedInUser == null) {
            // Redirect to the authentication page if not logged in
            return "redirect:/auth";
        }

        // Pass user information to the view
        model.addAttribute("username", UserEntity.signedInUser.getUsername());
        model.addAttribute("isAdmin", UserEntity.signedInUser.getAdmin());
        model.addAttribute("signedIn", UserEntity.signedInUser);

        return "homePage";
    }
}
