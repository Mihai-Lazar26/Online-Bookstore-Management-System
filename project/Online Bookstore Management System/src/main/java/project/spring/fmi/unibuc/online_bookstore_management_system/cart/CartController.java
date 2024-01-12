package project.spring.fmi.unibuc.online_bookstore_management_system.cart;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookService;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final BookService bookService;

    @Autowired
    public CartController(CartService cartService, BookService bookService) {
        this.cartService = cartService;
        this.bookService = bookService;
    }

    @GetMapping("/view")
    public String viewCart(Model model, @RequestParam Long userId) {
        CartEntity cart = cartService.getCartByUserId(userId);
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
        return "redirect:/cart/view?userId=" + userId; // Redirect to view the updated cart
    }

    @PostMapping("/update")
    public String updateCartItem(@RequestParam Long cartId, @RequestParam Long bookId, @RequestParam int quantity) {
        cartService.updateCartItemQuantity(cartId, bookId, quantity);
        // Redirect to view cart after updating quantity
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
            return "redirect:/cart/view?userId=" + cart.getUserId(); // Redirect to view cart after removing item
        } else {
            return "redirect:/cart/view";
        }
    }
}

