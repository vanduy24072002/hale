package com.imtTranding.BookService.service;

import com.imtTranding.BookService.dto.InventoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service" , url = "${inventory-service.url}")
public interface InventoryClient {

}
