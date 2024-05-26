package duyvv.webadmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class BookDTO {

    private Long id ;
    private String title ;
    private String description ;
    private String images;
    private String author;
    private Long category ;
    private String categoryName ;


}
