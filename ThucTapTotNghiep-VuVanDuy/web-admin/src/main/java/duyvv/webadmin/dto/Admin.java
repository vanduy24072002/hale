package duyvv.webadmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Admin {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String images;
    private Integer status;
    private List<String> roles;


}
