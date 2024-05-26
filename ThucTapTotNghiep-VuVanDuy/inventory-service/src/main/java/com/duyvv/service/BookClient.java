package com.duyvv.service;

import com.duyvv.dto.AddRequest;
import com.duyvv.dto.BookResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "book-service", url = "${book-service.url}")
public interface BookClient {
    String root = "/api/v1/book-service";
    @PostMapping({root+ "/addOrUpdate"})
    Long createAndUpdate(@RequestBody AddRequest request);

    @GetMapping({root+ "/detail/{id}"})
    BookResponse getBookById(@PathVariable Long id);

}
