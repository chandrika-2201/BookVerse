package com.bittercode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticController {

    @GetMapping("/")
    public String index() {
        return "index";
    }



    @GetMapping("/CustomerLogin")
    public String customerLogin() {
        return "CustomerLogin";
    }

    @GetMapping("/CustomerRegister")
    public String customerRegister() {
        return "CustomerRegister";
    }

    @GetMapping("/SellerLogin")
    public String sellerLogin() {
        return "SellerLogin";
    }

    /* The payment page is populated dynamically in CustomerController */

}
