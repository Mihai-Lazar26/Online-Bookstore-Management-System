package project.spring.fmi.unibuc.online_bookstore_management_system.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    CartEntity findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
