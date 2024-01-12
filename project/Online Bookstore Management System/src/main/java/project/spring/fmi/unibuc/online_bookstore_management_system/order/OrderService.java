package project.spring.fmi.unibuc.online_bookstore_management_system.order;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.spring.fmi.unibuc.online_bookstore_management_system.cart.CartRepository;
import project.spring.fmi.unibuc.online_bookstore_management_system.cart.CartService;

import java.time.LocalDateTime;
import java.util.List;

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

        for (OrderItemEntity item : orderItems) {
            item.setOrder(order);
        }

        orderRepository.save(order);

        cartService.clearCart(userId);
    }
}
