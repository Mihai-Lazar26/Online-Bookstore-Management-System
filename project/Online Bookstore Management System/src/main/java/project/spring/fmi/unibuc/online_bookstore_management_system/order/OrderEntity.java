package project.spring.fmi.unibuc.online_bookstore_management_system.order;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private Long userId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderItemEntity> orderItems;
    @NonNull
    private LocalDateTime orderDate;
    @NonNull
    private String status;

    public OrderEntity() {}

    public OrderEntity(Long id, Long userId, List<OrderItemEntity> orderItems, LocalDateTime orderDate, String status) {
        this.id = id;
        this.userId = userId;
        this.orderItems = orderItems;
        this.orderDate = orderDate;
        this.status = status;
    }

    public OrderEntity(Long userId, List<OrderItemEntity> orderItems, LocalDateTime orderDate, String status) {
        this.userId = userId;
        this.orderItems = orderItems;
        this.orderDate = orderDate;
        this.status = status;
    }

    public Integer getTotalPrice() {
        Integer sum = 0;
        for (OrderItemEntity item : this.orderItems) {
            sum += item.getPrice() * item.getQuantity();
        }

        return sum;
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

    public List<OrderItemEntity> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemEntity> orderItems) {
        this.orderItems = orderItems;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderItems=" + orderItems +
                ", orderDate=" + orderDate +
                ", status='" + status + '\'' +
                '}';
    }
}
