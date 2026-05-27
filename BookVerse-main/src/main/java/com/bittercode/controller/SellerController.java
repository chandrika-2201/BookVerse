package com.bittercode.controller;

import com.bittercode.model.*;
import com.bittercode.service.BookService;
import com.bittercode.service.UserService;
import com.bittercode.repository.OrderRepository;
import com.bittercode.repository.UserRepository;
import com.bittercode.repository.BookRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class SellerController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/adminlog")
    public String loginAdmin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session) {
        
        User user = userService.login(UserRole.SELLER, username, password);
        if (user != null) {
            session.setAttribute("user", user);
            session.setAttribute("usertype", "SELLER");
            return "redirect:/SellerHome";
        }
        return "redirect:/SellerLogin?error=true";
    }

    @GetMapping("/storebooks")
    public String storeBooks(HttpSession session, Model model) {
        if (session.getAttribute("user") == null || !"SELLER".equals(session.getAttribute("usertype"))) {
            return "redirect:/SellerLogin";
        }
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "storebooks";
    }

    @GetMapping("/addbook")
    public String addbookForm(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/SellerLogin";
        }
        return "addbook";
    }

    @PostMapping("/addbook")
    public String processAddBook(
            @RequestParam("barcode") String barcode,
            @RequestParam("name") String name,
            @RequestParam("author") String author,
            @RequestParam("price") double price,
            @RequestParam("quantity") int quantity,
            HttpSession session,
            Model model) {

        if (session.getAttribute("user") == null) {
            return "redirect:/SellerLogin";
        }

        Book book = new Book(barcode, name, author, price, quantity);
        String status = bookService.addBook(book);

        if ("SUCCESS".equalsIgnoreCase(status)) {
            return "redirect:/storebooks";
        } else {
            model.addAttribute("error", status);
            return "addbook";
        }
    }

    @RequestMapping(value = "/removebook", method = {RequestMethod.GET, RequestMethod.POST})
    public String removeBook(
            @RequestParam(value = "bookId", required = false) String bookId,
            HttpSession session,
            Model model) {

        if (session.getAttribute("user") == null || !"SELLER".equals(session.getAttribute("usertype"))) {
            return "redirect:/SellerLogin";
        }

        if (bookId != null && !bookId.trim().isEmpty()) {
            String status = bookService.deleteBookById(bookId.trim());
            model.addAttribute("status", status);
            model.addAttribute("bookId", bookId);
        }
        return "removebook";
    }

    @PostMapping("/updatebook")
    public String showOrProcessUpdate(
            @RequestParam("barcode") String barcode,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "quantity", required = false) Integer quantity,
            @RequestParam(value = "updateFormSubmitted", required = false) String updateFormSubmitted,
            HttpSession session,
            Model model) {

        if (session.getAttribute("user") == null || !"SELLER".equals(session.getAttribute("usertype"))) {
            return "redirect:/SellerLogin";
        }

        if (updateFormSubmitted != null) {
            Book book = new Book(barcode, name, author, price, quantity);
            bookService.updateBook(book);
            return "redirect:/storebooks";
        } else {
            Book book = bookService.getBookById(barcode);
            if (book == null) {
                return "redirect:/storebooks";
            }
            model.addAttribute("book", book);
            return "updatebook";
        }
    }

    @GetMapping("/SellerHome")
    public String sellerHome(HttpSession session, Model model) {
        if (session.getAttribute("user") == null || !"SELLER".equals(session.getAttribute("usertype"))) {
            return "redirect:/SellerLogin";
        }

        List<Book> books = bookService.getAllBooks();
        int totalBookTitles = books.size();
        int totalStockQuantity = books.stream().mapToInt(Book::getQuantity).sum();

        long totalCustomers = userRepository.findAll().stream()
                .filter(u -> UserRole.CUSTOMER == u.getRole())
                .count();

        List<Order> allOrders = orderRepository.findAll();
        List<Order> recentOrders = allOrders.stream()
                .sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))
                .toList();
        
        int totalOrdersCount = allOrders.size();
        double totalRevenue = allOrders.stream().mapToDouble(Order::getSubtotal).sum();

        List<Book> lowStockBooks = books.stream()
                .filter(b -> b.getQuantity() < 10)
                .toList();

        model.addAttribute("totalBookTitles", totalBookTitles);
        model.addAttribute("totalStockQuantity", totalStockQuantity);
        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("totalOrders", totalOrdersCount);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("recentOrders", recentOrders.stream().limit(10).toList());
        model.addAttribute("lowStockBooks", lowStockBooks);

        return "SellerHome";
    }
}
