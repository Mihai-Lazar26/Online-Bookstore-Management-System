package project.spring.fmi.unibuc.online_bookstore_management_system.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.spring.fmi.unibuc.online_bookstore_management_system.book.BookService;
import project.spring.fmi.unibuc.online_bookstore_management_system.order.OrderService;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserService;

import java.util.List;

@Controller
@RequestMapping("review")
public class ReviewController {
    private final ReviewService reviewService;
    private final OrderService orderService;
    private final BookService bookService;
    private final UserService userService;

    @Autowired
    public ReviewController(ReviewService reviewService, OrderService orderService, BookService bookService, UserService userService) {
        this.reviewService = reviewService;
        this.orderService = orderService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("/viewAll")
    public String viewReviews(Model model, @RequestParam("bookId") Long bookId) {
        List<ReviewEntity> reviews =  reviewService.getReviewsByBookId(bookId);
        model.addAttribute("reviews", reviews);
        model.addAttribute("bookTitle", bookService.getBookById(bookId).getTitle());
        model.addAttribute("userService", userService);
        return "reviewPage";
    }

    @GetMapping("form")
    public String getReviewForm(Model model, @RequestParam("userId") Long userId, @RequestParam("bookId") Long bookId) {
        Boolean existsOrder = orderService.existsOrderByUserIdAndBookId(userId, bookId);

        if (existsOrder) {
            model.addAttribute("userId", userId);
            model.addAttribute("bookId", bookId);
            return "reviewForm";
        }

        return "redirect:/";
    }

    @PostMapping("/submitReview")
    public String submitReview(@RequestParam("userId") Long userId,
                               @RequestParam("bookId") Long bookId,
                               @RequestParam("rating") Integer rating,
                               @RequestParam("review") String review) {

        reviewService.submitReview(userId, bookId, rating, review);

        return "redirect:/review/viewAll?bookId=" + bookId;
    }


}
