package com.imtTranding.BookService.responsitory;

import com.imtTranding.BookService.dto.CategoryDTO;
import com.imtTranding.BookService.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT b " +
            "FROM Category b "+
            "WHERE 1 = 1 " +
            "AND b.id = :id"
    )
    Category getCategoryById(Long  id);

    @Query(value = "SELECT b.nameShort " +
            "FROM Category b "+
            "WHERE 1 = 1 " +
            "AND b.id = :id"
    )
    String getNameShortById(Long id);

    @Query(value = "SELECT new com.imtTranding.BookService.dto.CategoryDTO(b.id, b.name, b.status, b.nameShort) " +
            "FROM Category b "+
            "WHERE 1 = 1 "
    )
    List<CategoryDTO> findCategoryDTOAll();
}
