package com.example.book_service.service;

import com.example.book_service.repository.Author;
import com.example.book_service.repository.AuthorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Page<Author> findAll(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    public Author addAuthor(Author author) {
        Optional<Author> authorFromBD = authorRepository.findByName(author.getName());
        if (authorFromBD.isPresent()) throw new IllegalStateException("author с именем " + author.getName() + " уже существует");
        return authorRepository.save(author);
    }

    public Author getAuthorById(Long id) {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isEmpty()) throw new IllegalStateException("author с id - " + id + " не существует");
        return author.get();
    }
}
