package imt.duy.borrow.entities;

import com.imtTranding.core.entities.NewBaseEntities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_borrow")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Borrow extends NewBaseEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderBookNumber;
    private Integer numberDayBorrow ;
    private Long idBook;
    private String userNameBorrrow ;
    private Integer quantity;
}
