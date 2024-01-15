package project.spring.fmi.unibuc.online_bookstore_management_system.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByBookId(Long bookId);

    ReviewEntity findByUserIdAndBookId(Long userId, Long bookId);
}
