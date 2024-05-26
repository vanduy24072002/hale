package com.imtTranding.ApiGateway.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.imtTranding.ApiGateway.dto.AdminDTO;
import com.imtTranding.ApiGateway.exception.ErrorAuth;
import com.imtTranding.ApiGateway.service.ApiGatewayService;
import com.imtTranding.ApiGateway.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouterValidator validator;
    @Autowired
    private JwtService jwtUtil;

    @Autowired
    ApiGatewayService apiGatewayService;

    public AuthenticationFilter() {
        super(Config.class);
    }

    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (this.validator.isSecured.test(exchange.getRequest())) {
                Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
                if (!exchange.getRequest().getHeaders().containsKey("Authorization")) {
                    return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
                }

                String authHeader = (String)exchange.getRequest().getHeaders().get("Authorization").get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                System.out.println(authHeader);
                try {
                    String username = jwtUtil.extractUsername(authHeader);
                    System.out.println(username);
                    if (username != null) {
                        AdminDTO admin = apiGatewayService.findByUsername(username);

                        System.out.println(admin.getUsername());
                        if (!jwtUtil.isTokenValid(authHeader, admin.getUsername())) {
                            return onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
                        }
                    }
                } catch (Exception var7) {
                    String errorMessage = "Xác thực không thành công: " + var7.getMessage();
                    log.error(errorMessage);
                    return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
                }
            }

            return chain.filter(exchange);
        };
    }

    public Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ObjectMapper objectMapper = new ObjectMapper();
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ErrorAuth errAuth = new ErrorAuth(httpStatus.value(), err);

        try {
            byte[] jsonData = objectMapper.writeValueAsBytes(errAuth);
            DataBuffer buffer = response.bufferFactory().wrap(jsonData);
            return response.writeWith(Mono.just(buffer));
        } catch (Exception var9) {
            return response.setComplete();
        }
    }

    public static class Config {
        public Config() {
        }
    }
}
