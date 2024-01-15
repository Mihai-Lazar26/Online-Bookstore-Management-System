package project.spring.fmi.unibuc.online_bookstore_management_system.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<ReviewEntity> getReviewsByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    public void submitReview(Long userId, Long bookId, Integer rating, String review) {
        ReviewEntity reviewEntity = reviewRepository.findByUserIdAndBookId(userId, bookId);

        if (reviewEntity == null) {
            reviewEntity = new ReviewEntity();
        }
        reviewEntity.setUserId(userId);
        reviewEntity.setBookId(bookId);
        reviewEntity.setRating(rating);
        reviewEntity.setReview(review);

        reviewRepository.save(reviewEntity);
    }
}
