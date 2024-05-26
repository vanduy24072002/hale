package com.duyvv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    private Long id ;
    private String title;
    private String codeBook;
    private String description;
    private String author;
    private Long category;
    private String categoryName ;
    private byte[] images;
}
