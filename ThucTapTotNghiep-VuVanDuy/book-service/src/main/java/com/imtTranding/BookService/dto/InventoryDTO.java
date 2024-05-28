package com.imtTranding.BookService.dto;

import com.imtTranding.core.entities.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {
    private Long id ;
    private Long bookCode ;
    private Status status ;
    private Integer quantity ;
    private String position ;
    private Integer remain ;
    private Integer borrowed ;

}
