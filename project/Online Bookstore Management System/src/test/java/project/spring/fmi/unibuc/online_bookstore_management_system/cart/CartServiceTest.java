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

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    void testGetCartByUserId() {
        // Arrange
        Long userId = 1L;
        CartEntity expectedCart = new CartEntity();
        when(cartRepository.findByUserId(userId)).thenReturn(expectedCart);

        // Act
        CartEntity result = cartService.getCartByUserId(userId);

        // Assert
        assertEquals(expectedCart, result);
    }

    @Test
    void testFetchCartExistingCart() {
        // Arrange
        Long userId = 1L;
        CartEntity existingCart = new CartEntity();
        when(cartRepository.findByUserId(userId)).thenReturn(existingCart);

        // Act
        CartEntity result = cartService.fetchCart(userId);

        // Assert
        assertEquals(existingCart, result);
        verify(cartRepository, never()).save(any(CartEntity.class));
    }

    @Test
    void testFetchCartNewCart() {
        // Arrange
        Long userId = 1L;
        when(cartRepository.findByUserId(userId)).thenReturn(null);

        // Act
        CartEntity result = cartService.fetchCart(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(cartRepository, times(1)).save(any(CartEntity.class));
    }

    @Test
    void testAddToCartExistingCartItem() {
        // Arrange
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

        // Act
        cartService.addToCart(userId, bookId, quantity);

        // Assert
        assertEquals(4, existingCartItem.getQuantity()); // Existing quantity + new quantity
        verify(cartRepository, times(1)).save(existingCart);
    }

    @Test
    void testAddToCartNewCartItem() {
        // Arrange
        Long userId = 1L;
        Long bookId = 2L;
        int quantity = 3;
        CartEntity existingCart = new CartEntity();
        when(cartRepository.findByUserId(userId)).thenReturn(existingCart);
        when(cartItemRepository.findByCartIdAndBookId(anyLong(), anyLong())).thenReturn(null);

        // Act
        cartService.addToCart(userId, bookId, quantity);

        // Assert
        assertEquals(1, existingCart.getCartItems().size());
        assertEquals(bookId, existingCart.getCartItems().get(0).getBookId());
        assertEquals(quantity, existingCart.getCartItems().get(0).getQuantity());
        verify(cartRepository, times(1)).save(existingCart);
    }

    @Test
    void testUpdateCartItemQuantityValid() {
        // Arrange
        Long cartId = 1L;
        Long bookId = 2L;
        int newQuantity = 5;
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setId(cartId);
        cartItem.setBookId(bookId);
        cartItem.setQuantity(3);
        when(cartItemRepository.findByCartIdAndBookId(cartId, bookId)).thenReturn(cartItem);

        // Act
        cartService.updateCartItemQuantity(cartId, bookId, newQuantity);

        // Assert
        assertEquals(newQuantity, cartItem.getQuantity());
        verify(cartItemRepository, times(1)).save(cartItem);
    }

    @Test
    void testUpdateCartItemQuantityInvalid() {
        // Arrange
        Long cartId = 1L;
        Long bookId = 2L;
        int newQuantity = 0;
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setId(cartId);
        cartItem.setBookId(bookId);
        cartItem.setQuantity(3);
        when(cartItemRepository.findByCartIdAndBookId(cartId, bookId)).thenReturn(cartItem);

        // Act
        cartService.updateCartItemQuantity(cartId, bookId, newQuantity);

        // Assert
        assertEquals(3, cartItem.getQuantity()); // Quantity should remain unchanged
        verify(cartItemRepository, never()).save(cartItem);
    }


    @Test
    void testGetCartItemByCartIdAndBookId() {
        // Arrange
        Long cartId = 1L;
        Long bookId = 2L;
        CartItemEntity expectedCartItem = new CartItemEntity();
        when(cartItemRepository.findByCartIdAndBookId(cartId, bookId)).thenReturn(expectedCartItem);

        // Act
        CartItemEntity result = cartService.getCartItemByCartIdAndBookId(cartId, bookId);

        // Assert
        assertEquals(expectedCartItem, result);
    }

    @Test
    void testGetCartByCartId() {
        // Arrange
        Long cartId = 1L;
        CartEntity expectedCart = new CartEntity();
        Optional<CartEntity> optionalCart = Optional.of(expectedCart);
        when(cartRepository.findById(cartId)).thenReturn(optionalCart);

        // Act
        CartEntity result = cartService.getCartByCartId(cartId);

        // Assert
        assertEquals(expectedCart, result);
    }

    @Test
    void testGetCartByCartIdNotFound() {
        // Arrange
        Long cartId = 1L;
        Optional<CartEntity> optionalCart = Optional.empty();
        when(cartRepository.findById(cartId)).thenReturn(optionalCart);

        // Act
        CartEntity result = cartService.getCartByCartId(cartId);

        // Assert
        assertNull(result);
    }

    @Test
    void testDeleteCartItemsByBook() {
        // Arrange
        Long bookId = 1L;
        List<CartItemEntity> cartItems = List.of(new CartItemEntity(), new CartItemEntity());
        when(cartItemRepository.findByBookId(bookId)).thenReturn(cartItems);

        // Act
        cartService.deleteCartItemsByBook(new BookEntity(bookId, "Test Book", "Test Author", 20));

        // Assert
        verify(cartItemRepository, times(1)).deleteAll(cartItems);
    }

    @Test
    void testDeleteCartAndCartItemsByUser() {
        // Arrange
        Long userId = 1L;
        CartEntity userCart = new CartEntity();
        userCart.setId(10L);
        List<CartItemEntity> cartItems = List.of(new CartItemEntity(), new CartItemEntity());
        when(cartRepository.findByUserId(userId)).thenReturn(userCart);
        when(cartItemRepository.findByCartId(userCart.getId())).thenReturn(cartItems);

        // Act
        cartService.deleteCartAndCartItemsByUser(new UserEntity(userId, "TestUser", "password", "email"));

        // Assert
        verify(cartItemRepository, times(1)).deleteAll(cartItems);
        verify(cartRepository, times(1)).delete(userCart);
    }

    @Test
    void testClearCart() {
        // Arrange
        Long userId = 1L;

        // Act
        cartService.clearCart(userId);

        // Assert
        verify(cartRepository, times(1)).deleteByUserId(userId);
    }

    // Add more tests as needed for other methods

    // ...

}
