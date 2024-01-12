package project.spring.fmi.unibuc.online_bookstore_management_system.cart;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/view")
    public String viewCart(Model model, @RequestParam Long userId) {
        CartEntity cart = cartService.getCartByUserId(userId);
        if (cart != null) {
            model.addAttribute("cart", cart);
            return "viewCart"; // Thymeleaf view to display the cart contents
        } else {
            // Handle the case where the cart is not found for the given user
            return "errorPage"; // Redirect to an error page or handle appropriately
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

