package com.michael.ecommercespringproj.serviceImpl;

import com.michael.ecommercespringproj.models.Cart;
import com.michael.ecommercespringproj.models.Order;
import com.michael.ecommercespringproj.models.Product;
import com.michael.ecommercespringproj.repositories.ProductRepositories;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Service  // Indicates that this class is a Spring service component
public class ProductServiceImpl {

    private ProductRepositories productRepositories;

    @Autowired  // Enables automatic dependency injection of ProductRepositories into the constructor
    public ProductServiceImpl(ProductRepositories productRepositories) {
        this.productRepositories = productRepositories;
    }

    // Supplier to retrieve all products from the repository
    public Supplier<List<Product>> findAllProducts = () -> productRepositories.findAll();

    // Function to find a product by ID from the repository
    public Function<Long, Product> findById = (id) ->
            productRepositories.findById(id)
                    .orElseThrow(() ->
                            new NullPointerException("No such product found with ID: " + id));

    // Method to add a product to the user's cart
    public void addProductToCart(Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Cart cart;

        // Check if the cart exists in the session
        if (session.getAttribute("cart") != null) {
            cart = (Cart) session.getAttribute("cart");
            cart.setProductIds(cart.getProductIds() + "," + id);
            session.setAttribute("cartItems", cart.getProductIds().split(",").length);
        } else {
            // If the cart doesn't exist, create a new cart and add the product
            cart = Cart.builder().productIds(id.toString())
                    .userId((Long) session.getAttribute("userID")).build();
            session.setAttribute("cart", cart);
            session.setAttribute("cartItems", cart.getProductIds().split(",").length);
        }
    }

    // Method to process the checkout of the cart
    public void checkOutCart(HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");
        List<Product> productList = new ArrayList<>();
        List<String> productIds = Arrays.stream(cart.getProductIds().split(",")).toList();

        // Retrieve products from the repository based on product IDs
        productIds.forEach(id -> {
            productList.add(productRepositories.findById(Long.parseLong(id)).orElseThrow(() ->
                    new NullPointerException("No such product found with ID: " + id)));
        });

        final BigDecimal[] totalPrice = {new BigDecimal(0)};

        // Calculate the total price of the products in the cart
        productList.forEach(product -> totalPrice[0] = totalPrice[0].add(product.getPrice()));

        // Update the model with the total price
        model.addAttribute("totalPrice", "Total Price: $" + totalPrice[0]);

        // Clear the cart in the session
        session.setAttribute("cart", null);

        // Create an order object and set it in the session and model
        Order order = Order.builder()
                .productList(productList)
                .userId((Long) session.getAttribute("userID"))
                .totalPrice(totalPrice[0])
                .build();
        session.setAttribute("order", order);
        model.addAttribute("order", order);
    }
}
