package com.duyvv.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {
    private Long id ;
    private Long idBook ;
    private Integer quantity ;
    private Integer status ;
    private LocalDateTime importDate ;
    // Data Book
    private String codeBook;
    private String title;
    private byte[] images;
    private String description;
    private String author;
    private Long category;
    private String categoryName ;

}
