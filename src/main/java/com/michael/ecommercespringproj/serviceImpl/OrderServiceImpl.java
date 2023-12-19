package com.michael.ecommercespringproj.serviceImpl;

import com.michael.ecommercespringproj.models.Order;
import com.michael.ecommercespringproj.models.Users;
import com.michael.ecommercespringproj.repositories.OrderRepositories;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.function.Function;

@Service  // Indicates that this class is a Spring service component
public class OrderServiceImpl {

    private OrderRepositories orderRepositories;
    private UsersServiceImpl usersService;

    @Autowired  // Enables automatic dependency injection of OrderRepositories and UsersServiceImpl into the constructor
    public OrderServiceImpl(OrderRepositories orderRepositories, UsersServiceImpl usersService) {
        this.orderRepositories = orderRepositories;
        this.usersService = usersService;
    }

    // Function to save an order to the repository
    public Function<Order, Order> saveOrder = (order) -> orderRepositories.save(order);

    // Method to process the payment for an order
    public String makePayment(HttpSession session, Model model) {
        // Retrieve the user and order from the session
        Users user = usersService.findUsersById.apply((Long) session.getAttribute("userID"));
        Order order = (Order) session.getAttribute("order");

        // Check if the user has sufficient balance for the payment
        if (user.getBalance().doubleValue() < order.getTotalPrice().doubleValue()) {
            model.addAttribute("paid", "Insufficient balance in your account!");
            return "checkout";
        }

        // Deduct the order total price from the user's balance
        user.setBalance(user.getBalance().subtract(order.getTotalPrice()));
        // Save the updated user information
        usersService.saveUser.apply(user);

        // Save the order to the repository
        Order savedOrder = saveOrder.apply(order);
        // Clear the order from the session
        session.setAttribute("order", null);

        // Update the model with payment success message
        model.addAttribute("paid", "Payment was successful!");
        return "successfully-paid";
    }
}
