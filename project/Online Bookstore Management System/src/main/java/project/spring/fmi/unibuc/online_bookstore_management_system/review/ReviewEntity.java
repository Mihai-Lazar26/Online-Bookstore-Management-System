package project.spring.fmi.unibuc.online_bookstore_management_system.review;

import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long bookId;
    private Integer rating;
    private String review;

    public ReviewEntity() {}

    public ReviewEntity(Long id, Long userId, Long bookId, Integer rating, String review) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.rating = rating;
        this.review = review;
    }

    public ReviewEntity(Long userId, Long bookId, Integer rating, String review) {
        this.userId = userId;
        this.bookId = bookId;
        this.rating = rating;
        this.review = review;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public String toString() {
        return "ReviewEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", bookId=" + bookId +
                ", rating=" + rating +
                ", review='" + review + '\'' +
                '}';
    }
}
