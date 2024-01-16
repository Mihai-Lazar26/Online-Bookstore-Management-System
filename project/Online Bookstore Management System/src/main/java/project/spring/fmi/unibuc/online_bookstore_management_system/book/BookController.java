package project.spring.fmi.unibuc.online_bookstore_management_system.book;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/manage")
    public String getManageBooks(Model model) {
        if (UserEntity.signedInUser == null || !UserEntity.signedInUser.getAdmin())
            return "redirect:/";
        List<BookEntity> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "bookListManage";
    }

    @GetMapping("/all")
    public String getAllBooks(Model model) {
        if (UserEntity.signedInUser == null) {
            return "reroute:/";
        }
        List<BookEntity> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("userId", UserEntity.signedInUser.getUserID());
        return "bookList";
    }


    @GetMapping("/{id}")
    public String getBookById(@PathVariable Long id, Model model) {
        if (UserEntity.signedInUser == null || !UserEntity.signedInUser.getAdmin())
            return "redirect:/";
        BookEntity book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "bookDetails";
    }

    @GetMapping("/add")
    public String addBookForm(Model model) {
        if (UserEntity.signedInUser == null || !UserEntity.signedInUser.getAdmin())
            return "redirect:/";
        model.addAttribute("book", new BookEntity());
        return "addBook";
    }

    @PostMapping("/add")
    public String addBookSubmit(@ModelAttribute("book") BookEntity book) {
        if (UserEntity.signedInUser == null || !UserEntity.signedInUser.getAdmin())
            return "redirect:/";
        bookService.createBook(book);
        return "redirect:/books/manage";
    }

    @GetMapping("/{id}/edit")
    public String editBookForm(@PathVariable Long id, Model model) {
        if (UserEntity.signedInUser == null || !UserEntity.signedInUser.getAdmin())
            return "redirect:/";
        BookEntity book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "editBook";
    }

    @PostMapping("/{id}/edit")
    public String editBookSubmit(@PathVariable Long id, @ModelAttribute("book") BookEntity updatedBook) {
        if (UserEntity.signedInUser == null || !UserEntity.signedInUser.getAdmin())
            return "redirect:/";
        bookService.updateBook(id, updatedBook);
        return "redirect:/books/manage";
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        if (UserEntity.signedInUser == null || !UserEntity.signedInUser.getAdmin())
            return "redirect:/";
        bookService.deleteBook(id);
        return "redirect:/books/manage";
    }
}


