package project.spring.fmi.unibuc.online_bookstore_management_system.review;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.spring.fmi.unibuc.online_bookstore_management_system.review.ReviewEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.review.ReviewRepository;
import project.spring.fmi.unibuc.online_bookstore_management_system.review.ReviewService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void getReviewsByBookId_ReturnsCorrectList() {
        Long bookId = 1L;
        List<ReviewEntity> expectedReviews = new ArrayList<>();
        when(reviewRepository.findByBookId(bookId)).thenReturn(expectedReviews);

        List<ReviewEntity> actualReviews = reviewService.getReviewsByBookId(bookId);

        assertEquals(expectedReviews, actualReviews);
    }

    @Test
    void submitReview_ExistingReviewEntity_SavesUpdatedReview() {
        Long userId = 1L;
        Long bookId = 2L;
        Integer rating = 4;
        String reviewText = "Good book!";
        ReviewEntity existingReview = new ReviewEntity();

        when(reviewRepository.findByUserIdAndBookId(userId, bookId)).thenReturn(existingReview);

        reviewService.submitReview(userId, bookId, rating, reviewText);

        verify(reviewRepository).save(existingReview);
        assertEquals(userId, existingReview.getUserId());
        assertEquals(bookId, existingReview.getBookId());
        assertEquals(rating, existingReview.getRating());
        assertEquals(reviewText, existingReview.getReview());
    }

    @Test
    void submitReview_NewReviewEntity_SavesNewReview() {
        Long userId = 1L;
        Long bookId = 2L;
        Integer rating = 5;
        String reviewText = "Great book!";
        ReviewEntity newReview = null;

        when(reviewRepository.findByUserIdAndBookId(userId, bookId)).thenReturn(newReview);

        reviewService.submitReview(userId, bookId, rating, reviewText);

        verify(reviewRepository).save(Mockito.any(ReviewEntity.class));
    }
}
