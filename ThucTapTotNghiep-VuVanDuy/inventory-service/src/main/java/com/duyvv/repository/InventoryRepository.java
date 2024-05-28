package com.duyvv.repository;

import com.duyvv.model.Inventory;
import com.imtTranding.core.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByIdBook(Long idBook);
    @Query("SELECT i FROM Inventory i " +
            "WHERE 1 = 1 " +
            "AND (:id IS NULL OR i.id =:id)")
    Inventory findInventoryById(Long id);

    @Query("SELECT i FROM Inventory i " +
            "WHERE 1 = 1 " +
            "AND (:fromDate IS NULL OR i.createdAt >= :fromDate) "+
            "AND (:toDate IS NULL OR i.createdAt < :toDate) "+
            "AND (:status IS NULL OR i.status =: status)"
    )
    List<Inventory> findWareHouseByRequest(LocalDateTime fromDate, LocalDateTime toDate, Status status);
}
