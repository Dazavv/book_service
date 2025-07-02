package com.example.book_service;

import com.example.book_service.repository.Author;
import com.example.book_service.repository.AuthorRepository;
import com.example.book_service.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class AuthorServiceTest {
    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @Test
    public void addAuthorIfNotExistTest() {
        Author author = new Author("Александр Сергеевич Пушкин", 1799);

        when(authorRepository.findByName("Александр Сергеевич Пушкин")).thenReturn(Optional.empty());
        authorService.addAuthor(author);
        verify(authorRepository).save(author);
    }

    @Test
    public void addAuthorIfExistTest() {
        Author author = new Author("Александр Сергеевич Пушкин", 1799);

        when(authorRepository.findByName("Александр Сергеевич Пушкин")).thenReturn(Optional.of(author));
        assertThrows(IllegalStateException.class, () -> authorService.addAuthor(author));
        verify(authorRepository, never()).save(any());
    }

    @Test
    public void getAuthorIfNotExistTest() {
        Long id = 1L;
        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> authorService.getAuthorById(id));
        verify(authorRepository).findById(id);
    }

    @Test
    public void getAuthorIfExistTest() {
        Long id = 1L;
        Author author = new Author("Александр Сергеевич Пушкин", 1799);

        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        Author authorFromBD = authorService.getAuthorById(id);
        assertEquals(authorFromBD, author);
        verify(authorRepository).findById(id);
    }

    @Test
    public void getAuthorsIfExistTest() {
        Author author1 = new Author("Александр Сергеевич Пушкин", 1799);
        Author author2 = new Author("Лев Николаевич Толстой", 1828);

        List<Author> authorList = List.of(author1, author2);
        Page<Author> page = new PageImpl<>(authorList);
        Pageable pageable = PageRequest.of(0, 10);

        when(authorRepository.findAll(pageable)).thenReturn(page);
        Page<Author> result = authorService.findAll(pageable);
        assertEquals(2, result.getContent().size());
        verify(authorRepository).findAll(pageable);
    }
}
