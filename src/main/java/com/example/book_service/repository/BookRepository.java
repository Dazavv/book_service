package com.example.book_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
//    @Query(value = "select * from books where title = :title", nativeQuery = true)
    Optional<Book> getBookByTitle(String title);

    @Query(value = "select * from books where title = ?1 and author_id = ?2 and id != ?3", nativeQuery = true)
    Optional<Book> getSameBook(String title, Long authorId, Long id);
}
