package imt.duy.borrow.service;

import imt.duy.borrow.dto.BookDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "book-service" , url = "${book-service.url}")
public interface BookClient {
    String root = "/api/v1/book-service";

    @GetMapping({root + "/detail/{id}"})
    BookDTO getBookById(@PathVariable Long id);
}
