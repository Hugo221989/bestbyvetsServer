package com.best.vet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserInfoDTO {
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate birthDate;
}
