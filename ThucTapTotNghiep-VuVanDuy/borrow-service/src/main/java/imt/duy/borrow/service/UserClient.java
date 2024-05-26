package imt.duy.borrow.service;

import imt.duy.borrow.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service" , url = "${user-service.url}")

public interface UserClient {
    String root = "/api/v1/user-service";
    @GetMapping({root+ "/findByUsername/{username}"})
    UserDTO findUserByUsername(@PathVariable String username);
}
