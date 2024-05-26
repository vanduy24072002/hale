package com.duyvv.repository;

import com.duyvv.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByIdBook(Long idBook);
    @Query("SELECT i FROM Inventory i " +
            "WHERE 1 = 1 " +
            "AND (:id IS NULL OR i.id =:id)")
    Inventory findInventoryById(Long id);
}
