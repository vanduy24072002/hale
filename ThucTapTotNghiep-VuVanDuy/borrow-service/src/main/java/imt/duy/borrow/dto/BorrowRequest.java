package imt.duy.borrow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowRequest {
    private String username ;
    private Long idBook;
    private Integer numberDayBorrow ;
    private Integer quantity;
}
