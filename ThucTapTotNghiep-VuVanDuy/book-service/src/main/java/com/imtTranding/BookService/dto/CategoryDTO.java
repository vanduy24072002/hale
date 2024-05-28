package com.imtTranding.BookService.dto;

import com.imtTranding.core.entities.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private Status status;
    private String nameShort;
    private Boolean isDelete ;

    public CategoryDTO(Long id, String name, Status status, String nameShort) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.nameShort = nameShort;
    }
}
