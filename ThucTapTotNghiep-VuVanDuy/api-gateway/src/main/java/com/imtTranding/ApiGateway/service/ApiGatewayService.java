package com.imtTranding.ApiGateway.service;

import com.imtTranding.ApiGateway.dto.AdminDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
//@FeignClient("user-service")
public class ApiGatewayService {
    @Autowired
    private WebClient.Builder webClientBuilder;

//    public AdminDTO findByUsername(String username){
//        String url = "http://localhost:9002/api/v1/user-service/findByUsername/" + username;
//        return webClientBuilder.build()
//                .get()
//                .uri(url)
//                .retrieve()
//                .bodyToMono(AdminDTO.class).block();
//
//    }

//    @PostMapping("/findByUsername")
//    AdminDTO findUserByUsername(@RequestBody String username);
    public AdminDTO findByUsername(String username){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://localhost:9002/api/v1/user-service/findByUsername/{username}", AdminDTO.class, username);    }
//    @PostMapping("/jwtAuthentication")
//    Boolean jwtAuthentication(@RequestBody String username);
}
