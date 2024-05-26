package duyvv.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeInfoUser {
    private String firstName;
    private String lastName;
    private byte[] images ;
}
