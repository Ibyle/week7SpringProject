package com.michael.ecommercespringproj.repositories;

import com.michael.ecommercespringproj.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepositories extends JpaRepository<Order, Long> {
}
