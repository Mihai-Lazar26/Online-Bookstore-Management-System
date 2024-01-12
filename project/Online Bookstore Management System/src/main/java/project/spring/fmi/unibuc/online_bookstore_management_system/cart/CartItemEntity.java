package project.spring.fmi.unibuc.online_bookstore_management_system.cart;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    private Long bookId;
    private int quantity;

    public CartItemEntity() {}

    public CartItemEntity(Long id, CartEntity cart, Long bookId, int quantity) {
        this.id = id;
        this.cart = cart;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public CartItemEntity(CartEntity cart, Long bookId, int quantity) {
        this.cart = cart;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CartEntity getCart() {
        return cart;
    }

    public void setCart(CartEntity cart) {
        this.cart = cart;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItemEntity{" +
                "id=" + id +
                ", cart=" + cart +
                ", bookId=" + bookId +
                ", quantity=" + quantity +
                '}';
    }
}

