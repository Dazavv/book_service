package com.example.book_service.contoller;

import com.example.book_service.repository.Author;
import com.example.book_service.repository.Book;
import com.example.book_service.service.BookService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping(path = "{id}")
    public Book getBook(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public Book addBook(@Valid @RequestBody Book book) {
        return bookService.addBook(book);
    }

    @PutMapping(path = "{id}")
    public void updateBook(@PathVariable Long id,
                           @RequestParam(required = false) String title,
                           @RequestParam(required = false) Long authorId,
                           @RequestParam(required = false) Integer year,
                           @RequestParam(required = false) String genre) {
        bookService.updateBook(id, title, authorId, year, genre);
    }

    @DeleteMapping(path = "{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}
