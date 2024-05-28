package com.duyvv.service;

import com.duyvv.dto.*;
import com.duyvv.model.Inventory;
import com.duyvv.repository.InventoryRepository;
import com.imtTranding.core.entities.Status;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final BookClient bookClient;
    @Transactional(readOnly = true)
    @SneakyThrows
    public InventoryResponse isInStock(Long bookCode, Integer quantity) {
//        log.info("Checking Inventory");
        Inventory inventory = inventoryRepository.findByIdBook(bookCode);
        InventoryResponse inventoryResponse =  new InventoryResponse() ;
        inventoryResponse.setIdBook(inventory.getIdBook());
        inventoryResponse.setInStock(inventory.getRemain() >= quantity);
        return inventoryResponse ;
    }


    public String importBook(InventoryDTO inventoryDTO) {
        Inventory inventory = toMapper(inventoryDTO);
        if(inventoryDTO.getIdBook() !=null){
            inventory = inventoryRepository.findInventoryById(inventoryDTO.getIdBook());
            inventory.setRemain(inventory.getRemain() + inventoryDTO.getQuantity());
            inventory.setQuantity(inventory.getQuantity() + inventoryDTO.getQuantity());
            inventory.setStatus(Status.ACTIVE);
//            inventory.setUpdatedAt(LocalDateTime.now());
            inventoryRepository.save(inventory);
            String codeBook = bookClient.getBookById(inventoryDTO.getIdBook()).getCodeBook();
            return "Đã nhập thêm " + inventoryDTO.getQuantity()+ " quyển sách với mã sách : " + codeBook;
        }
//        System.out.println(inventoryDTO.getImages());
        AddRequest addRequest = new AddRequest();
        addRequest.setId(inventoryDTO.getIdBook());
        addRequest.setTitle(inventoryDTO.getTitle());
        addRequest.setAuthor(inventoryDTO.getAuthor());
        addRequest.setDescription(inventoryDTO.getDescription());
        addRequest.setImages(inventoryDTO.getImages());
        addRequest.setCategory(inventoryDTO.getCategory());
        Long idBook = bookClient.createAndUpdate(addRequest);
        inventory.setIdBook(idBook);
        inventory.setRemain(inventory.getQuantity());
        if(inventory.getRemain() == 0){
            // Đã hết
            inventory.setStatus(Status.LOCKED);

        }
        inventory.setPosition(inventory.getPosition());
        inventory.setBorrowed(0);

        inventoryRepository.save(inventory);
        return "Đã nhập mới sách thành công";
    }
    public Inventory toMapper(InventoryDTO inventoryDTO){
        Inventory inventory = new Inventory();
        inventory.setId(inventoryDTO.getId());
        inventory.setQuantity(inventoryDTO.getQuantity());
        inventory.setIdBook(inventoryDTO.getIdBook());
        inventory.setPosition(inventoryDTO.getPosition());
        return inventory ;
    }

    public void borrowingBook(Long idBook, Integer quantity) {
        Inventory inventory = inventoryRepository.findByIdBook(idBook);
        inventory.setRemain(inventory.getRemain() - quantity);
        inventory.setBorrowed(inventory.getQuantity() - inventory.getRemain());

        if(inventory.getRemain() == 0){
            // Đã hết
            inventory.setStatus(Status.LOCKED);

        }
        inventoryRepository.save(inventory);
    }

    public void returningBook(Long idBook, Integer quantity) {
        Inventory inventory = inventoryRepository.findByIdBook(idBook);
        inventory.setRemain(inventory.getRemain() + quantity);
        inventory.setBorrowed(inventory.getQuantity() - inventory.getRemain());
        inventory.setStatus(Status.ACTIVE);
        inventoryRepository.save(inventory);
    }

    public List<InventoryDTO> getAllBookInventory() {
        List<Inventory> inventorys = inventoryRepository.findAll();
        List<InventoryDTO> inventorysDTO = new ArrayList();
        for(Inventory inventory : inventorys){
            InventoryDTO inventoryDTO = new InventoryDTO();
            // Thông tin tồn kho
            inventoryDTO.setId(inventory.getId());
            inventoryDTO.setIdBook(inventory.getIdBook());
            inventoryDTO.setStatus(inventory.getStatus().getValue());
            inventoryDTO.setQuantity(inventory.getQuantity());
            inventoryDTO.setImportDate(inventory.getCreatedAt());
            inventoryDTO.setPosition(inventory.getPosition());
            inventoryDTO.setRemain(inventory.getRemain());
            inventoryDTO.setBorrowed(inventory.getBorrowed());
            // Thông tin sách
            BookResponse book = bookClient.getBookById(inventoryDTO.getIdBook());
            inventoryDTO.setCodeBook(book.getCodeBook());
            inventoryDTO.setTitle(book.getTitle());
            inventoryDTO.setImages(book.getImages());
            inventoryDTO.setDescription(book.getDescription());
            inventoryDTO.setAuthor(book.getAuthor());
            inventoryDTO.setCategory(book.getCategory());
            inventoryDTO.setCategoryName(book.getCategoryName());
            inventorysDTO.add(inventoryDTO);
        }
        return inventorysDTO ;
    }

    public String deleteInventory(Long id) {
        Inventory inventory = inventoryRepository.findInventoryById(id);
        if(inventory == null){
            return "Id không hợp lệ" ;
        }
        inventory.setStatus(Status.DELETED);
        inventory.setUpdatedAt(LocalDateTime.now());
        inventoryRepository.save(inventory);
        return "Xóa thành công" ;
    }


    public String activeInventory(Long id) {
        Inventory inventory = inventoryRepository.findInventoryById(id);
        if(inventory == null){
            return "Id không hợp lệ" ;
        }
        inventory.setStatus(Status.ACTIVE);
        inventory.setUpdatedAt(LocalDateTime.now());
        inventoryRepository.save(inventory);
        return "Kích hoạt lại thành công" ;
    }

    public List<InventoryDTO> findByFilter(Filter filter) {
        List<Inventory> inventorys = inventoryRepository.findWareHouseByRequest(
                filter.getFromDate(),
                filter.getEndDate(),
                (filter.getStatus()==null) ? null : Status.getStatus(filter.getStatus())
        );
        List<InventoryDTO> inventorysDTO = new ArrayList();
        for(Inventory inventory : inventorys){
            InventoryDTO inventoryDTO = new InventoryDTO();
            // Thông tin tồn kho
            inventoryDTO.setId(inventory.getId());
            inventoryDTO.setIdBook(inventory.getIdBook());
            inventoryDTO.setStatus(inventory.getStatus().getValue());
            inventoryDTO.setQuantity(inventory.getQuantity());
            inventoryDTO.setImportDate(inventory.getCreatedAt());
            inventoryDTO.setPosition(inventory.getPosition());
            inventoryDTO.setRemain(inventory.getRemain());
            inventoryDTO.setBorrowed(inventory.getBorrowed());
            // Thông tin sách
            BookResponse book = bookClient.getBookById(inventoryDTO.getIdBook());
            inventoryDTO.setCodeBook(book.getCodeBook());
            inventoryDTO.setTitle(book.getTitle());
            inventoryDTO.setImages(book.getImages());
            inventoryDTO.setDescription(book.getDescription());
            inventoryDTO.setAuthor(book.getAuthor());
            inventoryDTO.setCategory(book.getCategory());
            inventoryDTO.setCategoryName(book.getCategoryName());
            inventorysDTO.add(inventoryDTO);
        }
        return inventorysDTO ;
    }
}
