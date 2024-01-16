package project.spring.fmi.unibuc.online_bookstore_management_system.review;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.order.OrderService;
import project.spring.fmi.unibuc.online_bookstore_management_system.review.ReviewEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.review.ReviewRestController;
import project.spring.fmi.unibuc.online_bookstore_management_system.review.ReviewService;
import project.spring.fmi.unibuc.online_bookstore_management_system.review.ReviewRestController.ReviewFormResponse;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewRestControllerTest {

    @Mock
    private ReviewService reviewService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private ReviewRestController reviewRestController;

    @Test
    void shouldReturnReviewsForViewReviews() {
        Long bookId = 1L;
        List<ReviewEntity> reviews = Arrays.asList(new ReviewEntity(), new ReviewEntity());

        when(reviewService.getReviewsByBookId(bookId)).thenReturn(reviews);

        ResponseEntity<Object> responseEntity = reviewRestController.viewReviews(bookId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(reviews, responseEntity.getBody());

        verify(reviewService, times(1)).getReviewsByBookId(bookId);
        verifyNoMoreInteractions(reviewService, orderService);
    }

    @Test
    void shouldReturnReviewFormForGetReviewFormWhenOrderExists() {
        Long userId = 1L;
        Long bookId = 2L;

        when(orderService.existsOrderByUserIdAndBookId(userId, bookId)).thenReturn(true);

        ResponseEntity<Object> responseEntity = reviewRestController.getReviewForm(userId, bookId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(orderService, times(1)).existsOrderByUserIdAndBookId(userId, bookId);
        verifyNoMoreInteractions(reviewService, orderService);
    }

    @Test
    void shouldReturnNotFoundForGetReviewFormWhenOrderNotExists() {
        Long userId = 1L;
        Long bookId = 2L;

        when(orderService.existsOrderByUserIdAndBookId(userId, bookId)).thenReturn(false);

        ResponseEntity<Object> responseEntity = reviewRestController.getReviewForm(userId, bookId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Order not found", responseEntity.getBody());

        verify(orderService, times(1)).existsOrderByUserIdAndBookId(userId, bookId);
        verifyNoMoreInteractions(reviewService, orderService);
    }

    @Test
    void shouldReturnReviewSubmittedForSubmitReviewWhenOrderExists() {
        Long userId = 1L;
        Long bookId = 2L;
        Integer rating = 5;
        String reviewText = "Excellent book!";

        when(orderService.existsOrderByUserIdAndBookId(userId, bookId)).thenReturn(true);

        ResponseEntity<String> responseEntity = reviewRestController.submitReview(userId, bookId, rating, reviewText);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Review submitted", responseEntity.getBody());

        verify(orderService, times(1)).existsOrderByUserIdAndBookId(userId, bookId);
        verify(reviewService, times(1)).submitReview(userId, bookId, rating, reviewText);
        verifyNoMoreInteractions(reviewService, orderService);
    }

    @Test
    void shouldReturnNotFoundForSubmitReviewWhenOrderNotExists() {
        Long userId = 1L;
        Long bookId = 2L;
        Integer rating = 5;
        String reviewText = "Excellent book!";

        when(orderService.existsOrderByUserIdAndBookId(userId, bookId)).thenReturn(false);

        ResponseEntity<String> responseEntity = reviewRestController.submitReview(userId, bookId, rating, reviewText);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Order not found", responseEntity.getBody());

        verify(orderService, times(1)).existsOrderByUserIdAndBookId(userId, bookId);
        verifyNoMoreInteractions(reviewService, orderService);
    }
}
