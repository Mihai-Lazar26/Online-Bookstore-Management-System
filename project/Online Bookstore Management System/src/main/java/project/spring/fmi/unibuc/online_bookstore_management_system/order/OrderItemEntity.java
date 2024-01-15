package project.spring.fmi.unibuc.online_bookstore_management_system.order;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "order_items")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;
    @NonNull
    private Long bookId;
    @NonNull
    private Integer price;
    @NonNull
    private Integer quantity;

    public OrderItemEntity() {}

    public OrderItemEntity(Long id, OrderEntity order, Long bookId, Integer price, Integer quantity) {
        this.id = id;
        this.order = order;
        this.bookId = bookId;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderItemEntity(OrderEntity order, Long bookId, Integer price, Integer quantity) {
        this.order = order;
        this.bookId = bookId;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderItemEntity{" +
                "id=" + id +
                ", order=" + order +
                ", bookId=" + bookId +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
