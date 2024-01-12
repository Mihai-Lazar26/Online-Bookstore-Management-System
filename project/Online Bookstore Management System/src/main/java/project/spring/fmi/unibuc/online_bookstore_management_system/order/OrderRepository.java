package project.spring.fmi.unibuc.online_bookstore_management_system.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository  extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserId(Long userId);
}
