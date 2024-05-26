package duyvv.user.service.impl;

import com.imtTranding.core.entities.Status;
import duyvv.user.dto.*;
import duyvv.user.entities.Role;
import duyvv.user.entities.User;
import duyvv.user.reponsitory.RoleRepository;
import duyvv.user.reponsitory.UserRepository;
import duyvv.user.service.JwtService;
import duyvv.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository repository;
    @Autowired
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = repository.findUserByUsername(request.getUsername());
        if(user == null){
            return AuthenticationResponse.builder().message("Incorrect username or password").statusCode(401).build();
        }
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return AuthenticationResponse.builder().message("Incorrect username or password").statusCode(401).build();
        }
        String jwtToken = jwtService.generateToken(user.getUsername());
//        var refreshToken = jwtService.generateRefreshToken(user);
//        revokeAllUserTokens(user);
//        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .message("Logged in successfully")
                .statusCode(200)
                .build();
    }

    @Override
    public AdminDTO findUserByUsernameIsNotLock(String username) {
        User user = repository.findUserByUsernameIsNotLock(username);
        if(user == null){
            return null ;
        }

        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(user.getId());
        adminDTO.setUsername(user.getUsername());
        adminDTO.setFirstName(user.getFirstName());
        adminDTO.setLastName(user.getLastName());
        adminDTO.setCode(user.getCode());
        adminDTO.setImages(user.getImages());
        adminDTO.setPassword(user.getPassword());
        adminDTO.setRoles(user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()));
        System.out.println(adminDTO);
        return adminDTO ;
    }

    @Override
    public AdminDTO findUserByUsername(String username) {
        User user = repository.findUserByUsername(username);
        if(user == null){
            return null ;
        }

        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(user.getId());
        adminDTO.setUsername(user.getUsername());
        adminDTO.setFirstName(user.getFirstName());
        adminDTO.setLastName(user.getLastName());
        adminDTO.setCode(user.getCode());
        adminDTO.setStatus(user.getStatus().getValue());
        if(user.getImages() == null){
            adminDTO.setImages(getImageBytesFromURL("http://localhost:9002/images/avatar_none.jpg"));
        }else{
            adminDTO.setImages(user.getImages());
        }
        adminDTO.setPassword(user.getPassword());
        adminDTO.setRoles(user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()));
        System.out.println(adminDTO);
        return adminDTO ;
    }

    @Override
    public String updateInfoUser(ChangeInfoUser info, Long id) {
        User user = repository.findById(id).orElse(new User());
        if(info.getImages()!=null){
            user.setImages(info.getImages());
        }
        user.setFirstName(info.getFirstName());
        user.setLastName(info.getLastName());
        repository.save(user);
        return "Cập nhật thông tin thành công";
    }

    @Override
    public String changePassword(String password, Long id) {
        User user = repository.findById(id).orElse(new User());
        user.setPassword(passwordEncoder.encode(password));
        repository.save(user);
        return "Thay đổi mật khẩu thành công";
    }

    @Override
    public String crateNewUser(UserDTO userDTO) {
        if(repository.findUserByUsername(userDTO.getUsername()) != null){
            return "Username này đã tồn tại" ;
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setLastName(userDTO.getLastName());
        user.setFirstName(userDTO.getFirstName());
        user.setCode(userDTO.getCode());
        user.setImages(userDTO.getImages());
        user =  repository.save(user);

        List<Role> roles = new ArrayList();
        if(userDTO.getRoles().equals("USER")){
            Role role = new Role(1L,"USER");
            roles.add(role);
        }
        else if(userDTO.getRoles().equals("ADMIN")){
            Role role1 = new Role(1L,"USER");
            Role role2 = new Role(2L,"ADMIN");
            roles.add(role1);
            roles.add(role2);
        }else{
            Role role1 = new Role(1L,"USER");
            Role role2 = new Role(2L,"ADMIN");
            Role role3 = new Role(3L,"SUPER_ADMIN");
            roles.add(role1);
            roles.add(role2);
            roles.add(role3);
        }
//        "username": "duyvv",
//                "password": "duy2002",
        user.setRoles(roles);
        repository.save(user);
        return "Tạo tài khoản thành công";
    }

    @Override
    public List<UserDTO> getAllUser(String username) {
        List<UserDTO> userDTOs = new ArrayList();

        List<User> users = repository.findAll();

        for(User user : users){
            if(username != null && user.getUsername().equals(username)){
                continue ;
            }
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setUsername(user.getUsername());
            userDTO.setCode(user.getCode());
            userDTO.setStatus(user.getStatus().getValue());
            if(user.getImages() == null){
                userDTO.setImages(getImageBytesFromURL("http://localhost:9002/images/avatar_none.jpg"));
            }else{
                userDTO.setImages(user.getImages());
            }

            if(user.getRoles().contains(new Role(3L,"SUPER_ADMIN"))){
                userDTO.setRoles("QTV cao cấp");
            }else if(user.getRoles().contains(new Role(2L,"ADMIN"))){
                userDTO.setRoles("Quản trị viên");
            }else{
                userDTO.setRoles("Người dùng");
            }
            userDTOs.add(userDTO);
        }

        return userDTOs;
    }

    @Override
    public Object findUserDTOById(Long id) {
        User user = repository.findById(id).orElse(null);
        if(user == null){
            return "Không tìm thấy tài khoản người dùng này";
        }
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUsername(user.getUsername());
        userDTO.setCode(user.getCode());
        userDTO.setPassword(user.getPassword());
        userDTO.setStatus(user.getStatus().getValue());
        if(user.getImages() == null){
            userDTO.setImages(getImageBytesFromURL("http://localhost:9002/images/avatar_none.jpg"));
            System.out.println(userDTO.getImages());
        }else{
            userDTO.setImages(user.getImages());
        }
        if(user.getRoles().contains(new Role(3L,"SUPER_ADMIN"))){
            userDTO.setRoles("QTV cấp cao");
        }else if(user.getRoles().contains(new Role(2L,"ADMIN"))){
            userDTO.setRoles("Quản trị viên");
        }else{
            userDTO.setRoles("Người dùng");
        }

        return userDTO ;
    }

    @Override
    public String updateStatusUser(Long id, Integer action) {
        User user = repository.findById(id).orElse(null);
        if(user == null){
            return "Không tìm thấy tài khoản này";
        }
        user.setStatus(Status.getStatus(action));
        repository.save(user);
        return "Thành công";
    }
    public byte[] getImageBytesFromURL(String imageURL) {
        try {
            URL url = new URL(imageURL);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
