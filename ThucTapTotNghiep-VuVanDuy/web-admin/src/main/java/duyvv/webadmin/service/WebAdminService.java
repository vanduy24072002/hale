package duyvv.webadmin.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import duyvv.webadmin.dto.*;
import duyvv.webadmin.url.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;

@Service

public class WebAdminService {

    private final RestTemplate restTemplate;

    @Autowired
    public WebAdminService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private UserHolder userHolder;


    public ModelAndView getListBookInPageBook(UserHolder userHolder){
        ModelAndView model = new ModelAndView("book");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<BookDTO[]> responseBooks = restTemplate.exchange(UrlService.API_GATEWAY_URL + UrlService.BOOK_SERVICE + "/getAll", HttpMethod.GET, entity, BookDTO[].class);

        model.addObject("books", responseBooks.getBody());
        model.addObject("user", userHolder);
        return model;
    }
    public ModelAndView userInfoPage(UserHolder userHolder) {
        ModelAndView model = new ModelAndView("page-user-info");
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + userHolder.getToken());
//        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
//        ResponseEntity<BookDTO[]> responseBooks = restTemplate.exchange(UrlService.API_GATEWAY_URL + UrlService.BOOK_SERVICE + "/getAll", HttpMethod.GET, entity, BookDTO[].class);
//
//        model.addObject("books", responseBooks.getBody());
        model.addObject("user", userHolder);
        return model;
    }
    public ModelAndView userPage(UserHolder userHolder) {
        ModelAndView model = new ModelAndView("page-user");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<UserDTO[]> responseBooks = restTemplate.exchange(UrlService.API_GATEWAY_URL + UrlService.USER_SERVICE + "/getAllUser", HttpMethod.GET, entity, UserDTO[].class);

        model.addObject("users", responseBooks.getBody());
        model.addObject("user", userHolder);
        return model;
    }



    public ModelAndView getBookInPageDetail(Long id , UserHolder userHolder){
        ModelAndView model = new ModelAndView("page-detail-book");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<CategoryDTO[]> categories = restTemplate.exchange(UrlService.API_GATEWAY_URL + UrlService.BOOK_SERVICE + "/findCategoryAll", HttpMethod.GET, entity, CategoryDTO[].class);
        if(id == null){
            BookDTO book = new BookDTO();
            book.setImages("https://th.bing.com/th/id/OIP.Uge8n3cdvDQTUusYkX_BwAHaFl?rs=1&pid=ImgDetMain");
            model.addObject("user", userHolder);
            model.addObject("book", book);
            model.addObject("categories", categories.getBody());
            return model;
        }


        ResponseEntity<BookDTO> response = restTemplate.exchange(UrlService.API_GATEWAY_URL + UrlService.BOOK_SERVICE + "/detail/" + id, HttpMethod.GET, entity, BookDTO.class);
        if(response.getBody().getImages() == null){
            response.getBody().setImages("https://th.bing.com/th/id/OIP.Uge8n3cdvDQTUusYkX_BwAHaFl?rs=1&pid=ImgDetMain");
        }
        else{
            response.getBody().setImages("data:image/jpeg;base64," + response.getBody().getImages());
        }
        model.addObject("user", userHolder);
        model.addObject("book", response.getBody());
        model.addObject("categories", categories.getBody());
        return model;
    }
    public Admin findByUsername(String username){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = UrlService.API_GATEWAY_URL + UrlService.USER_SERVICE + "/findByUsername/" + username;
        return restTemplate.getForObject(url, Admin.class);
    }
    public String token(String username){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(username, headers);
        return restTemplate.postForObject(UrlService.API_GATEWAY_URL + UrlService.USER_SERVICE + "/generateToken", requestEntity, String.class);
    }

    public String addAndUpdateBook(RequestBook book, byte[] file, Long id){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        if(file.length != 0){
            book.setImages(file);
        }
        System.out.println(book.getImages());
        HttpEntity<RequestBook> requestEntity = new HttpEntity<RequestBook>(book, headers);
        if(id == null){
            return restTemplate.postForObject(UrlService.API_GATEWAY_URL + UrlService.INVENTORY_SERVICE + "/import", requestEntity, String.class);
        }
        book.setId(id);
        requestEntity = new HttpEntity<RequestBook>(book, headers);


        return restTemplate.postForObject(UrlService.API_GATEWAY_URL + UrlService.BOOK_SERVICE + "/addOrUpdate", requestEntity, String.class);
    }
    public String updateInfoUser(Long id, ChangeInfoUser user, byte[] file, Boolean action) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(file.length > 0){
            user.setImages(file);
        }

        HttpEntity<ChangeInfoUser> requestEntity = new HttpEntity<ChangeInfoUser>(user, headers);
        String message =  restTemplate.postForObject(UrlService.API_GATEWAY_URL + UrlService.USER_SERVICE + "/updateInfoUser?id=" + id , requestEntity, String.class);
        if(action != false){
            loadUserHolder(user);
        }
        if(userHolder.getId() == id){
            loadUserHolder(user);
        }
        return message;
    }
    public void loadUserHolder(ChangeInfoUser user){
        userHolder.setFirstName(user.getFirstName());
        userHolder.setLastName(user.getLastName());
        if(user.getImages() !=null){
            userHolder.setImages(Base64.getEncoder().encodeToString(user.getImages()));
        }

    }


    public ModelAndView getListBorrowInPageBook(UserHolder userHolder, Boolean isFilter,Integer status, LocalDateTime fromDate, LocalDateTime endDate)  {
        ModelAndView model = new ModelAndView("borrow");
        if(isFilter){
            model.addObject("borrows", findBorrowByFilter(
                    (status == null) ? null : status,
                    (fromDate == null) ? null : fromDate,
                    (endDate == null) ? null : endDate
            ));
            model.addObject("user", userHolder);
            return model;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<BorrowDTO[]> response = restTemplate.exchange(UrlService.API_GATEWAY_URL + UrlService.BORROW_SERVICE + "/allBorrowBooks", HttpMethod.GET, entity, BorrowDTO[].class);
        model.addObject("borrows", response.getBody());
        model.addObject("user", userHolder);
        return model;
    }

    public String deleteBorrow(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Long> requestEntity = new HttpEntity<Long>(id, headers);
        return restTemplate.postForObject(UrlService.API_GATEWAY_URL + UrlService.BORROW_SERVICE + "/returnBorrow", requestEntity, String.class);
    }


    public String approveBorrrow(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Long> requestEntity = new HttpEntity<Long>(id, headers);
        return restTemplate.postForObject(UrlService.API_GATEWAY_URL + UrlService.BORROW_SERVICE + "/approveBorrow", requestEntity, String.class);
    }
    public String cancelBorrow(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Long> requestEntity = new HttpEntity<Long>(id, headers);
        return restTemplate.postForObject(UrlService.API_GATEWAY_URL + UrlService.BORROW_SERVICE + "/cancelBorrow", requestEntity, String.class);
    }

    public ModelAndView detailUserBorrow(UserHolder userHolder, Long id) {
        ModelAndView model = new ModelAndView("page-detail-userBorrow");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<UserDTO> responseBooks = restTemplate.exchange(UrlService.API_GATEWAY_URL + UrlService.USER_SERVICE + "/getUserById?id=" + id, HttpMethod.GET, entity, UserDTO.class);

        HttpEntity<String> entityBorrow = new HttpEntity<>("parameters", headers);
        ResponseEntity<BorrowDTO[]> responseBorrows = restTemplate.exchange(UrlService.API_GATEWAY_URL + UrlService.BORROW_SERVICE + "/getBorrowsById?id=" + id, HttpMethod.GET, entityBorrow, BorrowDTO[].class);


        model.addObject("_user", responseBooks.getBody());
        model.addObject("borrows", responseBorrows.getBody());
        model.addObject("user", userHolder);
        return model;
    }

    public ResponseEntity<String> updateStatusUser(Long id, String action) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        Integer idAction = 1;
        if(action.equals("lock")){
            idAction = -1 ;
        }else if(action.equals("active")){
            idAction = 1 ;
        }
        return restTemplate.exchange(UrlService.API_GATEWAY_URL + UrlService.USER_SERVICE + "/updateStatusUser?id=" + id + "&action=" + idAction, HttpMethod.GET, entity, String.class);
    }

    public ModelAndView inventoryPage(UserHolder userHolder) {
        ModelAndView model = new ModelAndView("page-inventory");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<InventoryDTO[]> responseInventorys = restTemplate.exchange(UrlService.API_GATEWAY_URL + UrlService.INVENTORY_SERVICE + "/getAllBookInventory", HttpMethod.GET, entity, InventoryDTO[].class);

        model.addObject("inventorys", responseInventorys.getBody());
        model.addObject("user", userHolder);
        return model;
    }

    public ResponseEntity<String> deleteInventory(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        return restTemplate.exchange(UrlService.API_GATEWAY_URL + UrlService.INVENTORY_SERVICE+ "/deleteInventory?id=" + id, HttpMethod.GET, entity, String.class);
    }

    public String addQuantity(Long id, Integer quantity) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        InventoryDTO inventory = new InventoryDTO();
        inventory.setIdBook(id);
        inventory.setQuantity(quantity);
        HttpEntity<InventoryDTO> requestEntity = new HttpEntity<InventoryDTO>(inventory, headers);
        return restTemplate.postForObject(UrlService.API_GATEWAY_URL + UrlService.INVENTORY_SERVICE + "/import", requestEntity, String.class);

    }

    public ResponseEntity<String> activeInventory(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        return restTemplate.exchange(UrlService.API_GATEWAY_URL + UrlService.INVENTORY_SERVICE+ "/activeInventory?id=" + id, HttpMethod.GET, entity, String.class);
    }

    public ModelAndView changePassword(Long id, ChangePassword password, UserHolder userHolder) {
        ModelAndView model = new ModelAndView("page-user-info");
        model.addObject("user", userHolder);
        if(password.getPasswordNew().equals(password.getPasswordOld())){
            String message = "Vui lòng nhập mật khẩu mới khác với mật khẩu cũ";
            System.out.println(message);
            model.addObject("message", message);
            return model ;

        }
        if(!password.getPasswordNew().equals(password.getPasswordConfirm())){
            String message = "Xác nhận lại mật khẩu không khớp";
            System.out.println(message);
            model.addObject("message", message);
            return model;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<UserDTO> user = restTemplate.exchange(UrlService.API_GATEWAY_URL + UrlService.USER_SERVICE+ "/getUserById?id=" + id, HttpMethod.GET, entity, UserDTO.class);
        if(!passwordEncoder.matches(password.getPasswordOld(),user.getBody().getPassword())){
            System.out.println(passwordEncoder.encode(user.getBody().getPassword()));
            System.out.println(password.getPasswordOld());
            String message = "Mật khẩu không chính xác";
            System.out.println(message);
            model.addObject("message", message);
            return model;
        }

        HttpEntity<String> requestEntity = new HttpEntity<String>(password.getPasswordNew(), headers);
        String message = restTemplate.postForObject(UrlService.API_GATEWAY_URL + UrlService.USER_SERVICE + "/changePassword?id="+ id, requestEntity, String.class);
        System.out.println(message);
        model.addObject("message", message);
        return model;
    }

    public ModelAndView createNewUser(UserHolder userHolder) {
        ModelAndView model = new ModelAndView("page-new-user");
        model.addObject("user", userHolder);
        model.addObject("_user",new UserDTO());
        return model;
    }

    public ModelAndView categoryPage(UserHolder userHolder) {
        ModelAndView model = new ModelAndView("page-category");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<CategoryDTO[]> responseCategories = restTemplate.exchange(UrlService.API_GATEWAY_URL + UrlService.BOOK_SERVICE + "/findCategoryAll", HttpMethod.GET, entity, CategoryDTO[].class);

        model.addObject("categories", responseCategories.getBody());
        model.addObject("user", userHolder);
        return model;
    }

    public ModelAndView createNewAccount(UserDTO user, byte[] file,UserHolder userHolder) {
        ModelAndView model = new ModelAndView("page-new-user");
        model.addObject("user", userHolder);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(file.length != 0){
            user.setImages(Base64.getEncoder().encodeToString(file));
        }
        HttpEntity<UserDTO> requestEntity = new HttpEntity<UserDTO>(user, headers);

        String message =  restTemplate.postForObject(UrlService.API_GATEWAY_URL + UrlService.USER_SERVICE + "/crateNewUser", requestEntity, String.class);
        if(!message.contains("thành công")){
            model.addObject("_user", user);
            model.addObject("message", message);
            return model ;
        }
        ModelAndView modelUser = new ModelAndView("page-user");
        modelUser.addObject("message", message);
        modelUser.addObject("user", userHolder);
        return modelUser ;
    }

    public ModelAndView dashboard(UserHolder userHolder) {
        ModelAndView model = new ModelAndView("dashboard");
        model.addObject("user", userHolder);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<ResponseReport> responseTotal = restTemplate.exchange(UrlService.API_GATEWAY_URL + UrlService.BORROW_SERVICE + "/report", HttpMethod.GET, entity, ResponseReport.class);

        model.addObject("dataTotalChart", responseTotal.getBody().getTotal());
        model.addObject("dataLastWeedChart", responseTotal.getBody().getLastWeek());
        return model ;
    }

    public BorrowDTO[] findBorrowByFilter(Integer status, LocalDateTime fromDate, LocalDateTime endDate) {
//        ModelAndView model = new ModelAndView("borrow");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userHolder.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        Filter filter = new Filter(fromDate,endDate,status);
        HttpEntity<Filter> entity = new HttpEntity<Filter>(filter, headers);
        BorrowDTO[] response = restTemplate.postForObject(UrlService.API_GATEWAY_URL + UrlService.BORROW_SERVICE + "/findByFilter", entity, BorrowDTO[].class);


//        model.addObject("borrows", response);
//        model.addObject("user", userHolder);
        return response ;
    }
}
