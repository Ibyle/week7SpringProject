package com.michael.ecommercespringproj.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.michael.ecommercespringproj.models.Product;
import com.michael.ecommercespringproj.models.Users;
import com.michael.ecommercespringproj.repositories.ProductRepositories;
import com.michael.ecommercespringproj.repositories.UsersRepositories;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

@Component  // Indicates that this class is a Spring component
public class CSVUtils {

    private UsersRepositories usersRepositories;
    private ProductRepositories productRepositories;

    @Autowired  // Enables automatic dependency injection of UsersRepositories and ProductRepositories into the constructor
    public CSVUtils(UsersRepositories usersRepositories, ProductRepositories productRepositories) {
        this.usersRepositories = usersRepositories;
        this.productRepositories = productRepositories;
    }

    @PostConstruct  // Indicates that this method should be executed after the bean has been initialized
    public void readUserCSV(){

        // User database seeding
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/java/com/michael/ecommercespringproj/users.csv"))) {
            String line;
            boolean lineOne = false;
            while ((line=bufferedReader.readLine())!=null){
                String[] user = line.split(",");
                if (lineOne) {
                    // Create a Users object and save it to the repository
                    Users user1 = Users.builder().fullName(user[1])
                            .imageUrl(user[3])
                            .password(BCrypt.withDefaults()
                                    .hashToString(12, user[2].trim().toCharArray()))
                            .username(user[0])
                            .balance(new BigDecimal(2500000))
                            .build();
                    usersRepositories.save(user1);
                }
                lineOne = true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Product database seeding
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/java/com/michael/ecommercespringproj/products.csv"))) {
            String line;
            boolean lineOne = false;
            while ((line=bufferedReader.readLine())!=null){
                String[] product = line.split(",");
                if (lineOne) {
                    // Create a Product object and save it to the repository
                    Product product1 = Product.builder()
                            .categories(product[0])
                            .price(new BigDecimal(product[1]))
                            .productName(product[2])
                            .quantity(Long.parseLong(product[3]))
                            .build();
                    productRepositories.save(product1);
                }
                lineOne = true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
