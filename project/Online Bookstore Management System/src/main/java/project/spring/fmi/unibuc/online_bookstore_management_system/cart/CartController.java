package project.spring.fmi.unibuc.online_bookstore_management_system.cart;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookService;
import project.spring.fmi.unibuc.online_bookstore_management_system.order.OrderItemEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.order.OrderService;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final BookService bookService;
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, BookService bookService, OrderService orderService, UserService userService) {
        this.cartService = cartService;
        this.bookService = bookService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/view")
    public String viewCart(Model model, @RequestParam Long userId) {

        UserEntity user = userService.findUserById(userId);
        if (user == null) {
            return "errorPage";
        }

        CartEntity cart = cartService.fetchCart(userId);

        if (cart != null) {

            Integer totalSum = 0;
            for (CartItemEntity item : cart.getCartItems()){
                BookEntity book = bookService.getBookById(item.getBookId());
                totalSum += book.getPrice() * item.getQuantity();
            }

            model.addAttribute("cart", cart);
            model.addAttribute("books", bookService);
            model.addAttribute("totalPrice", totalSum);
            return "viewCart";
        } else {
            return "errorPage";
        }
    }


    @PostMapping("/add")
    public String addToCart(@RequestParam Long userId, @RequestParam Long bookId, @RequestParam int quantity) {
        cartService.addToCart(userId, bookId, quantity);
        return "redirect:/cart/view?userId=" + userId;
    }

    @PostMapping("/update")
    public String updateCartItem(@RequestParam Long cartId, @RequestParam Long bookId, @RequestParam int quantity) {
        cartService.updateCartItemQuantity(cartId, bookId, quantity);
        CartItemEntity cartItem = cartService.getCartItemByCartIdAndBookId(cartId, bookId);
        if (cartItem != null) {
            return "redirect:/cart/view?userId=" + cartItem.getCart().getUserId();
        } else {
            return "redirect:/cart/view";
        }
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long cartId, @RequestParam Long bookId) {
        CartEntity cart = cartService.getCartByCartId(cartId);
        if (cart != null) {
            cartService.removeFromCart(cartId, bookId);
            return "redirect:/cart/view?userId=" + cart.getUserId();
        } else {
            return "redirect:/cart/view";
        }
    }

    @PostMapping("/submitOrder")
    public String submitOrder(Model model, @RequestParam Long userId) {
        CartEntity cart = cartService.getCartByUserId(userId);
        if (cart != null && !cart.getCartItems().isEmpty()) {
            List<OrderItemEntity> orderItems = convertCartItemsToOrderItems(cart.getCartItems());
            orderService.submitOrder(userId, orderItems);
            return "redirect:/orders/viewUser?userId=" + userId;
        }
        else {
            return "redirect:/cart/view";
        }
    }
    private List<OrderItemEntity> convertCartItemsToOrderItems(List<CartItemEntity> cartItems) {
        return cartItems.stream().map(this::convertCartItemToOrderItem).collect(Collectors.toList());
    }

    private OrderItemEntity convertCartItemToOrderItem(CartItemEntity cartItem) {
        OrderItemEntity orderItem = new OrderItemEntity();
        BookEntity book = bookService.getBookById(cartItem.getBookId());
        orderItem.setBookId(book.getId());
        orderItem.setPrice(book.getPrice());
        orderItem.setQuantity(cartItem.getQuantity());

        return orderItem;
    }
}

