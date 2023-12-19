package com.michael.ecommercespringproj.repositories;

import com.michael.ecommercespringproj.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepositories extends JpaRepository<Product, Long> {
    List<Product> findByCategories(String categories);
}
