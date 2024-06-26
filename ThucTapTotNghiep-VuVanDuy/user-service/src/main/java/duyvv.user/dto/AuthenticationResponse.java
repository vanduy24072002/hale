package duyvv.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String accessToken;
    private String message ;
    private Integer statusCode ;
    public AuthenticationResponse(String message, Long statusCode){
        this.message = message ;

    }

//    @JsonProperty("refresh_token")
//    private String refreshToken;
}

