package duyvv.webadmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestBook {
    private Long id ;
    private String title ;
    private String description ;
    private byte[] images;
    private String author;
    private Long category ;
    private String position ;
    private Integer quantity ;
    private String categoryName ;
}
