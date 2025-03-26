package com.best.vet.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;
    private String gender;
    private String address;
    private String username;
    private String email;
    private String password;
    private Boolean lgpdAccepted;

    private Boolean enabled = false;
    private LocalDate birthDate;
}
