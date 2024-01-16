package project.spring.fmi.unibuc.online_bookstore_management_system.order;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.spring.fmi.unibuc.online_bookstore_management_system.cart.CartService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void submitOrder_ValidData_CreatesOrderAndClearsCart() {
        Long userId = 1L;
        List<OrderItemEntity> orderItems = new ArrayList<>();
        orderItems.add(new OrderItemEntity());
        orderItems.add(new OrderItemEntity());

        orderService.submitOrder(userId, orderItems);

        verify(orderRepository, times(1)).save(any(OrderEntity.class));
        verify(cartService, times(1)).clearCart(userId);
    }

    @Test
    void getAllOrders_ReturnsListOfOrders() {
        List<OrderEntity> mockOrders = new ArrayList<>();
        when(orderRepository.findAll()).thenReturn(mockOrders);

        List<OrderEntity> result = orderService.getAllOrders();

        assertEquals(mockOrders, result);
    }

    @Test
    void getOrdersByUserId_ReturnsListOfOrders() {
        Long userId = 1L;
        List<OrderEntity> mockOrders = new ArrayList<>();
        when(orderRepository.findByUserId(userId)).thenReturn(mockOrders);

        List<OrderEntity> result = orderService.getOrdersByUserId(userId);

        assertEquals(mockOrders, result);
    }

    @Test
    void updateOrderStatus_ValidOrderIdAndStatus_UpdatesOrderStatus() {
        Long orderId = 1L;
        String newStatus = "Shipped";
        OrderEntity mockOrder = new OrderEntity();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        orderService.updateOrderStatus(orderId, newStatus);

        assertEquals(newStatus, mockOrder.getStatus());
        verify(orderRepository, times(1)).save(mockOrder);
    }

    @Test
    void updateOrderStatus_InvalidOrderId_ThrowsException() {
        Long invalidOrderId = null;

        assertThrows(IllegalArgumentException.class, () -> orderService.updateOrderStatus(invalidOrderId, "Shipped"));
    }

    @Test
    void getOrderById_ValidOrderId_ReturnsOrder() {
        Long orderId = 1L;
        OrderEntity mockOrder = new OrderEntity();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        OrderEntity result = orderService.getOrderById(orderId);

        assertEquals(mockOrder, result);
    }

    @Test
    void getOrderById_InvalidOrderId_ThrowsException() {
        Long invalidOrderId = 2L;
        when(orderRepository.findById(invalidOrderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.getOrderById(invalidOrderId));
    }

    @Test
    void existsOrderByUserIdAndBookId_OrderDoesNotExist_ReturnsFalse() {
        Long userId = 1L;
        Long bookId = 2L;

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserId(userId);

        Mockito.when(orderRepository.findByUserId(userId)).thenReturn(new ArrayList<>());

        boolean result = orderService.existsOrderByUserIdAndBookId(userId, bookId);

        assertFalse(result);
    }

    @Test
    void existsOrderByUserIdAndBookId_OrderExists_ReturnsTrue() {
        Long userId = 1L;
        Long bookId = 2L;

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserId(userId);

        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setBookId(bookId);

        List<OrderItemEntity> orderItems = new ArrayList<>();
        orderItems.add(orderItemEntity);

        orderEntity.setOrderItems(orderItems);

        Mockito.when(orderRepository.findByUserId(userId)).thenReturn(List.of(orderEntity));

        boolean result = orderService.existsOrderByUserIdAndBookId(userId, bookId);

        assertTrue(result);
    }
}
