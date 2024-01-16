package project.spring.fmi.unibuc.online_bookstore_management_system.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;

@Controller
public class HomeController {

    @GetMapping
    public String homePage(Model model) {
        if (UserEntity.signedInUser == null) {
            return "redirect:/auth";
        }

        model.addAttribute("username", UserEntity.signedInUser.getUsername());
        model.addAttribute("isAdmin", UserEntity.signedInUser.getAdmin());
        model.addAttribute("signedIn", UserEntity.signedInUser);

        return "homePage";
    }
}
