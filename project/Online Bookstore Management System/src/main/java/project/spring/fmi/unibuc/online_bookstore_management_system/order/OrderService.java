package project.spring.fmi.unibuc.online_bookstore_management_system.order;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.spring.fmi.unibuc.online_bookstore_management_system.cart.CartRepository;
import project.spring.fmi.unibuc.online_bookstore_management_system.cart.CartService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
    }

    @Transactional
    public void submitOrder(Long userId, List<OrderItemEntity> orderItems) {
        OrderEntity order = new OrderEntity();
        order.setUserId(userId);
        order.setOrderItems(orderItems);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Pending");

        orderItems.forEach(item -> item.setOrder(order));

        orderRepository.save(order);

        cartService.clearCart(userId);
    }

    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<OrderEntity> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public void updateOrderStatus(Long orderId, String newStatus) {
        // Validate orderId
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }

        // Retrieve the order from the database
        OrderEntity order = getOrderById(orderId);

        // Perform any additional validation or business logic if needed

        // Update the order status
        order.setStatus(newStatus);

        // Save the updated order
        orderRepository.save(order);
    }

    private OrderEntity getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order with ID " + orderId + " not found"));
    }


}
