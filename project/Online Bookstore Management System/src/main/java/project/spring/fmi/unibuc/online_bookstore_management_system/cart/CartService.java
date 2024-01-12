package project.spring.fmi.unibuc.online_bookstore_management_system.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public CartEntity getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public void addToCart(Long userId, Long bookId, int quantity) {
        // Fetch or create a cart for the user
        CartEntity cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            cart = new CartEntity();
            cart.setUserId(userId);
        }

        // Check if the item exists in the cart, update quantity if yes, otherwise add a new item
        CartItemEntity cartItem = cart.getCartItems().stream()
                .filter(item -> item.getBookId().equals(bookId))
                .findFirst()
                .orElse(new CartItemEntity());

        // Additional null checks if needed

        cartItem.setBookId(bookId);
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setCart(cart);

        cart.getCartItems().add(cartItem);

        cartRepository.save(cart);
    }


    public void updateCartItemQuantity(Long cartId, Long bookId, int newQuantity) {
        // Fetch the cart item and update its quantity
        CartItemEntity cartItem = cartItemRepository.findByCartIdAndBookId(cartId, bookId);
        if (cartItem != null) {
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        }
    }

    public void removeFromCart(Long cartId, Long bookId) {
        cartItemRepository.deleteByCartIdAndBookId(cartId, bookId);
    }

    public CartItemEntity getCartItemByCartIdAndBookId(Long cartId, Long bookId) {
        return cartItemRepository.findByCartIdAndBookId(cartId, bookId);
    }

    public CartEntity getCartByCartId(Long cartId) {
        Optional<CartEntity> optionalCart = cartRepository.findById(cartId);
        return optionalCart.orElse(null);
    }
}

