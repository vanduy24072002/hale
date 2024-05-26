package com.imtTranding.BookService.controller;

import com.imtTranding.BookService.dto.BookResponse;
import com.imtTranding.BookService.dto.CategoryDTO;
import com.imtTranding.BookService.entities.Category;
import com.imtTranding.BookService.responsitory.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book-service")
@CrossOrigin(origins = "http://localhost:9000")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryRepository responsitory ;
    @GetMapping("/findCategoryAll")
    public ResponseEntity<List<CategoryDTO>> listAllBook() {
        List<CategoryDTO> categories = responsitory.findCategoryDTOAll();
        return new ResponseEntity<List<CategoryDTO>>(categories, HttpStatus.OK);

    }
}
