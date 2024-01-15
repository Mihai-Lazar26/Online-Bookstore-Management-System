package project.spring.fmi.unibuc.online_bookstore_management_system.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.ui.Model;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookService;
import project.spring.fmi.unibuc.online_bookstore_management_system.order.OrderItemEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.order.OrderService;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private BookService bookService;

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartController cartController;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    void testViewCart() {
        // Arrange
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setUserID(userId);

        CartEntity cart = new CartEntity();
        cart.setUserId(userId);

        when(userService.findUserById(userId)).thenReturn(user);
        when(cartService.fetchCart(userId)).thenReturn(cart);

        // Act
        String result = cartController.viewCart(Mockito.mock(Model.class), userId);

        // Assert
        assertEquals("viewCart", result);
    }

    @Test
    void testViewCartUserNotFound() {
        // Arrange
        Long userId = 1L;

        when(userService.findUserById(userId)).thenReturn(null);

        // Act
        String result = cartController.viewCart(Mockito.mock(Model.class), userId);

        // Assert
        assertEquals("errorPage", result);
    }

    @Test
    void testViewCartEmptyCart() {
        // Arrange
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setUserID(userId);

        when(userService.findUserById(userId)).thenReturn(user);
        when(cartService.fetchCart(userId)).thenReturn(null);

        // Act
        String result = cartController.viewCart(Mockito.mock(Model.class), userId);

        // Assert
        assertEquals("errorPage", result);
    }

    @Test
    void testAddToCart() {
        // Arrange
        Long userId = 1L;
        Long bookId = 2L;
        int quantity = 3;

        // Act
        String result = cartController.addToCart(userId, bookId, quantity);

        // Assert
        assertEquals("redirect:/cart/view?userId=" + userId, result);
        verify(cartService, Mockito.times(1)).addToCart(userId, bookId, quantity);
    }

    @Test
    void testUpdateCartItem() {
        // Arrange
        Long cartId = 1L;
        Long bookId = 2L;
        int quantity = 3;

        // Create a mock CartEntity
        CartEntity mockCart = Mockito.mock(CartEntity.class);
        Mockito.when(mockCart.getUserId()).thenReturn(123L); // Set the user ID as needed

        // Create a mock CartItemEntity with a non-null cart property
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setId(cartId);
        cartItem.setBookId(bookId);
        cartItem.setCart(mockCart); // Set the non-null cart property

        Mockito.when(cartService.getCartItemByCartIdAndBookId(cartId, bookId)).thenReturn(cartItem);

        // Act
        String result = cartController.updateCartItem(cartId, bookId, quantity);

        // Assert
        assertEquals("redirect:/cart/view?userId=" + mockCart.getUserId(), result);
        verify(cartService, Mockito.times(1)).updateCartItemQuantity(cartId, bookId, quantity);
    }


    @Test
    void testUpdateCartItemCartItemNotFound() {
        // Arrange
        Long cartId = 1L;
        Long bookId = 2L;
        int quantity = 3;

        // Act
        String result = cartController.updateCartItem(cartId, bookId, quantity);

        // Assert
        Mockito.when(cartService.getCartItemByCartIdAndBookId(cartId, bookId)).thenReturn(null);

        assertEquals("redirect:/cart/view", result);
    }

    @Test
    void testRemoveFromCart() {
        // Arrange
        Long cartId = 1L;
        Long bookId = 2L;
        CartEntity cart = new CartEntity();
        cart.setUserId(3L);

        Mockito.when(cartService.getCartByCartId(cartId)).thenReturn(cart);
        // Act
        String result = cartController.removeFromCart(cartId, bookId);




        assertEquals("redirect:/cart/view?userId=" + cart.getUserId(), result);
        verify(cartService, Mockito.times(1)).removeFromCart(cartId, bookId);
    }



    @Test
    void testRemoveFromCartCartNotFound() {
        // Arrange
        Long cartId = 1L;
        Long bookId = 2L;

        // Act
        String result = cartController.removeFromCart(cartId, bookId);

        // Assert
        Mockito.when(cartService.getCartByCartId(cartId)).thenReturn(null);

        assertEquals("redirect:/cart/view", result);
    }

    @Captor
    private ArgumentCaptor<List<OrderItemEntity>> orderItemsCaptor;

    @Test
    void submitOrder_validCart_redirectToOrdersView() {
        // Arrange
        long userId = 1L;
        CartEntity cart = new CartEntity();
        cart.setUserId(userId);
        List<CartItemEntity> cartItems = new ArrayList<>();
        cartItems.add(new CartItemEntity(1L, cart, 1L, 1));
        cart.setCartItems(cartItems);
        when(cartService.getCartByUserId(userId)).thenReturn(cart);
        doNothing().when(orderService).submitOrder(anyLong(), anyList());

        BookEntity book = new BookEntity(1L, "test", "test", 10);

        when(bookService.getBookById(anyLong())).thenReturn(book);

        // Act
        String result = cartController.submitOrder(mock(Model.class), userId);

        // Assert
        assertEquals("redirect:/orders/viewUser?userId=" + userId, result);
        verify(cartService, times(1)).getCartByUserId(userId);
    }

    @Test
    void submitOrder_emptyCart_redirectToCartView() {
        // Arrange
        long userId = 1L;
        when(cartService.getCartByUserId(userId)).thenReturn(null);

        // Act
        String result = cartController.submitOrder(mock(Model.class), userId);

        // Assert
        assertEquals("redirect:/cart/view", result);
        verify(cartService, times(1)).getCartByUserId(userId);
        verify(orderService, never()).submitOrder(anyLong(), anyList());
        verify(cartService, never()).clearCart(anyLong());
    }

}
