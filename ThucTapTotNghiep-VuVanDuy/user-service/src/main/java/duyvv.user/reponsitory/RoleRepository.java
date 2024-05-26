package duyvv.user.reponsitory;

import duyvv.user.entities.Role;
import duyvv.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r.name FROM Role r " +
            "WHERE 1 = 1 " +
            "AND (:id IS NULL OR r.id =:id)")
    String getNameRoleById(Long id);
}
