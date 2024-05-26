package com.imtTranding.ApiGateway.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/api/v1/user-service/findByUsername",
            "/api/v1/user-service/findUserByUsernameIsNotLock",
            "/api/v1/user-service/generateToken",
            "/swagger-ui/index.html",
            "/swagger-ui.html",
            "/swagger-ui/*",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/user/v3/api-docs",
            "/book/v3/api-docs",
            "/inventory/v3/api-docs",
            "/borrow/v3/api-docs"



    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));


}