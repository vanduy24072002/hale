package duyvv.user.dto;

import duyvv.user.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String code;
    private String password;
    private byte[] images;
    private String roles;
    private Integer status ;
}
