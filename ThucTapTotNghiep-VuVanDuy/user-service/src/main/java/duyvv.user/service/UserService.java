package duyvv.user.service;

import duyvv.user.dto.*;
import duyvv.user.reponsitory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AdminDTO findUserByUsernameIsNotLock(String username);
    AdminDTO findUserByUsername(String username);
    String updateInfoUser(ChangeInfoUser info,Long id);

    String changePassword(String password, Long id);

    String crateNewUser(UserDTO user);

    List<UserDTO> getAllUser(String username);

    Object findUserDTOById(Long id);

    String updateStatusUser(Long id, Integer action);
}
