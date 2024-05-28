package com.imtTranding.BookService.responsitory;

import com.imtTranding.BookService.dto.BookResponse;
import com.imtTranding.BookService.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface BookResponsitory extends JpaRepository<Book, Long> {
    @Query(value = "SELECT new com.imtTranding.BookService.dto.BookResponse (b.id, b.title, b.codeBook,b.description, b.author,b.category.id, b.category.name, b.images ) " +
            "FROM Book b"
    )
    List<BookResponse> findBookRepositoryAll();

    @Query(value = "SELECT b " +
            "FROM Book b "+
            "WHERE 1 = 1 " +
            "AND b.category.id = :id "
    )
    List<Book> findBookByCategory(Long id);
}
