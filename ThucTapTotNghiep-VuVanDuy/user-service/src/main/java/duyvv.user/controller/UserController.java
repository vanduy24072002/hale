package duyvv.user.controller;

import duyvv.user.dto.*;
import duyvv.user.service.JwtService;
import duyvv.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user-service")
@CrossOrigin(origins = "http://localhost:9000")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final JwtService jwtService ;

    @GetMapping("/getAllUser")
    public List<UserDTO> getAllUser(@RequestParam(required = false) String username){
        return service.getAllUser(username);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.status(service.authenticate(request).getStatusCode()).body(service.authenticate(request));
    }

    @GetMapping("/findUserByUsernameIsNotLock/{username}")
    public AdminDTO findUserByUsernameIsNotLock(@PathVariable String username) {
        System.out.println("PathVariable : "+ username);
        return service.findUserByUsernameIsNotLock(username);
    }
    @GetMapping("/findByUsername/{username}")
    public AdminDTO findUserByUsername(@PathVariable String username) {
        System.out.println("PathVariable : "+ username);
        return service.findUserByUsername(username);
    }

    @PostMapping("/generateToken")
    public String generateToken(@RequestBody String username) {
        return jwtService.generateToken(username);
    }
    @PostMapping("/updateInfoUser")
    public String updateInfoUser(@RequestBody ChangeInfoUser info, @RequestParam Long id) {
        String message = service.updateInfoUser(info, id);
        return message ;
    }
    @PostMapping("/changePassword")
    public String changePassword(@RequestBody String password, @RequestParam Long id) {
        String message = service.changePassword(password, id);
        return message ;
    }
    @PostMapping("/crateNewUser")
    public String crateNewUser(@RequestBody UserDTO user) {
        String message = service.crateNewUser(user);
        return message ;
    }
    @GetMapping("/getUserById")
    public Object findById(@RequestParam Long id){
        return service.findUserDTOById(id);
    }
    @GetMapping("/updateStatusUser")
    public String updateStatusUser(@RequestParam Long id, @RequestParam Integer action){
        return service.updateStatusUser(id,action);
    }





}
