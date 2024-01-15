package project.spring.fmi.unibuc.online_bookstore_management_system.book;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import project.spring.fmi.unibuc.online_bookstore_management_system.cart.CartService;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private BookService bookService;

    @Test
    void testGetAllBooks() {
        // Arrange
        List<BookEntity> mockBooks = new ArrayList<>();
        Mockito.when(bookRepository.findAll()).thenReturn(mockBooks);

        // Act
        List<BookEntity> result = bookService.getAllBooks();

        // Assert
        assertEquals(mockBooks, result);
    }

    @Test
    void testGetBookById() {
        // Arrange
        Long bookId = 1L;
        BookEntity mockBook = new BookEntity();
        Mockito.when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.ofNullable(mockBook));

        // Act
        BookEntity result = bookService.getBookById(bookId);

        // Assert
        assertEquals(mockBook, result);
    }

    @Test
    void testCreateBook() {
        // Arrange
        BookEntity mockBook = new BookEntity();
        Mockito.when(bookRepository.save(any(BookEntity.class))).thenReturn(mockBook);

        // Act
        BookEntity result = bookService.createBook(new BookEntity());

        // Assert
        assertEquals(mockBook, result);
    }

    @Test
    void testUpdateBook() {
        // Arrange
        Long bookId = 1L;
        BookEntity existingBook = new BookEntity();
        existingBook.setId(bookId);

        BookEntity updatedBook = new BookEntity();
        updatedBook.setId(bookId);
        updatedBook.setTitle("Updated Title");

        Mockito.when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.ofNullable(existingBook));
        Mockito.when(bookRepository.save(any(BookEntity.class))).thenReturn(updatedBook);

        // Act
        BookEntity result = bookService.updateBook(bookId, updatedBook);

        // Assert
        assertEquals(updatedBook, result);
    }

    @Test
    void testUpdateBookNonexistentBook() {
        // Arrange
        Long nonExistentBookId = 999L;
        BookEntity updatedBook = new BookEntity();

        Mockito.when(bookRepository.findById(nonExistentBookId)).thenReturn(java.util.Optional.empty());

        // Act
        BookEntity result = bookService.updateBook(nonExistentBookId, updatedBook);

        // Assert
        assertNull(result);
    }

    @Test
    void testDeleteBook() {
        // Arrange
        Long bookId = 1L;
        BookEntity existingBook = new BookEntity();
        existingBook.setId(bookId);

        Mockito.when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.ofNullable(existingBook));
        Mockito.doNothing().when(cartService).deleteCartItemsByBook(existingBook);

        // Act
        boolean result = bookService.deleteBook(bookId);

        // Assert
        assertTrue(result);
        Mockito.verify(bookRepository, Mockito.times(1)).delete(existingBook);
    }


    @Test
    void testDeleteBookNonexistentBook() {
        // Arrange
        Long nonExistentBookId = 999L;

        Mockito.when(bookRepository.findById(nonExistentBookId)).thenReturn(java.util.Optional.empty());

        // Act
        boolean result = bookService.deleteBook(nonExistentBookId);

        // Assert
        assertFalse(result);
        Mockito.verify(bookRepository, Mockito.never()).delete(any(BookEntity.class));
    }
}
