package imt.duy.borrow.dto;

import com.imtTranding.core.entities.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {
    private Long id ;
    private Long idBook ;
    private Status status ;
    private Integer quantity ;
    private String position ;
    private Integer remain ;
    private Integer borrowed ;
}
