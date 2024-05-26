package com.imtTranding.BookService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddRequest {
    private Long id ;
    private String title;
    private String description;
    private String author;
    private Long category;
    private byte[] images;
}
