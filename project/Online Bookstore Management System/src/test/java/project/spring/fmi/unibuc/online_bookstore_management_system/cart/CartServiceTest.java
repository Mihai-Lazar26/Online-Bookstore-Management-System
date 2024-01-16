package project.spring.fmi.unibuc.online_bookstore_management_system.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;


    @Test
    void testGetCartByUserId() {
        Long userId = 1L;
        CartEntity expectedCart = new CartEntity();
        when(cartRepository.findByUserId(userId)).thenReturn(expectedCart);

        CartEntity result = cartService.getCartByUserId(userId);

        assertEquals(expectedCart, result);
    }

    @Test
    void testFetchCartExistingCart() {
        Long userId = 1L;
        CartEntity existingCart = new CartEntity();
        when(cartRepository.findByUserId(userId)).thenReturn(existingCart);

        CartEntity result = cartService.fetchCart(userId);

        assertEquals(existingCart, result);
        verify(cartRepository, never()).save(any(CartEntity.class));
    }

    @Test
    void testFetchCartNewCart() {
        Long userId = 1L;
        when(cartRepository.findByUserId(userId)).thenReturn(null);

        CartEntity result = cartService.fetchCart(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(cartRepository, times(1)).save(any(CartEntity.class));
    }

    @Test
    void testAddToCartExistingCartItem() {
        Long userId = 1L;
        Long bookId = 2L;
        int quantity = 3;
        CartEntity existingCart = new CartEntity();
        CartItemEntity existingCartItem = new CartItemEntity();
        existingCartItem.setBookId(bookId);
        existingCartItem.setQuantity(1);
        existingCart.getCartItems().add(existingCartItem);
        when(cartRepository.findByUserId(userId)).thenReturn(existingCart);
        when(cartItemRepository.findByCartIdAndBookId(anyLong(), anyLong())).thenReturn(existingCartItem);

        cartService.addToCart(userId, bookId, quantity);

        assertEquals(4, existingCartItem.getQuantity());
        verify(cartRepository, times(1)).save(existingCart);
    }

    @Test
    void testAddToCartNewCartItem() {
        Long userId = 1L;
        Long bookId = 2L;
        int quantity = 3;
        CartEntity existingCart = new CartEntity();
        when(cartRepository.findByUserId(userId)).thenReturn(existingCart);
        when(cartItemRepository.findByCartIdAndBookId(anyLong(), anyLong())).thenReturn(null);

        cartService.addToCart(userId, bookId, quantity);

        assertEquals(1, existingCart.getCartItems().size());
        assertEquals(bookId, existingCart.getCartItems().get(0).getBookId());
        assertEquals(quantity, existingCart.getCartItems().get(0).getQuantity());
        verify(cartRepository, times(1)).save(existingCart);
    }

    @Test
    void testUpdateCartItemQuantityValid() {
        Long cartId = 1L;
        Long bookId = 2L;
        int newQuantity = 5;
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setId(cartId);
        cartItem.setBookId(bookId);
        cartItem.setQuantity(3);
        when(cartItemRepository.findByCartIdAndBookId(cartId, bookId)).thenReturn(cartItem);

        cartService.updateCartItemQuantity(cartId, bookId, newQuantity);

        assertEquals(newQuantity, cartItem.getQuantity());
        verify(cartItemRepository, times(1)).save(cartItem);
    }

    @Test
    void testUpdateCartItemQuantityInvalid() {
        Long cartId = 1L;
        Long bookId = 2L;
        int newQuantity = 0;
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setId(cartId);
        cartItem.setBookId(bookId);
        cartItem.setQuantity(3);
        when(cartItemRepository.findByCartIdAndBookId(cartId, bookId)).thenReturn(cartItem);

        cartService.updateCartItemQuantity(cartId, bookId, newQuantity);

        assertEquals(3, cartItem.getQuantity());
        verify(cartItemRepository, never()).save(cartItem);
    }


    @Test
    void testGetCartItemByCartIdAndBookId() {
        Long cartId = 1L;
        Long bookId = 2L;
        CartItemEntity expectedCartItem = new CartItemEntity();
        when(cartItemRepository.findByCartIdAndBookId(cartId, bookId)).thenReturn(expectedCartItem);

        CartItemEntity result = cartService.getCartItemByCartIdAndBookId(cartId, bookId);

        assertEquals(expectedCartItem, result);
    }

    @Test
    void testGetCartByCartId() {
        Long cartId = 1L;
        CartEntity expectedCart = new CartEntity();
        Optional<CartEntity> optionalCart = Optional.of(expectedCart);
        when(cartRepository.findById(cartId)).thenReturn(optionalCart);

        CartEntity result = cartService.getCartByCartId(cartId);

        assertEquals(expectedCart, result);
    }

    @Test
    void testGetCartByCartIdNotFound() {
        Long cartId = 1L;
        Optional<CartEntity> optionalCart = Optional.empty();
        when(cartRepository.findById(cartId)).thenReturn(optionalCart);

        CartEntity result = cartService.getCartByCartId(cartId);

        assertNull(result);
    }

    @Test
    void testDeleteCartItemsByBook() {
        Long bookId = 1L;
        List<CartItemEntity> cartItems = List.of(new CartItemEntity(), new CartItemEntity());
        when(cartItemRepository.findByBookId(bookId)).thenReturn(cartItems);

        cartService.deleteCartItemsByBook(new BookEntity(bookId, "Test Book", "Test Author", 20));

        verify(cartItemRepository, times(1)).deleteAll(cartItems);
    }

    @Test
    void testDeleteCartAndCartItemsByUser() {
        Long userId = 1L;
        CartEntity userCart = new CartEntity();
        userCart.setId(10L);
        List<CartItemEntity> cartItems = List.of(new CartItemEntity(), new CartItemEntity());
        when(cartRepository.findByUserId(userId)).thenReturn(userCart);
        when(cartItemRepository.findByCartId(userCart.getId())).thenReturn(cartItems);

        cartService.deleteCartAndCartItemsByUser(new UserEntity(userId, "TestUser", "password", "email"));

        verify(cartItemRepository, times(1)).deleteAll(cartItems);
        verify(cartRepository, times(1)).delete(userCart);
    }

    @Test
    void testClearCart() {
        Long userId = 1L;

        cartService.clearCart(userId);

        verify(cartRepository, times(1)).deleteByUserId(userId);
    }

}
