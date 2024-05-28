package imt.duy.borrow.service;

import imt.duy.borrow.dto.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service" , url = "${inventory-service.url}")
public interface InventoryClient {
    String root = "/api/v1/warehouse-service";
    @GetMapping({root+ "/isInStock"})
    InventoryResponse isInStock(@RequestParam Long bookCode, @RequestParam Integer quantity);

    @GetMapping({root+ "/borrowBook"})
    void borrowingBook(@RequestParam Long idBook, @RequestParam Integer quantity);

    @GetMapping({root+ "/returnBook"})
    void returningBook(@RequestParam Long idBook,@RequestParam Integer quantity);

}
