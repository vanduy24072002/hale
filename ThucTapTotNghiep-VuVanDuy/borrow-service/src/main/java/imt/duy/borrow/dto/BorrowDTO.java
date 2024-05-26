package imt.duy.borrow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowDTO {
    private Long id;
    private String orderBookNumber;
    private Integer numberDayBorrow ;
    private Integer quantity;
    private String code ;
    private String firstName;
    private String lastName;
    private Long idBook;
    private String codeBook;
    private Integer status ;
    private String title;
    private String author;
    private String description;
    private String images;
    private Long category;
    private LocalDateTime borrowDate ;

}
