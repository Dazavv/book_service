package com.example.book_service.service;

import com.example.book_service.repository.Author;
import com.example.book_service.repository.AuthorRepository;
import com.example.book_service.repository.Book;
import com.example.book_service.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    public Book getBookById(Long id) {
        Optional<Book> checkedBook = bookRepository.findById(id);
        if (checkedBook.isEmpty()) throw new IllegalStateException("book с id - " + id + " не существует");
        return checkedBook.get();
    }

    public Book addBook(Book book) {
        Long authorId = book.getAuthor().getId();
        Optional<Book> checkedBook = bookRepository.getBookByTitle(book.getTitle());
        Optional<Author> checkedAuthor = authorRepository.findById(authorId);
        if (checkedAuthor.isEmpty()) throw new IllegalStateException("book не удалось добавить т.к. author с id - " + authorId + " не добавлен");
        if (checkedBook.isPresent() && checkedBook.get().getAuthor().getId().equals(book.getAuthor().getId())) throw new IllegalStateException("book с названием " + book.getTitle() + " уже добавлена");
        book.setAuthor(checkedAuthor.get());
        return bookRepository.save(book);
    }

    public void updateBook(Long id, String title, Long authorId, Integer year, String genre) {
        Optional<Book> checkedBook = bookRepository.findById(id);
        if (checkedBook.isEmpty()) throw new IllegalStateException("обновить информацию не удалось, book c id - " + id + " не добавлена");

        Book book = checkedBook.get();

        if (title != null && !title.equals(book.getTitle())) {
            book.setTitle(title);
        }

        if (year != null && !year.equals(book.getYear())) {
            book.setYear(year);
        }

        if (genre != null && !genre.equals(book.getGenre())) {
            book.setGenre(genre);
        }

        if (authorId != null && !authorId.equals(book.getAuthor().getId())) {
            Optional<Author> checkedAuthor = authorRepository.findById(authorId);
            if (checkedAuthor.isEmpty()) throw new IllegalStateException("обновить информацию не удалось, author c id - " + id + " не добавлен");
            book.setAuthor(checkedAuthor.get());
        }

        Optional<Book> similarBook = bookRepository.getSameBook(book.getTitle(), book.getAuthor().getId(), id);
        if (similarBook.isPresent()) throw new IllegalStateException("обновить информацию не удалось, book с title \"" + book.getTitle() + "\" и author - \"" + book.getAuthor().getName() + "\" уже добавлена");

        bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        Optional<Book> checkedBook = bookRepository.findById(id);
        if (checkedBook.isEmpty()) throw new IllegalStateException("book с id - " + id + " не существует");
        bookRepository.deleteById(id);
    }
}
