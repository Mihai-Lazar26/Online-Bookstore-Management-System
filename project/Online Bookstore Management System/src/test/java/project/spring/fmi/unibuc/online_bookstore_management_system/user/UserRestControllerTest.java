package project.spring.fmi.unibuc.online_bookstore_management_system.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.order.OrderService;
import project.spring.fmi.unibuc.online_bookstore_management_system.review.ReviewEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.review.ReviewRestController;
import project.spring.fmi.unibuc.online_bookstore_management_system.review.ReviewService;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ReviewRestControllerTest {

    @InjectMocks
    private ReviewRestController reviewRestController;

    @Mock
    private ReviewService reviewService;

    @Mock
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnReviewsForViewReviews() {
        // Arrange
        Long bookId = 1L;
        List<ReviewEntity> reviews = new ArrayList<>();
        when(reviewService.getReviewsByBookId(bookId)).thenReturn(reviews);

        // Act
        ResponseEntity<Object> responseEntity = reviewRestController.viewReviews(bookId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(reviews, responseEntity.getBody());
    }

    @Test
    void shouldReturnReviewFormForGetReviewFormWhenOrderExists() {
        // Arrange
        Long userId = 1L;
        Long bookId = 2L;
        when(orderService.existsOrderByUserIdAndBookId(userId, bookId)).thenReturn(true);

        // Act
        ResponseEntity<Object> responseEntity = reviewRestController.getReviewForm(userId, bookId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(new ReviewRestController.ReviewFormResponse(userId, bookId), responseEntity.getBody());
    }

    @Test
    void shouldReturnNotFoundForGetReviewFormWhenOrderNotExists() {
        // Arrange
        Long userId = 1L;
        Long bookId = 2L;
        when(orderService.existsOrderByUserIdAndBookId(userId, bookId)).thenReturn(false);

        // Act
        ResponseEntity<Object> responseEntity = reviewRestController.getReviewForm(userId, bookId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Order not found", responseEntity.getBody());
    }

    @Test
    void shouldReturnReviewSubmittedForSubmitReviewWhenOrderExists() {
        // Arrange
        Long userId = 1L;
        Long bookId = 2L;
        Integer rating = 5;
        String reviewText = "Great book!";
        when(orderService.existsOrderByUserIdAndBookId(userId, bookId)).thenReturn(true);

        // Act
        ResponseEntity<String> responseEntity = reviewRestController.submitReview(userId, bookId, rating, reviewText);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Review submitted", responseEntity.getBody());
        verify(reviewService, times(1)).submitReview(eq(userId), eq(bookId), eq(rating), eq(reviewText));
    }

    @Test
    void shouldReturnNotFoundForSubmitReviewWhenOrderNotExists() {
        // Arrange
        Long userId = 1L;
        Long bookId = 2L;
        Integer rating = 5;
        String reviewText = "Great book!";
        when(orderService.existsOrderByUserIdAndBookId(userId, bookId)).thenReturn(false);

        // Act
        ResponseEntity<String> responseEntity = reviewRestController.submitReview(userId, bookId, rating, reviewText);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Order not found", responseEntity.getBody());
        verify(reviewService, never()).submitReview(anyLong(), anyLong(), anyInt(), anyString());
    }
}
