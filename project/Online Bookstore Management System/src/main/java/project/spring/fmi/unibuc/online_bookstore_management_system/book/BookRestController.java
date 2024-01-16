package project.spring.fmi.unibuc.online_bookstore_management_system.book;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book", description = "Book operations")
public class BookRestController {

    private final BookService bookService;

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/manage")
    public ResponseEntity<List<BookEntity>> getManageBooks() {
        if (UserEntity.signedInUser == null || !UserEntity.signedInUser.getAdmin()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<BookEntity> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookEntity>> getAllBooks() {
        if (UserEntity.signedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<BookEntity> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookEntity> getBookById(@PathVariable Long id) {
        if (UserEntity.signedInUser == null || !UserEntity.signedInUser.getAdmin()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        BookEntity book = bookService.getBookById(id);
        if (book == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addBook(@RequestBody BookEntity book) {
        if (UserEntity.signedInUser == null || !UserEntity.signedInUser.getAdmin()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        bookService.createBook(book);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<Void> editBook(@PathVariable Long id, @RequestBody BookEntity updatedBook) {
        if (UserEntity.signedInUser == null || !UserEntity.signedInUser.getAdmin()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (bookService.getBookById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        bookService.updateBook(id, updatedBook);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (UserEntity.signedInUser == null || !UserEntity.signedInUser.getAdmin()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (bookService.getBookById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
