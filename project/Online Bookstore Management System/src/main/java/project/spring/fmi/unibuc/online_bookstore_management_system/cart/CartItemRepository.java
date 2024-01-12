package project.spring.fmi.unibuc.online_bookstore_management_system.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookEntity;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    List<CartItemEntity> findByCartId(Long cartId);
    void deleteByCartIdAndBookId(Long cartId, Long bookId);

    CartItemEntity findByCartIdAndBookId(Long cartId, Long bookId);


    List<CartItemEntity> findByBookId(Long id);
}
