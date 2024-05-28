package com.imtTranding.BookService.controller;

import ch.qos.logback.core.model.Model;
import com.imtTranding.BookService.dto.BookResponse;
import com.imtTranding.BookService.dto.CategoryDTO;
import com.imtTranding.BookService.entities.Category;
import com.imtTranding.BookService.responsitory.BookResponsitory;
import com.imtTranding.BookService.responsitory.CategoryRepository;
import com.imtTranding.core.entities.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book-service")
@CrossOrigin(origins = "http://localhost:9000")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryRepository responsitory ;
    private final BookResponsitory bookResponsitory ;
    @GetMapping("/findCategoryAll")
    public ResponseEntity<List<CategoryDTO>> listAllCategory() {
        List<CategoryDTO> categories = responsitory.findCategoryDTOAll();
        for(CategoryDTO category : categories){
            if(bookResponsitory.findBookByCategory(category.getId()).size() > 0){
                category.setIsDelete(false);
                continue;
            }
            category.setIsDelete(true);
        }
        return new ResponseEntity<List<CategoryDTO>>(categories, HttpStatus.OK);

    }
    @PostMapping(value = "/addOrUpdateCategory")
    public String addOrUpdateCategory(@RequestBody CategoryDTO categoryDTO) {

        Category category = responsitory.findById(categoryDTO.getId()).orElse(new Category());

        if(category != null){

            category.setName(categoryDTO.getName());
            category.setNameShort(categoryDTO.getNameShort());
            responsitory.save(category);
            return "Cập nhật thành công" ;
        }

        if(responsitory.findCategoryByName(categoryDTO.getName(), categoryDTO.getNameShort()).size() > 0){
            return "Tên hoặc tên viết tắt đã tồn tại";
        }
        category.setName(categoryDTO.getName());
        category.setNameShort(categoryDTO.getNameShort());
        responsitory.save(category);
        return "Thêm thành công" ;
    }
    @GetMapping("/deleteCategory")
    public String deleteCategory(@PathVariable Long id) {
        if(id != null){
            Category category = responsitory.getCategoryById(id);
            category.setStatus(Status.DELETED);
            responsitory.save(category);
            return "Xóa thành công" ;
        }
        return "Thể loại không tồn tại" ;
    }


}
