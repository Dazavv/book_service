package com.example.book_service.contoller;

import com.example.book_service.repository.Author;
import com.example.book_service.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping()
    public Page<Author> getAllAuthors(@PageableDefault(sort = "id") Pageable pageable) {
        return authorService.findAll(pageable);
    }

    @GetMapping(path = "{id}")
    public Author getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @PostMapping
    public Author addAuthor(@RequestBody Author author) {
        return authorService.addAuthor(author);
    }
}
