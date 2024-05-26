package duyvv.webadmin.controller;

import duyvv.webadmin.dto.*;
import duyvv.webadmin.service.WebAdminService;
import duyvv.webadmin.url.UrlService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
@RequestMapping("admin")
@AllArgsConstructor
public class WebController {
    @Autowired
    private WebAdminService webAdminService ;

    @Autowired
    private UserHolder userHolder;

    @GetMapping("/dashboard")
    public ModelAndView dashboardPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//        model.addAttribute("title", "Home Page");
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new ModelAndView("/login");
        }
        return webAdminService.dashboard(userHolder);
//        model.addAttribute("user", userHolder);
//        return "/dashboard";
    }
    @RequestMapping("/login")
    public ModelAndView loginPage() {
        ModelAndView model = new ModelAndView("page-login");
        return model;
    }
    @GetMapping("book")
    public ModelAndView bookPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new ModelAndView("login");
        }
        return webAdminService.getListBookInPageBook(userHolder);
    }
    @GetMapping("borrow")
    public ModelAndView borrowPage(@RequestParam(required = false) Boolean isFilter, @RequestParam(required = false) Integer status, @RequestParam(required = false) LocalDate fromDate, @RequestParam(required = false) LocalDate endDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new ModelAndView("login");
        }
        if(status == null && fromDate == null && endDate == null){
            isFilter = false ;
        }
        return webAdminService.getListBorrowInPageBook(userHolder, isFilter, (status == null) ? null: status, (fromDate == null) ? null: LocalDateTime.of(fromDate, LocalTime.of(0, 0, 0, 0)) , (endDate == null) ? null : LocalDateTime.of(endDate,LocalTime.of(0, 0, 0, 0)) );
    }
    @GetMapping("detail")
    public ModelAndView detailBook(@RequestParam(required = false) Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            new ModelAndView("login");
        }
        return webAdminService.getBookInPageDetail(id,userHolder);
    }
    @PostMapping("/addAndUpdateBook")
    public String addAndUpdateBook(@ModelAttribute(value = "book") RequestBook book, @RequestParam(value = "image",required = false) MultipartFile file, Model model, @RequestParam(value = "id",required = false) Long id) throws IOException {
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getBytes());
        String message = webAdminService.addAndUpdateBook(book, file.getBytes(), id);
        if(!message.isEmpty() && id == null){
            System.out.println(message);
            return "redirect:/admin/inventory";
        }
        if(!message.isEmpty() && id != null){
            System.out.println(message);
            return "redirect:/admin/book";
        }
        model.addAttribute("book", book);
        return "redirect:/admin/detail";
    }
    @GetMapping("/information-user")
    public ModelAndView userInfpPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new ModelAndView("login");
        }
        return webAdminService.userInfoPage(userHolder);
    }
    @GetMapping("/user")
    public ModelAndView userPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new ModelAndView("login");
        }
        return webAdminService.userPage(userHolder);
    }
    @GetMapping("/category")
    public ModelAndView categoryPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new ModelAndView("login");
        }
        return webAdminService.categoryPage(userHolder);
    }
    @GetMapping("/warehouse")
    public ModelAndView inventoryPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new ModelAndView("login");
        }
        return webAdminService.inventoryPage(userHolder);
    }

    @GetMapping("/detailUserBorrow")
    public ModelAndView detailUserBorrow(@RequestParam Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new ModelAndView("login");
        }
        return webAdminService.detailUserBorrow(userHolder,id);
    }
    @GetMapping("/createNewUser")
    public ModelAndView createNewUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new ModelAndView("login");
        }
        return webAdminService.createNewUser(userHolder);
    }
    @PostMapping("/createNewAccount")
    public ModelAndView createNewAccount(@ModelAttribute(value = "_user") UserDTO user, @RequestParam(value = "avatar",required = false) MultipartFile file, Model model) throws IOException {
        System.out.println(user.getRoles());
        System.out.println(file.getBytes());
        return webAdminService.createNewAccount(user, file.getBytes(),userHolder);
//        System.out.println(message);
//        if(!message.contains("thành công")){
//            model.addAttribute("message", message);
//            return "redirect:/admin/createNewUser";
//        }
//        model.addAttribute("message", message);
//        return "redirect:/admin/user";
    }





}
