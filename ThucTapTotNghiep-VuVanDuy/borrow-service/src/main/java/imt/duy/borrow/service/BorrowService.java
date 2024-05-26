package imt.duy.borrow.service;

import com.imtTranding.core.entities.Status;
import java.time.temporal.ChronoUnit;
import imt.duy.borrow.dto.*;
import imt.duy.borrow.entities.Borrow;
import imt.duy.borrow.repository.BorrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BorrowRepository borrowRepository;

    private final InventoryClient inventoryClient ;
    private final BookClient bookClient ;
    private final UserClient userClient ;

    public String borrowBook(BorrowRequest borrowRequest) {
        if(borrowRequest.getQuantity() > 5){
            return "Số lượng vượt quá mức quy định" ;
        }
        if(!inventoryClient.isInStock(borrowRequest.getIdBook(), borrowRequest.getQuantity()).isInStock()){
            return "Số lượng sách tồn kho không đủ" ;
        }
        Borrow borrow = new Borrow();
        borrow.setOrderBookNumber(UUID.randomUUID().toString());
        borrow.setIdBook(borrowRequest.getIdBook());
        borrow.setStatus(Status.NOT_APPROVED);
        borrow.setUserNameBorrrow(borrowRequest.getUsername());
        borrow.setQuantity(borrowRequest.getQuantity());
        borrow.setNumberDayBorrow(borrowRequest.getNumberDayBorrow());
        inventoryClient.borrowingBook(borrowRequest.getIdBook(), borrowRequest.getQuantity());
        borrowRepository.save(borrow);
        return "Mượn thành công";
    }
    public String returnBook(Long id) {
        if(id == null){
            return "Trả thất bại không tìm thấy id sách";
        }
        Borrow borrow = borrowRepository.findBorrowById(id);
        borrow.setStatus(Status.DELETED);
        borrow.setUpdatedAt(LocalDateTime.now());
        inventoryClient.returningBook(borrow.getIdBook(),borrow.getQuantity() );
        borrowRepository.save(borrow);
        return "Trả thành công";
    }

    public List<BorrowDTO> allBorrowBooks() {
        List<BorrowDTO> borrowDTOs = new ArrayList();
        List<Borrow> borrows = borrowRepository.findAll();
        for(Borrow borrow : borrows){
            BorrowDTO borrowDTO = new BorrowDTO();
            borrowDTO.setId(borrow.getId());
            borrowDTO.setNumberDayBorrow(borrow.getNumberDayBorrow());
            borrowDTO.setQuantity(borrow.getQuantity());
            borrowDTO.setIdBook(borrow.getIdBook());
            borrowDTO.setStatus(borrow.getStatus().getValue());
            borrowDTO.setBorrowDate(borrow.getCreatedAt());
            borrowDTO.setOrderBookNumber(borrow.getOrderBookNumber());
            // Thông tin sách
            BookDTO book = bookClient.getBookById(borrow.getIdBook());
            borrowDTO.setIdBook(book.getId());
            borrowDTO.setAuthor(book.getAuthor());
            borrowDTO.setTitle(book.getTitle());
            borrowDTO.setDescription(book.getDescription());
            borrowDTO.setCodeBook(book.getCodeBook());
            borrowDTO.setImages(Base64.getEncoder().encodeToString(book.getImages()));
            borrowDTO.setCategory(book.getCategory());
            // Thông tin người mượn sách
            UserDTO user = userClient.findUserByUsername(borrow.getUserNameBorrrow());
            borrowDTO.setCode(user.getCode());
            borrowDTO.setFirstName(user.getFirstName());
            borrowDTO.setLastName(user.getLastName());
            // Thêm vào
            borrowDTOs.add(borrowDTO);
        }
        return borrowDTOs ;

    }


    public String approveBorrow(Long id) {
        if(id == null){
            return "Không thể duyệt. Không tìm thấy id sách";
        }
        Borrow borrow = borrowRepository.findBorrowById(id);
        borrow.setStatus(Status.ACTIVE);
//        inventoryClient.returningBook(borrow.getIdBook(),borrow.getQuantity());
        borrowRepository.save(borrow);
        return "Đã duyệt";
    }
    public String cancelBorrow(Long id) {
        if(id == null){
            return "Không thể duyệt. Không tìm thấy id sách";
        }
        Borrow borrow = borrowRepository.findBorrowById(id);
        inventoryClient.returningBook(borrow.getIdBook(),borrow.getQuantity() );
        borrow.setStatus(Status.CANCEL);
        borrowRepository.save(borrow);
        return "Đã hủy";
    }

    public List<BorrowDTO> getBorrowsById(Long id) {

        String username = borrowRepository.getUsernameById(id);

        List<Borrow> borrows = borrowRepository.findBorrowByUserNameBorrrow(username);
        List<BorrowDTO> borrowDTOs = new ArrayList();
        for(Borrow borrow : borrows){
            BorrowDTO borrowDTO = new BorrowDTO();
            borrowDTO.setId(borrow.getId());
            borrowDTO.setNumberDayBorrow(borrow.getNumberDayBorrow());
            borrowDTO.setQuantity(borrow.getQuantity());
            borrowDTO.setIdBook(borrow.getIdBook());
            borrowDTO.setStatus(borrow.getStatus().getValue());
            borrowDTO.setBorrowDate(borrow.getCreatedAt());
            borrowDTO.setOrderBookNumber(borrow.getOrderBookNumber());
            // Thông tin sách
            BookDTO book = bookClient.getBookById(borrow.getIdBook());
            borrowDTO.setIdBook(book.getId());
            borrowDTO.setAuthor(book.getAuthor());
            borrowDTO.setTitle(book.getTitle());
            borrowDTO.setDescription(book.getDescription());
            borrowDTO.setCodeBook(book.getCodeBook());
            borrowDTO.setImages(Base64.getEncoder().encodeToString(book.getImages()));
            borrowDTO.setCategory(book.getCategory());
            // Thông tin người mượn sách
            UserDTO user = userClient.findUserByUsername(borrow.getUserNameBorrrow());
            borrowDTO.setCode(user.getCode());
            borrowDTO.setFirstName(user.getFirstName());
            borrowDTO.setLastName(user.getLastName());
            // Thêm vào
            borrowDTOs.add(borrowDTO);
        }
        return borrowDTOs ;
    }

    public ResponseReport report() {
        ResponseReport data = new ResponseReport(new ArrayList(),new ArrayList());


        LocalDateTime today = LocalDateTime.now();
        LocalDateTime previousMonday = today.with(TemporalAdjusters.previous(DayOfWeek.MONDAY)).withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        if(ChronoUnit.DAYS.between(today, previousMonday) < 7){
            previousMonday = previousMonday.with(TemporalAdjusters.previous(DayOfWeek.MONDAY)).withHour(0)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);
        }

        LocalDateTime previousSunday = today.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
//        System.out.println(previousMonday);
//        System.out.println(previousSunday);
        // Thống kê theo tuần
        LocalDateTime currentDay = previousMonday;
        while (!currentDay.isAfter(previousSunday)) {
            data.getLastWeek().add(

                    borrowRepository.findBorrowByRequest(
                            currentDay,
                            currentDay.plusDays(1),
                            null
                    ).toArray().length
            );

            currentDay = currentDay.plusDays(1);


        }


        // Thống kê tổng
        data.getTotal().add(borrowRepository.countByStatus(Status.ACTIVE));
        data.getTotal().add(borrowRepository.countByStatus(Status.DELETED));
        data.getTotal().add(borrowRepository.countByStatus(Status.NOT_APPROVED));
        data.getTotal().add(borrowRepository.countByStatus(Status.CANCEL));

        return data;
    }

    public List<BorrowDTO> findByFilter(Filter filter) {

        List<Borrow> borrows = borrowRepository.findBorrowByRequest(
                filter.getFromDate(),
                filter.getEndDate(),
                (filter.getStatus()==null) ? null : Status.getStatus(filter.getStatus())
        );
        List<BorrowDTO> borrowDTOs = new ArrayList();
        for(Borrow borrow : borrows){
            BorrowDTO borrowDTO = new BorrowDTO();
            borrowDTO.setId(borrow.getId());
            borrowDTO.setNumberDayBorrow(borrow.getNumberDayBorrow());
            borrowDTO.setQuantity(borrow.getQuantity());
            borrowDTO.setIdBook(borrow.getIdBook());
            borrowDTO.setStatus(borrow.getStatus().getValue());
            borrowDTO.setBorrowDate(borrow.getCreatedAt());
            borrowDTO.setOrderBookNumber(borrow.getOrderBookNumber());
            // Thông tin sách
            BookDTO book = bookClient.getBookById(borrow.getIdBook());
            borrowDTO.setIdBook(book.getId());
            borrowDTO.setAuthor(book.getAuthor());
            borrowDTO.setTitle(book.getTitle());
            borrowDTO.setDescription(book.getDescription());
            borrowDTO.setCodeBook(book.getCodeBook());
            borrowDTO.setImages(Base64.getEncoder().encodeToString(book.getImages()));
            borrowDTO.setCategory(book.getCategory());
            // Thông tin người mượn sách
            UserDTO user = userClient.findUserByUsername(borrow.getUserNameBorrrow());
            borrowDTO.setCode(user.getCode());
            borrowDTO.setFirstName(user.getFirstName());
            borrowDTO.setLastName(user.getLastName());
            // Thêm vào
            borrowDTOs.add(borrowDTO);
        }
        return borrowDTOs ;
    }
}
