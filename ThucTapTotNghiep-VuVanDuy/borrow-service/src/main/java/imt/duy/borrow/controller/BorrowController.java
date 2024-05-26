package imt.duy.borrow.controller;

import com.imtTranding.core.entities.Status;
import imt.duy.borrow.dto.BorrowDTO;
import imt.duy.borrow.dto.BorrowRequest;
import imt.duy.borrow.dto.Filter;
import imt.duy.borrow.dto.ResponseReport;
import imt.duy.borrow.entities.Borrow;
import imt.duy.borrow.repository.BorrowRepository;
import imt.duy.borrow.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/borrow-service")
@CrossOrigin(origins = "http://localhost:9000")
@RequiredArgsConstructor
public class BorrowController {
    private final BorrowService borrowService;

//    private final BorrowRepository borrowRepository;

    @PostMapping("/borrow")
    public String borrowBook(@RequestBody BorrowRequest borrowRequest) {
        return borrowService.borrowBook(borrowRequest);
    }

    @PostMapping("/returnBorrow")
    public String returnBook(@RequestBody Long id) {
        return borrowService.returnBook(id);
    }
    @PostMapping("/approveBorrow")
    public String approveBorrow(@RequestBody Long id) {
        return borrowService.approveBorrow(id);
    }
    @PostMapping("/cancelBorrow")
    public String cancelBorrow(@RequestBody Long id) {
        return borrowService.cancelBorrow(id);
    }
    @GetMapping("/allBorrowBooks")
    public ResponseEntity<List<BorrowDTO>> allBorrowBooks() {
        return new ResponseEntity<List<BorrowDTO>>(borrowService.allBorrowBooks(), HttpStatus.OK);
    }
    @GetMapping("/getBorrowsById")
    public ResponseEntity<List<BorrowDTO>> getBorrowsById(@RequestParam Long id) {
        return new ResponseEntity<List<BorrowDTO>>(borrowService.getBorrowsById(id), HttpStatus.OK);
    }
    @GetMapping("/report")
    public ResponseEntity<ResponseReport> report() {
        return new ResponseEntity<ResponseReport>(borrowService.report(), HttpStatus.OK);
    }
    @PostMapping("/findByFilter")
    public ResponseEntity<List<BorrowDTO>> findBorrowByRequest(@RequestBody Filter filter) {
        return new ResponseEntity<List<BorrowDTO>>(borrowService.findByFilter(filter), HttpStatus.OK);
    }





}
