package project.spring.fmi.unibuc.online_bookstore_management_system.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookService;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final BookService bookService;

    @Autowired
    public OrderController(OrderService orderService, BookService bookService) {
        this.orderService = orderService;
        this.bookService = bookService;
    }

    @GetMapping("/viewAll")
    public String viewOrders(Model model) {
        if (UserEntity.signedInUser == null || !UserEntity.signedInUser.getAdmin()) {
            return "redirect:/";
        }
        List<OrderEntity> orders = orderService.getAllOrders();

        model.addAttribute("orders", orders);
        model.addAttribute("isAdmin", UserEntity.signedInUser.getAdmin());
        return "viewOrders";
    }

    @GetMapping("/viewUser")
    public String viewUserOrders(Model model, @RequestParam Long userId) {
        UserEntity signedUser = UserEntity.signedInUser;
        if (signedUser == null){
            return "redirect:/";
        }

        if (signedUser.getUserID().equals(userId) || signedUser.getAdmin()) {
            List<OrderEntity> orders = orderService.getOrdersByUserId(userId);
            model.addAttribute("orders", orders);
            model.addAttribute("isAdmin", UserEntity.signedInUser.getAdmin());
            return "viewOrders";
        }

        return "redirect:/";
    }

    @PostMapping("/updateStatus")
    public String updateStatus(@RequestParam("orderId") Long orderId, @RequestParam("status") String status) {
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/orders/viewAll";
    }

    @GetMapping("/viewOrderItems")
    public String viewOrderItems(Model model, @RequestParam("orderId") Long orderId) {
        OrderEntity order = orderService.getOrderById(orderId);
        model.addAttribute("orderItems", order.getOrderItems());
        model.addAttribute("booksService", bookService);
        model.addAttribute("userId", UserEntity.signedInUser.getUserID());
        return "viewOrderItemsPage";
    }
}
