package project.spring.fmi.unibuc.online_bookstore_management_system.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository  extends JpaRepository<OrderEntity, Long> {
}
