package project.spring.fmi.unibuc.online_bookstore_management_system.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookService;
import project.spring.fmi.unibuc.online_bookstore_management_system.order.OrderEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.order.OrderItemEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.order.OrderService;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OrderRestControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private OrderRestController orderRestController;

    @BeforeEach
    void setUp() {
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(true);
    }

    @Test
    void shouldReturnUnauthorizedForViewOrdersWhenNotAdmin() {
        UserEntity.signedInUser.setAdmin(false);

        ResponseEntity<Object> responseEntity = orderRestController.viewOrders();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isEqualTo("Unauthorized");
    }

    @Test
    void shouldReturnOrdersForViewOrdersWhenAdmin() {
        List<OrderEntity> orders = Collections.singletonList(new OrderEntity());
        when(orderService.getAllOrders()).thenReturn(orders);

        ResponseEntity<Object> responseEntity = orderRestController.viewOrders();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(orders);
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void shouldReturnUnauthorizedForViewUserOrdersWhenNotLoggedIn() {
        UserEntity.signedInUser = null;
        Long userId = 1L;

        ResponseEntity<Object> responseEntity = orderRestController.viewUserOrders(userId);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isEqualTo("Unauthorized");
    }

    @Test
    void shouldReturnOrdersForViewUserOrdersWhenLoggedInAndMatchesUserId() {
        Long userId = 1L;
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setUserID(1L);
        List<OrderEntity> orders = Collections.singletonList(new OrderEntity());
        when(orderService.getOrdersByUserId(userId)).thenReturn(orders);

        ResponseEntity<Object> responseEntity = orderRestController.viewUserOrders(userId);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(orders);
        verify(orderService, times(1)).getOrdersByUserId(userId);
    }

    @Test
    void shouldReturnUnauthorizedForViewUserOrdersWhenLoggedInButNotMatchingUserId() {
        Long userId = 2L;
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setUserID(1L);

        ResponseEntity<Object> responseEntity = orderRestController.viewUserOrders(userId);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isEqualTo("Unauthorized");
    }

    @Test
    void shouldUpdateOrderStatus() {
        Long orderId = 1L;
        String status = "Shipped";
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Order status updated", HttpStatus.OK);

        doNothing().when(orderService).updateOrderStatus(orderId, status);

        ResponseEntity<String> responseEntity = orderRestController.updateStatus(orderId, status);

        assertThat(responseEntity).isEqualTo(expectedResponse);
        verify(orderService, times(1)).updateOrderStatus(orderId, status);
    }

    @Test
    void shouldReturnOrderItemsForViewOrderItems() {
        Long orderId = 1L;
        OrderEntity order = new OrderEntity();
        order.setOrderItems(Collections.singletonList(new OrderItemEntity()));
        when(orderService.getOrderById(orderId)).thenReturn(order);

        ResponseEntity<Object> responseEntity = orderRestController.viewOrderItems(orderId);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(order.getOrderItems());
        verify(orderService, times(1)).getOrderById(orderId);
    }
}
