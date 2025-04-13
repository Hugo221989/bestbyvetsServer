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

    private String firstName;
    private String lastName;
    private String gender;
    private String address;
    private String email;
    private String password;
    private Boolean lgpdAccepted;
    @Column(name = "profile_image")
    private String profileImage;

    private Boolean enabled = false;
    private LocalDate birthDate;
}
