package com.duyvv.model;

import com.imtTranding.core.entities.NewBaseEntities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name="tbl_warehouse")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inventory extends NewBaseEntities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idBook;
    private Integer quantity;
    private String position ;
    private Integer remain ;
    private Integer borrowed ;
}
