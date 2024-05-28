package com.duyvv.controller;

import com.duyvv.dto.Filter;
import com.duyvv.dto.InventoryDTO;
import com.duyvv.dto.InventoryResponse;
import com.duyvv.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/warehouse-service")
@CrossOrigin(origins = "http://localhost:9000")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;


    @GetMapping("/isInStock")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponse isInStock(@RequestParam Long bookCode, @RequestParam Integer quantity) {
//        log.info("Received inventory check request for bookCode: {}", bookCode);
        return inventoryService.isInStock(bookCode, quantity);
    }
//    @PostMapping()
//    public InventoryDTO addInventory(@RequestBody InventoryDTO inventory){
//        return inventoryService.addInventory(inventory);
//    }
    @PostMapping("/import")
    public String importBook(@RequestBody InventoryDTO inventory){
        return inventoryService.importBook(inventory);
    }
    @GetMapping("/borrowBook")
    public void borrowingBook(@RequestParam Long idBook, @RequestParam Integer quantity){
        inventoryService.borrowingBook(idBook, quantity);
    }
    @GetMapping("/returnBook")
    public void returningBook(@RequestParam Long idBook,@RequestParam Integer quantity){
        inventoryService.returningBook(idBook, quantity);
    }
    @GetMapping("/getAllBookInventory")
    public List<InventoryDTO> getAllBookInventory(){
        return inventoryService.getAllBookInventory();
    }
    @GetMapping("/deleteInventory")
    public String deleteInventory(@RequestParam Long id){
        return inventoryService.deleteInventory(id);
    }
    @GetMapping("/activeInventory")
    public String activeInventory(@RequestParam Long id){
        return inventoryService.activeInventory(id);
    }
    @PostMapping("/findByFilter")
    public ResponseEntity<List<InventoryDTO>> findBorrowByRequest(@RequestBody Filter filter) {
        return new ResponseEntity<List<InventoryDTO>>(inventoryService.findByFilter(filter), HttpStatus.OK);
    }
}

