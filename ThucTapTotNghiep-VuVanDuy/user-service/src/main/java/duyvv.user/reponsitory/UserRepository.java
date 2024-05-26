package duyvv.user.reponsitory;

import duyvv.user.dto.UserDTO;
import duyvv.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT u " +
            "FROM User u " +
            "WHERE 1 = 1 AND u.username = :username " +
            "AND u.status = 1")
    User findUserByUsernameIsNotLock(String username);
    User findUserByUsername(String username);
//
//    @Query(value = "SELECT new duyvv.user.dto.UserDTO (u.id, u.firstName, u.lastName, u.username, u.code, u.password, u.images, u.roles.name) " +
//            "FROM User u " +
//            "WHERE 1 = 1 ")
//    List<UserDTO> findUserDTOAll();

}
