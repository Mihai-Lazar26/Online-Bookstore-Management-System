package project.spring.fmi.unibuc.online_bookstore_management_system.cart;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="carts")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private Long userId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart", orphanRemoval = true)
    private List<CartItemEntity> cartItems = new ArrayList<>();

    public CartEntity() {}

    public CartEntity(Long id, Long userId, List<CartItemEntity> cartItems) {
        this.id = id;
        this.userId = userId;
        this.cartItems = cartItems;
    }

    public CartEntity(Long userId, List<CartItemEntity> cartItems) {
        this.userId = userId;
        this.cartItems = cartItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CartItemEntity> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemEntity> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public String toString() {
        return "CartEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", cartItems=" + cartItems +
                '}';
    }
}
