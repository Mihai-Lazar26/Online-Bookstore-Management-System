package project.spring.fmi.unibuc.online_bookstore_management_system.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookService;
import project.spring.fmi.unibuc.online_bookstore_management_system.order.OrderService;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



//@WebMvcTest(controllers = CartRestController.class)
//@ExtendWith(MockitoExtension.class)
class CartRestControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private BookService bookService;

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartRestController cartRestController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartRestController).build();
    }

    @Test
    void shouldReturnCartView() throws Exception {
        UserEntity user = new UserEntity();
        CartEntity cart = new CartEntity();
        when(userService.findUserById(anyLong())).thenReturn(user);
        when(cartService.fetchCart(anyLong())).thenReturn(cart);
        when(bookService.getBookById(anyLong())).thenReturn(new BookEntity());

        ResultActions result = mockMvc.perform(get("/api/cart/view")
                .param("userId", "1"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.cart").exists())
                .andExpect(jsonPath("$.totalPrice").exists());
        verify(userService, times(1)).findUserById(anyLong());
        verify(cartService, times(1)).fetchCart(anyLong());
        verify(bookService, times(cart.getCartItems().size())).getBookById(anyLong());
    }

    @Test
    void shouldReturnNotFoundForViewCartWhenUserNotFound() throws Exception {
        when(userService.findUserById(anyLong())).thenReturn(null);

        ResultActions result = mockMvc.perform(get("/api/cart/view")
                .param("userId", "1"));

        result.andExpect(status().isNotFound());
        verify(userService, times(1)).findUserById(anyLong());
        verifyNoInteractions(cartService);
    }

    @Test
    void shouldReturnAddedToCart() throws Exception {
        when(bookService.getBookById(anyLong())).thenReturn(new BookEntity());

        ResultActions result = mockMvc.perform(post("/api/cart/add")
                .param("userId", "1")
                .param("bookId", "2")
                .param("quantity", "3"));

        result.andExpect(status().isCreated())
                .andExpect(content().string("Added to cart"));
        verify(cartService, times(1)).addToCart(anyLong(), anyLong(), anyInt());
    }

    @Test
    void shouldReturnCartUpdated() throws Exception {
        long cartId = 1L;
        long bookId = 2L;
        int quantity = 3;

        when(cartService.getCartByCartId(cartId)).thenReturn(new CartEntity());

        mockMvc.perform(post("/api/cart/update")
                        .param("cartId", String.valueOf(cartId))
                        .param("bookId", String.valueOf(bookId))
                        .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isOk());

        verify(cartService).updateCartItemQuantity(cartId, bookId, quantity);

        reset(cartService);
    }

    @Test
    void shouldReturnRemovedFromCart() throws Exception {
        when(cartService.getCartByCartId(anyLong())).thenReturn(new CartEntity());

        ResultActions result = mockMvc.perform(post("/api/cart/remove")
                .param("cartId", "1")
                .param("bookId", "2"));

        result.andExpect(status().isOk())
                .andExpect(content().string("Item removed from cart"));
        verify(cartService, times(1)).getCartByCartId(anyLong());
        verify(cartService, times(1)).removeFromCart(anyLong(), anyLong());
    }

    @Test
    void shouldReturnNotFoundForRemoveFromCartWhenCartNotFound() throws Exception {
        long cartId = 1L;
        long bookId = 2L;

        when(cartService.getCartByCartId(cartId)).thenReturn(null);

        mockMvc.perform(post("/api/cart/remove")
                        .param("cartId", String.valueOf(cartId))
                        .param("bookId", String.valueOf(bookId)))
                .andExpect(status().isNotFound());

        verify(cartService, never()).removeFromCart(anyLong(), anyLong());

        reset(cartService);
    }

    @Test
    void shouldReturnOrderSubmitted() throws Exception {
        CartEntity cart = new CartEntity();
        cart.setId(1L);
        List<CartItemEntity> cartItemEntityList = new ArrayList<>();
        cartItemEntityList.add(new CartItemEntity(1L, cart, 1L, 1));
        cart.setCartItems(cartItemEntityList);

        BookEntity book = new BookEntity();
        book.setId(1L);
        when(cartService.getCartByUserId(anyLong())).thenReturn(cart);
        when(bookService.getBookById(anyLong())).thenReturn(book);

        ResultActions result = mockMvc.perform(post("/api/cart/submitOrder")
                .param("userId", "1"));

        result.andExpect(status().isOk())
                .andExpect(content().string("Order submitted"));
        verify(cartService, times(1)).getCartByUserId(anyLong());
        verify(orderService, times(1)).submitOrder(anyLong(), anyList());
    }

    @Test
    void shouldReturnBadRequestForSubmitOrderWhenCartEmpty() throws Exception {
        CartEntity cart = new CartEntity();
        when(cartService.getCartByUserId(anyLong())).thenReturn(cart);

        ResultActions result = mockMvc.perform(post("/api/cart/submitOrder")
                .param("userId", "1"));

        result.andExpect(status().isBadRequest())
                .andExpect(content().string("Cart is empty"));
        verify(cartService, times(1)).getCartByUserId(anyLong());
        verifyNoInteractions(orderService);
    }
}
