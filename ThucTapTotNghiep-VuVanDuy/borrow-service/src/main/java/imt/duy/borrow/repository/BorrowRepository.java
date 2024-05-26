package imt.duy.borrow.repository;

import com.imtTranding.core.entities.Status;
import imt.duy.borrow.entities.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    @Query("SELECT i FROM Borrow i " +
            "WHERE 1 = 1 " +
            "AND (:id IS NULL OR i.id =:id)")
    Borrow findBorrowById(Long id);

    List<Borrow> findBorrowByUserNameBorrrow(String username);

    @Query("SELECT i.userNameBorrrow FROM Borrow i " +
            "WHERE 1 = 1 " +
            "AND (:id IS NULL OR i.id =:id)")
    String getUsernameById(Long id);

    @Query("SELECT COUNT(i) FROM Borrow i " +
            "WHERE (:status IS NULL OR i.status = :status) "
    )
    Integer countByStatus(Status status);

    @Query("SELECT i FROM Borrow i " +
            "WHERE 1 = 1 " +
            "AND (:fromDate IS NULL OR i.createdAt >= :fromDate) "+
            "AND (:toDate IS NULL OR i.createdAt < :toDate) "+
            "AND (:status IS NULL OR i.status =: status)"
    )
    List<Borrow> findBorrowByRequest(LocalDateTime fromDate, LocalDateTime toDate, Status status);
}
