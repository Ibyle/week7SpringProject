package com.michael.ecommercespringproj.models;

import com.michael.ecommercespringproj.dtos.AdminDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String imageUrl;
    private String password;
    private String fullName;
    private BigDecimal balance;

    public Admin(AdminDTO adminDTO) {
        this.username = adminDTO.getUsername();
        this.password =  BCrypt.withDefaults().hashToString(12, adminDTO.getPassword().toCharArray());
        this.fullName = adminDTO.getFullname();
        this.balance = new BigDecimal(2500000);
    }
}
