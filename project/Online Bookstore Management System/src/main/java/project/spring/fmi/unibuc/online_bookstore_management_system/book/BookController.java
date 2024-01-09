package project.spring.fmi.unibuc.online_bookstore_management_system.book;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/all")
    public String getAllBooks(Model model) {
        List<BookEntity> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "bookList"; // Thymeleaf view name (bookList.html)
    }

    @GetMapping("/{id}")
    public String getBookById(@PathVariable Long id, Model model) {
        BookEntity book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "bookDetails"; // Thymeleaf view name (bookDetails.html)
    }

    @GetMapping("/add")
    public String addBookForm(Model model) {
        model.addAttribute("book", new BookEntity());
        return "addBook"; // Thymeleaf view name for the form to add a new book (addBook.html)
    }

    @PostMapping("/add")
    public String addBookSubmit(@ModelAttribute("book") BookEntity book) {
        bookService.createBook(book);
        return "redirect:/books/all"; // Redirect to the book list view after adding a book
    }

    @GetMapping("/{id}/edit")
    public String editBookForm(@PathVariable Long id, Model model) {
        BookEntity book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "editBook"; // Thymeleaf view name for editing a book (editBook.html)
    }

    @PostMapping("/{id}/edit")
    public String editBookSubmit(@PathVariable Long id, @ModelAttribute("book") BookEntity updatedBook) {
        bookService.updateBook(id, updatedBook);
        return "redirect:/books/all"; // Redirect to the book list view after updating a book
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books/all"; // Redirect to the book list view after deleting a book
    }

    // Other methods for adding, updating, and deleting books can be added similarly
}


