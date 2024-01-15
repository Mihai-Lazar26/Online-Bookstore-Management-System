package project.spring.fmi.unibuc.online_bookstore_management_system.book;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void testGetManageBooks() throws Exception {
        // Mock signed-in admin user
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(true);

        // Mock books
        List<BookEntity> books = new ArrayList<>();
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/books/manage"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookListManage"))
                .andExpect(model().attribute("books", hasSize(0)));

        // Reset static variables
        UserEntity.signedInUser = null;
    }

    @Test
    void testGetManageBooksUnauthenticatedUser() throws Exception {
        mockMvc.perform(get("/books/manage"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testGetAllBooks() throws Exception {
        // Mock signed-in user
        UserEntity.signedInUser = new UserEntity();

        // Mock books
        List<BookEntity> books = new ArrayList<>();
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/books/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookList"))
                .andExpect(model().attribute("books", hasSize(0)))
                .andExpect(model().attribute("userId", equalTo(UserEntity.signedInUser.getUserID())));

        // Reset static variables
        UserEntity.signedInUser = null;
    }

    @Test
    void testGetAllBooksUnauthenticatedUser() throws Exception {
        mockMvc.perform(get("/books/all"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testGetBookById() throws Exception {
        // Mock signed-in admin user
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(true);

        // Mock book
        BookEntity book = new BookEntity();
        when(bookService.getBookById(ArgumentMatchers.any(Long.class))).thenReturn(book);
        mockMvc.perform(get("/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("bookDetails"))
                .andExpect(model().attribute("book", equalTo(book)));

        // Reset static variables
        UserEntity.signedInUser = null;
    }

    @Test
    void testGetBookByIdUnauthenticatedUser() throws Exception {
        mockMvc.perform(get("/books/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testAddBookForm() throws Exception {
        // Mock signed-in admin user
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(true);

        mockMvc.perform(get("/books/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("addBook"))
                .andExpect(model().attribute("book", instanceOf(BookEntity.class)));

        // Reset static variables
        UserEntity.signedInUser = null;
    }

    @Test
    void testAddBookFormUnauthenticatedUser() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testAddBookSubmit() throws Exception {
        // Mock signed-in admin user
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(true);

        mockMvc.perform(post("/books/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/manage"));

        verify(bookService, times(1)).createBook(ArgumentMatchers.any(BookEntity.class));

        // Reset static variables
        UserEntity.signedInUser = null;
    }

    @Test
    void testAddBookSubmitUnauthenticatedUser() throws Exception {
        mockMvc.perform(post("/books/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testEditBookForm() throws Exception {
        // Mock signed-in admin user
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(true);

        // Mock book
        BookEntity book = new BookEntity();
        when(bookService.getBookById(ArgumentMatchers.any(Long.class))).thenReturn(book);

        mockMvc.perform(get("/books/{id}/edit", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("editBook"))
                .andExpect(model().attribute("book", equalTo(book)));

        // Reset static variables
        UserEntity.signedInUser = null;
    }

    @Test
    void testEditBookFormUnauthenticatedUser() throws Exception {
        mockMvc.perform(get("/books/{id}/edit", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testEditBookSubmit() throws Exception {
        // Mock signed-in admin user
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(true);

        mockMvc.perform(post("/books/{id}/edit", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/manage"));

        verify(bookService, times(1)).updateBook(eq(1L), ArgumentMatchers.any(BookEntity.class));

        // Reset static variables
        UserEntity.signedInUser = null;
    }

    @Test
    void testEditBookSubmitUnauthenticatedUser() throws Exception {
        mockMvc.perform(post("/books/{id}/edit", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testDeleteBook() throws Exception {
        // Mock signed-in admin user
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(true);

        mockMvc.perform(post("/books/{id}/delete", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/manage"));

        verify(bookService, times(1)).deleteBook(eq(1L));

        // Reset static variables
        UserEntity.signedInUser = null;
    }

    @Test
    void testDeleteBookUnauthenticatedUser() throws Exception {
        mockMvc.perform(post("/books/{id}/delete", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
