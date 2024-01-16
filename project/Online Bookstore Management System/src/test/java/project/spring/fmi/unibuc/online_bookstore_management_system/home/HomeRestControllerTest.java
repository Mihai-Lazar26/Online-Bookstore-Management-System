package project.spring.fmi.unibuc.online_bookstore_management_system.home;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.spring.fmi.unibuc.online_bookstore_management_system.home.HomeRestController;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HomeRestControllerTest {

    private HomeRestController homeRestController;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        homeRestController = new HomeRestController();
        this.mockMvc = MockMvcBuilders.standaloneSetup(homeRestController).build();
    }

    @Test
    void shouldReturnUnauthorizedForHomePageWhenUserNotLoggedIn() throws Exception {
        try (MockedStatic<UserEntity> userEntityMockedStatic = Mockito.mockStatic(UserEntity.class)) {

            mockMvc.perform(get("/api/home"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(mvcResult -> {
                        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
                        assertEquals("User not logged in", mvcResult.getResponse().getContentAsString());
                    });
        }
    }

    @Test
    void shouldReturnHomeResponseForHomePageWhenUserLoggedIn() throws Exception {
        UserEntity mockUser = mock(UserEntity.class);
        when(mockUser.getUsername()).thenReturn("testUser");
        when(mockUser.getAdmin()).thenReturn(false);

        UserEntity.signedInUser = mockUser;

        try (MockedStatic<UserEntity> userEntityMockedStatic = Mockito.mockStatic(UserEntity.class)) {

            mockMvc.perform(get("/api/home"))
                    .andExpect(status().isOk())
                    .andExpect(mvcResult -> {
                        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
                    });
        }
    }
}
