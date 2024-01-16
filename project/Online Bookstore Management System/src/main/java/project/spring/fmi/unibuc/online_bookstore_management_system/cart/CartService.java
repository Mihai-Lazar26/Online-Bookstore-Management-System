package project.spring.fmi.unibuc.online_bookstore_management_system.cart;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserService;

import java.util.List;
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

    public CartEntity fetchCart(Long userId) {
        CartEntity cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            cart = new CartEntity();
            cart.setUserId(userId);
            cartRepository.save(cart);
        }

        return cart;
    }

    public void addToCart(Long userId, Long bookId, int quantity) {
        CartEntity cart = fetchCart(userId);

        CartItemEntity cartItem = cart.getCartItems().stream()
                .filter(item -> item.getBookId().equals(bookId))
                .findFirst()
                .orElse(new CartItemEntity());

        cartItem.setBookId(bookId);
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setCart(cart);

        cart.getCartItems().add(cartItem);

        cartRepository.save(cart);
    }


    public void updateCartItemQuantity(Long cartId, Long bookId, int newQuantity) {
        CartItemEntity cartItem = cartItemRepository.findByCartIdAndBookId(cartId, bookId);
        if (cartItem != null) {
            if (newQuantity > 0) {
                cartItem.setQuantity(newQuantity);
                cartItemRepository.save(cartItem);
            }
            else {
                cartItemRepository.delete(cartItem);
            }
        }
    }

    @Transactional
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

    public void deleteCartItemsByBook(BookEntity book) {
        List<CartItemEntity> cartItems = cartItemRepository.findByBookId(book.getId());
        cartItemRepository.deleteAll(cartItems);
    }

    public void deleteCartAndCartItemsByUser(UserEntity user) {
        CartEntity userCart = cartRepository.findByUserId(user.getUserID());

        if (userCart != null) {
            List<CartItemEntity> cartItems = cartItemRepository.findByCartId(userCart.getId());

            cartItemRepository.deleteAll(cartItems);
            cartRepository.delete(userCart);
        }
    }

    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }
}

