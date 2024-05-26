package duyvv.webadmin.handle;


import java.io.IOException;

import duyvv.webadmin.dto.Admin;
import duyvv.webadmin.dto.UserDTO;
import duyvv.webadmin.dto.UserHolder;
import duyvv.webadmin.service.WebAdminService;
import duyvv.webadmin.url.UrlService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private final UserHolder userHolder;
    @Autowired
    WebAdminService webAdminService;

    public CustomAuthenticationSuccessHandler(UserHolder userHolder) {
        this.userHolder = userHolder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        User user = (User) authentication.getPrincipal() ;

        Admin admin = webAdminService.findByUsername(user.getUsername());
        boolean isAdmin = admin.getRoles().stream()
                .anyMatch(role -> role.equals("ADMIN"));
        if(admin.getStatus() != 1){
            String errorMessage = "Tài khoản của bạn đã bị khóa";
            request.getSession().setAttribute("errorMessage", errorMessage);
            response.sendRedirect("/admin/login");
        }
        else if (isAdmin) {

            // Nếu là Admin, tạo token và chuyển hướng tới trang dashboard
            userHolder.setToken(webAdminService.token(user.getUsername()));

            userHolder.setFirstName(admin.getFirstName());
            userHolder.setLastName(admin.getLastName());
            userHolder.setUsername(admin.getUsername());
            userHolder.setId(admin.getId());
            userHolder.setImages(admin.getImages());
            if(admin.getRoles().contains("SUPER_ADMIN")){
                userHolder.setRole("SUPER_ADMIN");
            }else if(admin.getRoles().contains("ADMIN")){
                userHolder.setRole("ADMIN");
            }else{
                userHolder.setRole("USER");
            }
            System.out.println("Đăng nhập thành công");
            response.sendRedirect("/admin/dashboard");
        }else {
            // Nếu không phải là Admin, chuyển hướng tới trang lỗi
            String errorMessage = "Bạn không có quyền truy cập";
            request.getSession().setAttribute("errorMessage", errorMessage);
            response.sendRedirect("/admin/login");
        }
    }
}
