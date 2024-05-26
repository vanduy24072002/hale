//package duyvv.webadmin.service.feign;
//import duyvv.webadmin.dto.Admin;
//import duyvv.webadmin.url.UrlService;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.cloud.openfeign.FeignClient;
//
//@Service
//@FeignClient(name = "user-service")
//public interface UserClient {
//
//    @PostMapping(value = {"/findByUsername"})
//    Admin findByUsername(@RequestBody String username);
//
//    @PostMapping(value = {"/generateToken"})
//    String token(@RequestBody String username);
//}
