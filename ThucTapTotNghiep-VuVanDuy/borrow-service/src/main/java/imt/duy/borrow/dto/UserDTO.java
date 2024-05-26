package imt.duy.borrow.dto;

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
    private String code ;
    private String username;
    private String password;
    private byte[] images;
    private List<String> roles;


}
