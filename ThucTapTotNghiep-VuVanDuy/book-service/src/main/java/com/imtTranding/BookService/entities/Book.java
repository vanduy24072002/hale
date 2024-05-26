package com.imtTranding.BookService.entities;

import com.imtTranding.core.entities.NewBaseEntities;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "tbl_book")

public class Book extends NewBaseEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "codeBook", length = 10) // tên ngắn thể loại + id sách định dạng 5 chữ số
    private String codeBook;

    @Column(name = "title", length = 255)
    private String title;

    @Lob
    @Column(name = "images")
    private byte[] images;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "author", length = 50)
    private String author;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;


}
