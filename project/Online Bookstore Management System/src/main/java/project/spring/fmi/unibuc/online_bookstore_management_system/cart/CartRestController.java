package project.spring.fmi.unibuc.online_bookstore_management_system.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookService;
import project.spring.fmi.unibuc.online_bookstore_management_system.cart.CartEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.cart.CartItemEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.cart.CartService;
import project.spring.fmi.unibuc.online_bookstore_management_system.order.OrderItemEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.order.OrderService;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    private final CartService cartService;
    private final BookService bookService;
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public CartRestController(CartService cartService, BookService bookService, OrderService orderService, UserService userService) {
        this.cartService = cartService;
        this.bookService = bookService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/view")
    public ResponseEntity<Object> viewCart(@RequestParam Long userId) {
        UserEntity user = userService.findUserById(userId);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        CartEntity cart = cartService.fetchCart(userId);

        if (cart != null) {
            Integer totalSum = 0;
            for (CartItemEntity item : cart.getCartItems()) {
                BookEntity book = bookService.getBookById(item.getBookId());
                totalSum += book.getPrice() * item.getQuantity();
            }

            return new ResponseEntity<>(new CartViewResponse(cart, totalSum), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cart not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam Long userId, @RequestParam Long bookId, @RequestParam int quantity) {
        cartService.addToCart(userId, bookId, quantity);
        return new ResponseEntity<>("Added to cart", HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateCartItem(@RequestParam Long cartId, @RequestParam Long bookId, @RequestParam int quantity) {
        cartService.updateCartItemQuantity(cartId, bookId, quantity);
        return new ResponseEntity<>("Cart item updated", HttpStatus.OK);
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam Long cartId, @RequestParam Long bookId) {
        CartEntity cart = cartService.getCartByCartId(cartId);
        if (cart != null) {
            cartService.removeFromCart(cartId, bookId);
            return new ResponseEntity<>("Item removed from cart", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cart not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/submitOrder")
    public ResponseEntity<String> submitOrder(@RequestParam Long userId) {
        CartEntity cart = cartService.getCartByUserId(userId);
        if (cart != null && !cart.getCartItems().isEmpty()) {
            List<OrderItemEntity> orderItems = convertCartItemsToOrderItems(cart.getCartItems());
            orderService.submitOrder(userId, orderItems);
            return new ResponseEntity<>("Order submitted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cart is empty", HttpStatus.BAD_REQUEST);
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

    private static class CartViewResponse {
        private final CartEntity cart;
        private final Integer totalPrice;

        public CartViewResponse(CartEntity cart, Integer totalPrice) {
            this.cart = cart;
            this.totalPrice = totalPrice;
        }

        public CartEntity getCart() {
            return cart;
        }

        public Integer getTotalPrice() {
            return totalPrice;
        }
    }
}
