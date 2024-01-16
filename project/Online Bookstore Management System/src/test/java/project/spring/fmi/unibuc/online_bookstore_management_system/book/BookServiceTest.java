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
        List<BookEntity> mockBooks = new ArrayList<>();
        Mockito.when(bookRepository.findAll()).thenReturn(mockBooks);

        List<BookEntity> result = bookService.getAllBooks();

        assertEquals(mockBooks, result);
    }

    @Test
    void testGetBookById() {
        Long bookId = 1L;
        BookEntity mockBook = new BookEntity();
        Mockito.when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.ofNullable(mockBook));

        BookEntity result = bookService.getBookById(bookId);

        assertEquals(mockBook, result);
    }

    @Test
    void testCreateBook() {
        BookEntity mockBook = new BookEntity();
        Mockito.when(bookRepository.save(any(BookEntity.class))).thenReturn(mockBook);

        BookEntity result = bookService.createBook(new BookEntity());

        assertEquals(mockBook, result);
    }

    @Test
    void testUpdateBook() {
        Long bookId = 1L;
        BookEntity existingBook = new BookEntity();
        existingBook.setId(bookId);

        BookEntity updatedBook = new BookEntity();
        updatedBook.setId(bookId);
        updatedBook.setTitle("Updated Title");

        Mockito.when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.ofNullable(existingBook));
        Mockito.when(bookRepository.save(any(BookEntity.class))).thenReturn(updatedBook);

        BookEntity result = bookService.updateBook(bookId, updatedBook);

        assertEquals(updatedBook, result);
    }

    @Test
    void testUpdateBookNonexistentBook() {
        Long nonExistentBookId = 999L;
        BookEntity updatedBook = new BookEntity();

        Mockito.when(bookRepository.findById(nonExistentBookId)).thenReturn(java.util.Optional.empty());

        BookEntity result = bookService.updateBook(nonExistentBookId, updatedBook);

        assertNull(result);
    }

    @Test
    void testDeleteBook() {
        Long bookId = 1L;
        BookEntity existingBook = new BookEntity();
        existingBook.setId(bookId);

        Mockito.when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.ofNullable(existingBook));
        Mockito.doNothing().when(cartService).deleteCartItemsByBook(existingBook);

        boolean result = bookService.deleteBook(bookId);

        assertTrue(result);
        Mockito.verify(bookRepository, Mockito.times(1)).delete(existingBook);
    }


    @Test
    void testDeleteBookNonexistentBook() {
        Long nonExistentBookId = 999L;

        Mockito.when(bookRepository.findById(nonExistentBookId)).thenReturn(java.util.Optional.empty());

        boolean result = bookService.deleteBook(nonExistentBookId);

        assertFalse(result);
        Mockito.verify(bookRepository, Mockito.never()).delete(any(BookEntity.class));
    }
}
