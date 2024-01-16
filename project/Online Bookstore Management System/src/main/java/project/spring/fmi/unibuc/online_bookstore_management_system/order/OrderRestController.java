package project.spring.fmi.unibuc.online_bookstore_management_system.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookService;
import project.spring.fmi.unibuc.online_bookstore_management_system.order.OrderEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.order.OrderService;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;
    private final BookService bookService;

    @Autowired
    public OrderRestController(OrderService orderService, BookService bookService) {
        this.orderService = orderService;
        this.bookService = bookService;
    }

    @GetMapping("/viewAll")
    public ResponseEntity<Object> viewOrders() {
        if (UserEntity.signedInUser == null || !UserEntity.signedInUser.getAdmin()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        List<OrderEntity> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/viewUser")
    public ResponseEntity<Object> viewUserOrders(@RequestParam Long userId) {
        UserEntity signedUser = UserEntity.signedInUser;
        if (signedUser == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        if (signedUser.getUserID().equals(userId) || signedUser.getAdmin()) {
            List<OrderEntity> orders = orderService.getOrdersByUserId(userId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }

        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestParam("orderId") Long orderId, @RequestParam("status") String status) {
        orderService.updateOrderStatus(orderId, status);
        return new ResponseEntity<>("Order status updated", HttpStatus.OK);
    }

    @GetMapping("/viewOrderItems")
    public ResponseEntity<Object> viewOrderItems(@RequestParam("orderId") Long orderId) {
        OrderEntity order = orderService.getOrderById(orderId);
        if (order != null) {
            return new ResponseEntity<>(order.getOrderItems(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
    }
}
