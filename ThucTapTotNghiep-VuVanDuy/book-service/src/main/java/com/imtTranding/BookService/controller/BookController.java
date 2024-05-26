package com.imtTranding.BookService.controller;

import com.imtTranding.BookService.dto.AddRequest;
import com.imtTranding.BookService.dto.BookResponse;
import com.imtTranding.BookService.entities.Book;
import com.imtTranding.BookService.responsitory.BookResponsitory;
import com.imtTranding.BookService.responsitory.CategoryRepository;
import com.imtTranding.BookService.service.InventoryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;


@RestController
@RequestMapping("/api/v1/book-service")
@CrossOrigin(origins = "http://localhost:9000")
@RequiredArgsConstructor
public class BookController {

    private final BookResponsitory responsitory;
    private final CategoryRepository categoryRepository ;
    public final InventoryClient inventoryClient ;

    @PostMapping("/addOrUpdate")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createAndUpdate(@RequestBody AddRequest request) throws IOException {
        System.out.println(request.getTitle() + "\n" + request.getImages() + "\n" + request.getCategory());
        Book book = new Book();
        if(request.getId() != null){
            book = responsitory.findById(request.getId()).orElse(new Book()) ;
        }
        book.setTitle(request.getTitle());


        book.setCategory(categoryRepository.getCategoryById(request.getCategory()));
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setDescription(request.getDescription());
        System.out.println(request.getImages());
        if(request.getImages()!=null){
            book.setImages(request.getImages());
        }

        responsitory.save(book);
        String formattedNumber = String.format("%06d", book.getId());
        book.setCodeBook(categoryRepository.getNameShortById(request.getCategory()) + "_PYU_" + formattedNumber);
        responsitory.save(book);
//        InventoryDTO inventoryDTO = new InventoryDTO();
//        inventoryDTO.setBookCode(book.getId());
//        inventoryClient.addUpdateDeleteInventory(inventoryDTO);
        return book.getId() ;
    }
//    @PostMapping("/deleteBook")
//    @ResponseStatus(HttpStatus.CREATED)
//    public String delete(@RequestBody Long id) {
//
//
//        Book book = responsitory.findById(id).orElse(new Book()) ;
//        book.setStatus(com.imtTranding.core.entities.Status.DELETED);
//        book.setUpdatedAt(LocalDateTime.now());
//        responsitory.save(book);
//        return "Thành công" ;
//    }

//    @GetMapping
//    public ResponseEntity<byte[]> downloadFile(@RequestParam Long id) {
//        Book book = responsitory.findById(id).orElse(null);
//            byte[] fileData = book.getImages();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.IMAGE_JPEG);
//            headers.setContentDispositionFormData("attachment", book.getTitle()+".jpg");
//            return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
//
//    }
    @GetMapping("/getAll")
    public ResponseEntity<List<BookResponse>> listAllBook() {
        List<BookResponse> books = responsitory.findBookRepositoryAll();
        return new ResponseEntity<List<BookResponse>>(books, HttpStatus.OK);

    }

    @GetMapping("/detail/{id}")
    public BookResponse getBookById(@PathVariable Long id) {
        Book book = responsitory.findById(id).orElse(new Book());
        BookResponse bookResponse = new BookResponse();
        bookResponse.setId(book.getId());
        bookResponse.setCodeBook(book.getCodeBook());
        bookResponse.setAuthor(book.getAuthor());
        bookResponse.setCategory(book.getCategory().getId());
        bookResponse.setCategoryName(book.getCategory().getName());
        bookResponse.setTitle(book.getTitle());
        bookResponse.setImages(book.getImages());
        bookResponse.setDescription(book.getDescription());



        return bookResponse;

    }


}
