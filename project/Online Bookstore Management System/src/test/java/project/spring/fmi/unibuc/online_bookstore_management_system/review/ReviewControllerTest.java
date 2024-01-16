package project.spring.fmi.unibuc.online_bookstore_management_system.review;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookService;
import project.spring.fmi.unibuc.online_bookstore_management_system.order.OrderService;
import project.spring.fmi.unibuc.online_bookstore_management_system.review.ReviewController;
import project.spring.fmi.unibuc.online_bookstore_management_system.review.ReviewEntity;
import project.spring.fmi.unibuc.online_bookstore_management_system.review.ReviewService;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @Mock
    private OrderService orderService;

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private ReviewController reviewController;

    @Test
    void viewReviews_ReturnsCorrectViewNameAndAddsAttributesToModel() {
        Long bookId = 1L;
        List<ReviewEntity> reviews = new ArrayList<>();
        BookEntity bookEntity = new BookEntity();
        bookEntity.setTitle("Test Book");

        Mockito.when(reviewService.getReviewsByBookId(bookId)).thenReturn(reviews);
        Mockito.when(bookService.getBookById(bookId)).thenReturn(bookEntity);

        String viewName = reviewController.viewReviews(model, bookId);

        assertEquals("reviewPage", viewName);
        Mockito.verify(model).addAttribute("reviews", reviews);
        Mockito.verify(model).addAttribute("bookTitle", bookEntity.getTitle());
        Mockito.verify(model).addAttribute("userService", userService);
    }

    @Test
    void getReviewForm_OrderExists_ReturnsCorrectViewNameAndAddsAttributesToModel() {
        Long userId = 1L;
        Long bookId = 2L;

        Mockito.when(orderService.existsOrderByUserIdAndBookId(userId, bookId)).thenReturn(true);

        String viewName = reviewController.getReviewForm(model, userId, bookId);

        assertEquals("reviewForm", viewName);
        Mockito.verify(model).addAttribute("userId", userId);
        Mockito.verify(model).addAttribute("bookId", bookId);
    }

    @Test
    void getReviewForm_OrderDoesNotExist_RedirectsToHomePage() {
        Long userId = 1L;
        Long bookId = 2L;

        Mockito.when(orderService.existsOrderByUserIdAndBookId(userId, bookId)).thenReturn(false);

        String viewName = reviewController.getReviewForm(model, userId, bookId);

        assertEquals("redirect:/", viewName);
    }

    @Test
    void submitReview_CallsSubmitReviewAndRedirectsToCorrectView() {
        Long userId = 1L;
        Long bookId = 2L;
        Integer rating = 5;
        String reviewText = "Great book!";

        String viewName = reviewController.submitReview(userId, bookId, rating, reviewText);

        Mockito.verify(reviewService).submitReview(userId, bookId, rating, reviewText);
        assertEquals("redirect:/review/viewAll?bookId=" + bookId, viewName);
    }
}
