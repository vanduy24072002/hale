package duyvv.webadmin.controller;

import duyvv.webadmin.dto.ChangeInfoUser;
import duyvv.webadmin.dto.ChangePassword;
import duyvv.webadmin.dto.UserHolder;
import duyvv.webadmin.service.WebAdminService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.awt.print.Book;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("admin")
public class HandleController {
    @Autowired
    private WebAdminService webAdminService ;
    @Autowired
    private UserHolder userHolder;

    @GetMapping("/deleteBorrow")
    public String delete(@RequestParam Long id,  Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        String message = webAdminService.deleteBorrow(id);
        System.out.println(message);
        model.addAttribute("message", message);
        return "redirect:/admin/borrow";
    }
    @GetMapping("/approveBorrow")
    public String approveBorrrow(@RequestParam Long id,  Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        String message = webAdminService.approveBorrrow(id);
        System.out.println(message);
        model.addAttribute("message", message);
        return "redirect:/admin/borrow";
    }
    @GetMapping("/cancelBorrow")
    public String cancelBorrow(@RequestParam Long id,  Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        String message = webAdminService.cancelBorrow(id);
        System.out.println(message);
        model.addAttribute("message", message);
        return "redirect:/admin/borrow";
    }
    @PostMapping("/updateInfoUser")
    public String updateInfoUser(@RequestParam Long id,@RequestParam(required = false) Boolean action, @ModelAttribute ChangeInfoUser user, @RequestParam(value = "image",required = false) MultipartFile file,Model model) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        byte[] dataFile = new byte[0];
        if(file != null){
            dataFile = file.getBytes();
        }

        String message = webAdminService.updateInfoUser(id,user, dataFile,action);
        System.out.println(message);
        model.addAttribute("message", message);
        return "redirect:/admin/user";
    }
    @PostMapping("/changePassword")
    public ModelAndView changePassword(@RequestParam Long id, @ModelAttribute ChangePassword password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new ModelAndView("login");
        }
        System.out.println(password);
        return webAdminService.changePassword(id,password, userHolder);
    }
    @GetMapping("/updateStatusUser")
    public String updateStatusUser(@RequestParam Long id,  Model model, @RequestParam String action) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        ResponseEntity<String> message = webAdminService.updateStatusUser(id, action);
        System.out.println(message.getBody());
        model.addAttribute("message", message.getBody());
        return "redirect:/admin/user";
    }
    @GetMapping("/deleteInventory")
    public String deleteInventory(@RequestParam Long id,  Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        ResponseEntity<String> message = webAdminService.deleteInventory(id);
        System.out.println(message.getBody());
        model.addAttribute("message", message.getBody());
        return "redirect:/admin/warehouse";
    }

    @GetMapping("/activeInventory")
    public String activeInventory(@RequestParam Long id,  Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        ResponseEntity<String> message = webAdminService.activeInventory(id);
        System.out.println(message.getBody());
        model.addAttribute("message", message.getBody());
        return "redirect:/admin/warehouse";
    }

    @GetMapping("/addQuantity")
    public String addQuantity(@RequestParam Long id, @RequestParam Integer quantity, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        String message = webAdminService.addQuantity(id,quantity);
        System.out.println(message);
        model.addAttribute("message", message);
        return "redirect:/admin/warehouse";
    }
    @GetMapping("/findByFilter")
    public String findByFilter(@RequestParam(required = false) Integer status, @RequestParam(required = false) LocalDate fromDate, @RequestParam(required = false) LocalDate toDate) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return  "login";
        }

        return "redirect:/admin/borrow?isFilter=true" + ((status==null) ? "" : "&status="+ status) + ((fromDate==null) ? "" : "&fromDate=" + fromDate) +  ((toDate==null) ? "" : "&endDate=" + toDate);


    }



}
