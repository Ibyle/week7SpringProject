package com.michael.ecommercespringproj.controllers;

import com.michael.ecommercespringproj.models.Product;
import com.michael.ecommercespringproj.serviceImpl.OrderServiceImpl;
import com.michael.ecommercespringproj.serviceImpl.ProductServiceImpl;
import com.michael.ecommercespringproj.serviceImpl.UsersServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Controller  // Marks the class as a Spring MVC controller
@RequestMapping("/products")  // Base URL path for all handler methods in this controller
public class ProductController {
    private ProductServiceImpl productService;
    private UsersServiceImpl usersService;
    private OrderServiceImpl orderService;

    @Autowired  // Enables automatic dependency injection of ProductServiceImpl, UsersServiceImpl, and OrderServiceImpl
    public ProductController(ProductServiceImpl productService, UsersServiceImpl usersService, OrderServiceImpl orderService) {
        this.productService = productService;
        this.usersService = usersService;
        this.orderService = orderService;
    }

    @GetMapping("/all")  // Handler method for GET requests to "/products/all"
    public ModelAndView findAllProducts(HttpServletRequest request){
        HttpSession session = request.getSession();
        // Retrieve the list of all products from ProductService
        List<Product> productList = productService.findAllProducts.get();
        // Return a ModelAndView with the "dashboard" view, products, and cart items
        return new ModelAndView("dashboard")
                .addObject("products", productList)
                .addObject("cartItems", "Cart Items: "+session.getAttribute("cartItems"));
    }

    @GetMapping("/add-cart")  // Handler method for GET requests to "/products/add-cart"
    public String addToCart(@RequestParam(name = "cart") Long id, HttpServletRequest request, Model model){
        // Add the specified product to the user's cart using ProductService
        productService.addProductToCart(id, request);
        // Redirect to the "/products/all" page after adding to the cart
        return "redirect:/products/all";
    }

    @GetMapping("/payment")  // Handler method for GET requests to "/products/payment"
    public String checkOut(HttpSession session, Model model){
        // Process the checkout and update the model with checkout details
        productService.checkOutCart(session, model);
        model.addAttribute("paid", "");
        // Return the "checkout" view
        return "checkout";
    }

    @GetMapping("/pay")  // Handler method for GET requests to "/products/pay"
    public String orderPayment(HttpSession session, Model model){
        // Initiate the order payment process using OrderServiceImpl
        return orderService.makePayment(session, model);
    }
}
