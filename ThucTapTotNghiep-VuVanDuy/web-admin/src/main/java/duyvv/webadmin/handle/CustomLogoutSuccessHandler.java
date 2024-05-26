package duyvv.webadmin.handle;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) throws IOException {
        // Thực hiện logic xử lý sau khi đăng xuất thành công ở đây, ví dụ chuyển hướng về trang chủ

        System.out.println("Xóa token thành công");
        response.sendRedirect("/admin/login");
    }
}

