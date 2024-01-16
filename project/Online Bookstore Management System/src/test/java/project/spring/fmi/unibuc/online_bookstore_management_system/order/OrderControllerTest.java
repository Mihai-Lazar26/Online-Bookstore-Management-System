package project.spring.fmi.unibuc.online_bookstore_management_system.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookService;
import project.spring.fmi.unibuc.online_bookstore_management_system.order.*;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private OrderController orderController;

    @Test
    void viewOrders_AdminUser_ReturnsViewOrdersPage() {
        UserEntity adminUser = new UserEntity();
        adminUser.setAdmin(true);
        UserEntity.signedInUser = adminUser;

        List<OrderEntity> orders = new ArrayList<>();
        when(orderService.getAllOrders()).thenReturn(orders);

        Model model = Mockito.mock(Model.class);

        String viewName = orderController.viewOrders(model);

        assertEquals("viewOrders", viewName);
        verify(model).addAttribute("orders", orders);
        verify(model).addAttribute("isAdmin", true);
    }

    @Test
    void viewUserOrders_ValidUser_ReturnsViewOrdersPage() {
        UserEntity signedUser = new UserEntity();
        signedUser.setUserID(1L);
        UserEntity.signedInUser = signedUser;

        List<OrderEntity> orders = new ArrayList<>();
        when(orderService.getOrdersByUserId(anyLong())).thenReturn(orders);

        Model model = Mockito.mock(Model.class);

        String viewName = orderController.viewUserOrders(model, 1L);

        assertEquals("viewOrders", viewName);
        verify(model).addAttribute("orders", orders);
        verify(model).addAttribute("isAdmin", false);
    }

    @Test
    void updateStatus_ValidOrder_ReturnsRedirectToViewAll() {
        Long orderId = 1L;
        String newStatus = "Shipped";

        String redirectToViewAll = orderController.updateStatus(orderId, newStatus);

        assertEquals("redirect:/orders/viewAll", redirectToViewAll);
        verify(orderService).updateOrderStatus(orderId, newStatus);
    }

    @Test
    void viewOrderItems_ValidOrderId_ReturnsViewOrderItemsPage() {
        Long orderId = 1L;
        OrderEntity order = new OrderEntity();
        List<OrderItemEntity> orderItems = new ArrayList<>();
        order.setOrderItems(orderItems);

        when(orderService.getOrderById(orderId)).thenReturn(order);

        Model model = Mockito.mock(Model.class);

        String viewName = orderController.viewOrderItems(model, orderId);

        assertEquals("viewOrderItemsPage", viewName);
        verify(model).addAttribute("orderItems", orderItems);
        verify(model).addAttribute("booksService", bookService);
        verify(model).addAttribute("userId", UserEntity.signedInUser.getUserID());
    }
}
