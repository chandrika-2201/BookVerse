package com.bittercode.controller;

import com.bittercode.model.*;
import com.bittercode.repository.OrderRepository;
import com.bittercode.service.BookService;
import com.bittercode.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class CustomerController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/userreg")
    public String registerCustomer(
            @RequestParam("mailid") String mailId,
            @RequestParam("password") String password,
            @RequestParam("firstname") String firstName,
            @RequestParam("lastname") String lastName,
            @RequestParam("address") String address,
            @RequestParam("phone") Long phone,
            Model model) {
        
        User user = new User(mailId, password, firstName, lastName, phone, address, mailId, UserRole.CUSTOMER);
        String status = userService.register(UserRole.CUSTOMER, user);
        
        if ("SUCCESS".equalsIgnoreCase(status)) {
            model.addAttribute("message", "User Registered Successfully");
            return "CustomerLogin";
        } else {
            model.addAttribute("error", "Registration Failed: " + status);
            return "CustomerRegister";
        }
    }

    @PostMapping("/userlog")
    public String loginCustomer(
            @RequestParam("emailId") String emailId,
            @RequestParam("password") String password,
            HttpSession session) {
        
        User user = userService.login(UserRole.CUSTOMER, emailId, password);
        if (user != null) {
            session.setAttribute("user", user);
            session.setAttribute("usertype", "CUSTOMER");
            return "redirect:/CustomerHome";
        }
        return "redirect:/CustomerLogin?error=true";
    }

    @GetMapping("/viewbook")
    public String viewBooks(HttpSession session, Model model) {
        if (session.getAttribute("user") == null || !"CUSTOMER".equals(session.getAttribute("usertype"))) {
            return "redirect:/CustomerLogin";
        }
        
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);

        Map<String, Integer> cartItems = getCartItemsFromSession(session);
        model.addAttribute("cartItems", cartItems);
        
        return "viewbook";
    }

    @RequestMapping(value = "/cart", method = {RequestMethod.GET, RequestMethod.POST})
    public String showCart(
            @RequestParam(value = "selectedBookId", required = false) String selectedBookId,
            @RequestParam(value = "addToCart", required = false) String addToCart,
            @RequestParam(value = "removeFromCart", required = false) String removeFromCart,
            HttpSession session,
            Model model) {
        
        if (session.getAttribute("user") == null || !"CUSTOMER".equals(session.getAttribute("usertype"))) {
            return "redirect:/CustomerLogin";
        }

        Map<String, Integer> cartItems = getCartItemsFromSession(session);
        if (cartItems == null) {
            cartItems = new HashMap<>();
            session.setAttribute("cartItems", cartItems);
        }

        if (selectedBookId != null) {
            if (addToCart != null) {
                cartItems.put(selectedBookId, cartItems.getOrDefault(selectedBookId, 0) + 1);
            } else if (removeFromCart != null) {
                int currentQty = cartItems.getOrDefault(selectedBookId, 0);
                if (currentQty > 1) {
                    cartItems.put(selectedBookId, currentQty - 1);
                } else {
                    cartItems.remove(selectedBookId);
                }
            }
            session.setAttribute("cartItems", cartItems);
        }

        double totalAmount = 0.0;
        List<Cart> cartList = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
            Book book = bookService.getBookById(entry.getKey());
            if (book != null) {
                double amt = book.getPrice() * entry.getValue();
                totalAmount += amt;
                cartList.add(new Cart(book, entry.getValue()));
            }
        }

        model.addAttribute("cartList", cartList);
        model.addAttribute("totalAmount", totalAmount);
        return "cart";
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam("amount") double amount, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/CustomerLogin";
        }
        session.setAttribute("amountToPay", amount);
        return "redirect:/payment";
    }

    @GetMapping("/payment")
    public String paymentPage(HttpSession session, Model model) {
        if (session.getAttribute("user") == null || !"CUSTOMER".equals(session.getAttribute("usertype"))) {
            return "redirect:/CustomerLogin";
        }

        Double amount = (Double) session.getAttribute("amountToPay");
        if (amount == null) {
            amount = 0.0;
        }

        Map<String, Integer> cartItems = getCartItemsFromSession(session);
        List<Cart> cartList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
            Book book = bookService.getBookById(entry.getKey());
            if (book != null) {
                cartList.add(new Cart(book, entry.getValue()));
            }
        }

        model.addAttribute("amountToPay", amount);
        model.addAttribute("cartList", cartList);
        return "payment";
    }

    @PostMapping("/pay")
    public String pay(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/CustomerLogin";
        }

        Map<String, Integer> cartItems = getCartItemsFromSession(session);
        if (cartItems != null) {
            for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
                Book b = bookService.getBookById(entry.getKey());
                if (b != null) {
                    int newQty = Math.max(0, b.getQuantity() - entry.getValue());
                    bookService.updateBookQtyById(b.getBarcode(), newQty);
                    
                    // Save order record
                    Order order = new Order(
                        user.getEmailId(),
                        b.getBarcode(),
                        b.getName(),
                        entry.getValue(),
                        b.getPrice(),
                        b.getPrice() * entry.getValue(),
                        LocalDateTime.now()
                    );
                    orderRepository.save(order);
                }
            }
        }
        return "redirect:/buys";
    }

    @GetMapping("/buys")
    public String getReceipt(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/CustomerLogin";
        }

        Map<String, Integer> cartItems = getCartItemsFromSession(session);
        Double amount = (Double) session.getAttribute("amountToPay");
        if (amount == null) amount = 0.0;

        List<Cart> receiptItems = new ArrayList<>();
        if (cartItems != null) {
            for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
                Book b = bookService.getBookById(entry.getKey());
                if (b != null) {
                    receiptItems.add(new Cart(b, entry.getValue()));
                }
            }
        }

        model.addAttribute("receiptItems", receiptItems);
        model.addAttribute("totalAmount", amount);
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());

        // Clear cart
        session.removeAttribute("cartItems");
        session.removeAttribute("amountToPay");
        
        return "receipt";
    }

    // Safely read cart items from session and convert entries to Map<String,Integer>
    @SuppressWarnings("unchecked")
    private Map<String, Integer> getCartItemsFromSession(HttpSession session) {
        Object raw = session.getAttribute("cartItems");
        if (raw == null) return new HashMap<>();
        if (raw instanceof Map) {
            Map<?, ?> rawMap = (Map<?, ?>) raw;
            Map<String, Integer> safe = new HashMap<>();
            for (Map.Entry<?, ?> e : rawMap.entrySet()) {
                Object k = e.getKey();
                Object v = e.getValue();
                if (k instanceof String && v instanceof Integer) {
                    safe.put((String) k, (Integer) v);
                } else if (k instanceof String && v instanceof Number) {
                    safe.put((String) k, ((Number) v).intValue());
                }
            }
            return safe;
        }
        return new HashMap<>();
    }

    @GetMapping("/orderhistory")
    public String orderHistory(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"CUSTOMER".equals(session.getAttribute("usertype"))) {
            return "redirect:/CustomerLogin";
        }

        List<Order> orders = orderRepository.findByCustomerEmailOrderByOrderDateDesc(user.getEmailId());
        model.addAttribute("orders", orders);
        return "orderhistory";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"CUSTOMER".equals(session.getAttribute("usertype"))) {
            return "redirect:/CustomerLogin";
        }

        List<Order> recentOrders = orderRepository.findByCustomerEmailOrderByOrderDateDesc(user.getEmailId());
        double totalSpent = recentOrders.stream().mapToDouble(Order::getSubtotal).sum();

        model.addAttribute("user", user);
        model.addAttribute("totalOrders", recentOrders.size());
        model.addAttribute("totalSpent", totalSpent);
        model.addAttribute("recentOrders", recentOrders.stream().limit(5).toList());
        return "profile";
    }

    @GetMapping("/CustomerHome")
    public String customerHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"CUSTOMER".equals(session.getAttribute("usertype"))) {
            return "redirect:/CustomerLogin";
        }

        List<Order> orders = orderRepository.findByCustomerEmailOrderByOrderDateDesc(user.getEmailId());
        double totalSpent = orders.stream().mapToDouble(Order::getSubtotal).sum();
        
        int cartItemsCount = getCartItemsFromSession(session).values().stream().mapToInt(Integer::intValue).sum();

        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        model.addAttribute("totalOrders", orders.size());
        model.addAttribute("totalSpent", totalSpent);
        model.addAttribute("cartItemsCount", cartItemsCount);
        
        return "CustomerHome";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}

