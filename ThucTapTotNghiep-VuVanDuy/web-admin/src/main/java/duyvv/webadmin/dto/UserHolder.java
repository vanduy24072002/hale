package duyvv.webadmin.dto;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserHolder {
    private Long id ;
    private String firstName;
    private String lastName;
    private String username;
    private String images;
    private String role ;
    private String token;
}

