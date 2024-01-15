package project.spring.fmi.unibuc.online_bookstore_management_system.book;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import project.spring.fmi.unibuc.online_bookstore_management_system.cart.CartItemEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class BookEntity {
    @Id
    @SequenceGenerator(
            name = "book_sequence",
            sequenceName = "book_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "book_sequence"
    )
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String author;
    @NonNull
    private Integer price;

    public BookEntity() {}

    public BookEntity(Long id, String title, String author, Integer price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public BookEntity(String title, String author, Integer price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "BookEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                '}';
    }
}

