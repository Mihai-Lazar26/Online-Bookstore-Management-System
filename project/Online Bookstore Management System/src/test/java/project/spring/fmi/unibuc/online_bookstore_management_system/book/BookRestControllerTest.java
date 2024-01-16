package project.spring.fmi.unibuc.online_bookstore_management_system.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import project.spring.fmi.unibuc.online_bookstore_management_system.user.UserEntity;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookRestController.class)
@ExtendWith(MockitoExtension.class)
class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @BeforeEach
    void setUp() {
        // Set up the default user for each test
        UserEntity.signedInUser = new UserEntity();
        UserEntity.signedInUser.setAdmin(true);
    }


    @Test
    void shouldReturnAllBooks() throws Exception {
        // Arrange
        List<BookEntity> books = Arrays.asList(new BookEntity(), new BookEntity());
        when(bookService.getAllBooks()).thenReturn(books);

        // Act
        ResultActions result = mockMvc.perform(get("/api/books/all"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void shouldReturnBookByIdForAdminUser() throws Exception {
        // Arrange
        BookEntity book = new BookEntity();
        book.setId(1L);
        when(bookService.getBookById(anyLong())).thenReturn(book);

        // Act
        ResultActions result = mockMvc.perform(get("/api/books/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
        verify(bookService, times(1)).getBookById(anyLong());
    }

    @Test
    void shouldReturnNotFoundForNonExistingBook() throws Exception {
        // Arrange
        when(bookService.getBookById(anyLong())).thenReturn(null);

        // Act
        ResultActions result = mockMvc.perform(get("/api/books/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isNotFound());
        verify(bookService, times(1)).getBookById(anyLong());
    }

    @Test
    void shouldReturnCreatedForAddBook() throws Exception {
        // Arrange
        BookEntity book = new BookEntity();
        when(bookService.createBook(any())).thenReturn(book);

        // Act
        ResultActions result = mockMvc.perform(post("/api/books/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(book)));

        // Assert
        result.andExpect(status().isCreated());
        verify(bookService, times(1)).createBook(any());
    }

    @Test
    void shouldReturnOkForEditBook() throws Exception {
        // Arrange
        BookEntity updatedBook = new BookEntity();
        when(bookService.getBookById(anyLong())).thenReturn(new BookEntity());

        // Act
        ResultActions result = mockMvc.perform(put("/api/books/{id}/edit", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedBook)));

        // Assert
        result.andExpect(status().isOk());
        verify(bookService, times(1)).updateBook(anyLong(), any());
    }

    @Test
    void shouldReturnNotFoundForEditNonExistingBook() throws Exception {
        // Arrange
        BookEntity updatedBook = new BookEntity();
        when(bookService.getBookById(anyLong())).thenReturn(null);

        // Act
        ResultActions result = mockMvc.perform(put("/api/books/{id}/edit", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedBook)));

        // Assert
        result.andExpect(status().isNotFound());
        verify(bookService, never()).updateBook(anyLong(), any());
    }

    @Test
    void shouldReturnOkForDeleteBook() throws Exception {
        // Arrange
        when(bookService.getBookById(anyLong())).thenReturn(new BookEntity());

        // Act
        ResultActions result = mockMvc.perform(delete("/api/books/{id}/delete", 1));

        // Assert
        result.andExpect(status().isOk());
        verify(bookService, times(1)).deleteBook(anyLong());
    }

    @Test
    void shouldReturnNotFoundForDeleteNonExistingBook() throws Exception {
        // Arrange
        when(bookService.getBookById(anyLong())).thenReturn(null);

        // Act
        ResultActions result = mockMvc.perform(delete("/api/books/{id}/delete", 1));

        // Assert
        result.andExpect(status().isNotFound());
        verify(bookService, never()).deleteBook(anyLong());
    }

    // Additional tests for other methods

}
