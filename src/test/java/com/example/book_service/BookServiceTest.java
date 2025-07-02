package com.example.book_service;

import com.example.book_service.repository.Author;
import com.example.book_service.repository.AuthorRepository;
import com.example.book_service.repository.Book;
import com.example.book_service.repository.BookRepository;
import com.example.book_service.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    public void addBookIfAuthorNotExistsTest() {
        Author author = new Author();
        author.setId(1L);

        Book book = new Book("Война и мир", author, null, null);
        book.setId(1L);

        assertThrows(IllegalStateException.class, () -> bookService.addBook(book));
        verify(bookRepository, never()).save(any());
    }

    @Test
    public void addBookIfAuthorExistsAndBookNotExistsTest() {
        Author author = new Author();
        author.setId(1L);

        Book book = new Book("Война и мир", author, null, null);
        book.setId(1L);

        when(bookRepository.getSameBook("Война и мир", author.getId())).thenReturn(Optional.empty());
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book savedBook = bookService.addBook(book);
        verify(bookRepository).save(book);
        assertEquals("Война и мир", savedBook.getTitle());
    }

    @Test
    public void addBookIfExistTest() {
        Author author = new Author();
        author.setId(1L);

        Book book = new Book("Война и мир", author, null, null);
        book.setId(1L);

        when(bookRepository.getSameBook("Война и мир", author.getId())).thenReturn(Optional.of(book));
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));

        assertThrows(IllegalStateException.class, () -> bookService.addBook(book));
        verify(bookRepository, never()).save(any());
    }

    @Test
    public void getBookIfNotExistTest() {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> bookService.getBookById(id));
        verify(bookRepository).findById(id);
    }

    @Test
    public void getBookIfExistTest() {
        Long id = 1L;
        Book book = new Book();
        book.setTitle("Война и мир");
        book.setId(id);

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        Book bookFromBD =  bookService.getBookById(id);
        assertEquals(bookFromBD, book);
        verify(bookRepository).findById(id);
    }

    @Test
    public void getBooksIfExistTest() {
        List<Book> books = List.of(new Book(), new Book());

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();
        assertEquals(2, result.size());
        verify(bookRepository).findAll();
    }

    @Test
    public void updateBookIfSimilarBookExistTest() {
        Author author = new Author();
        author.setId(1L);

        Book book1 = new Book("Война и мир", author, null, null);
        book1.setId(1L);

        Book book2 = new Book("Название", author, null, null);
        book2.setId(2L);

        when(bookRepository.findById(book2.getId())).thenReturn(Optional.of(book2));
        when(bookRepository.getSameBook("Война и мир", author.getId(), book2.getId())).thenReturn(Optional.of(book1));

        assertThrows(IllegalStateException.class, () -> bookService.updateBook(book2.getId(), "Война и мир", null, null, null));
        verify(bookRepository, never()).save(any());
    }

    @Test
    public void updateBookIfSimilarBookNotExistTest() {
        String newTitle = "Война и мир";
        int newYear = 1892;
        String newGenre = "Роман";

        Author author1 = new Author();
        author1.setId(1L);

        Author author2 = new Author();
        author2.setId(2L);

        Book book = new Book("Название", author1, null, null);
        book.setId(1L);

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookRepository.getSameBook(newTitle, author2.getId(), book.getId())).thenReturn(Optional.empty());
        when(authorRepository.findById(author2.getId())).thenReturn(Optional.of(author2));

        bookService.updateBook(book.getId(), newTitle, author2.getId(), newYear, newGenre);
        verify(bookRepository).save(book);

        assertEquals(newTitle, book.getTitle());
        assertEquals(author2.getId(), book.getAuthor().getId());
        assertEquals(newYear, book.getYear());
        assertEquals(newGenre, book.getGenre());
    }

}
