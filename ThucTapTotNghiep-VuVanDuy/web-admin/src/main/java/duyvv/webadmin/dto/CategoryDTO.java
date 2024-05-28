package duyvv.webadmin.dto;

import ch.qos.logback.core.status.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String status;
    private String nameShort;
    private Boolean isDelete ;

    public CategoryDTO(Long id, String name, String nameShort) {
        this.id = id;
        this.name = name;
        this.nameShort = nameShort;
    }
}
