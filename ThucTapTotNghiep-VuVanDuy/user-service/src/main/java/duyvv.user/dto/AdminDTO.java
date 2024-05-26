package duyvv.user.dto;

import duyvv.user.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AdminDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String code ;
    private String username;
    private String password;
    private Integer status;
    private byte[] images;
    private List<String> roles;


}
