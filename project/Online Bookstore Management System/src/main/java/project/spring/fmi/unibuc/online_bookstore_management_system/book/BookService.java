package project.spring.fmi.unibuc.online_bookstore_management_system.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    public BookEntity getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public BookEntity createBook(BookEntity book) {
        return bookRepository.save(book);
    }

    public BookEntity updateBook(Long id, BookEntity updatedBook) {
        BookEntity existingBook = bookRepository.findById(id)
                .orElse(null);

        if (existingBook != null) {
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setPrice(updatedBook.getPrice());

            return bookRepository.save(existingBook);
        }

        return null;
    }

    public boolean deleteBook(Long id) {
        BookEntity existingBook = bookRepository.findById(id)
                .orElse(null);

        if (existingBook != null) {
            bookRepository.delete(existingBook);
            return true;
        }

        return false;
    }


}

