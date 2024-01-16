package project.spring.fmi.unibuc.online_bookstore_management_system.home;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @Mock
    private Model model;

    @InjectMocks
    private HomeController homeController;

    @Test
    void testHomePageUserLoggedIn() {
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setUsername("testUser");
        UserEntity.signedInUser.setAdmin(true);

        String result = homeController.homePage(model);

        assertEquals("homePage", result);
        verify(model, times(1)).addAttribute("username", "testUser");
        verify(model, times(1)).addAttribute("isAdmin", true);
        verify(model, times(1)).addAttribute("signedIn", UserEntity.signedInUser);
    }

    @Test
    void testHomePageUserNotLoggedIn() {
        UserEntity.signedInUser = null;

        String result = homeController.homePage(model);

        assertEquals("redirect:/auth", result);
        verifyNoInteractions(model);
    }
}
