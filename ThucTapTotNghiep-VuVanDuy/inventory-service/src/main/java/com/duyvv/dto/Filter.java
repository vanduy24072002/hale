package com.duyvv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter {
    LocalDateTime fromDate ;
    LocalDateTime endDate ;
    Integer status ;
}
