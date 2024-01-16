package project.spring.fmi.unibuc.online_bookstore_management_system.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookService;
import project.spring.fmi.unibuc.online_bookstore_management_system.order.OrderService;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/review")
public class ReviewRestController {
    private final ReviewService reviewService;
    private final OrderService orderService;
    private final BookService bookService;
    private final UserService userService;

    @Autowired
    public ReviewRestController(ReviewService reviewService, OrderService orderService, BookService bookService, UserService userService) {
        this.reviewService = reviewService;
        this.orderService = orderService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("/viewAll")
    public ResponseEntity<Object> viewReviews(@RequestParam("bookId") Long bookId) {
        List<ReviewEntity> reviews = reviewService.getReviewsByBookId(bookId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/form")
    public ResponseEntity<Object> getReviewForm(@RequestParam("userId") Long userId, @RequestParam("bookId") Long bookId) {
        Boolean existsOrder = orderService.existsOrderByUserIdAndBookId(userId, bookId);

        if (existsOrder) {
            ReviewFormResponse response = new ReviewFormResponse(userId, bookId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/submitReview")
    public ResponseEntity<String> submitReview(@RequestParam("userId") Long userId,
                                               @RequestParam("bookId") Long bookId,
                                               @RequestParam("rating") Integer rating,
                                               @RequestParam("review") String review) {
        Boolean existsOrder = orderService.existsOrderByUserIdAndBookId(userId, bookId);

        if (existsOrder) {
            reviewService.submitReview(userId, bookId, rating, review);
            return new ResponseEntity<>("Review submitted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
    }

    public static class ReviewFormResponse {
        private final Long userId;
        private final Long bookId;

        public ReviewFormResponse(Long userId, Long bookId) {
            this.userId = userId;
            this.bookId = bookId;
        }

        public Long getUserId() {
            return userId;
        }

        public Long getBookId() {
            return bookId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ReviewFormResponse that)) return false;
            return Objects.equals(userId, that.userId) && Objects.equals(bookId, that.bookId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, bookId);
        }
    }
}
