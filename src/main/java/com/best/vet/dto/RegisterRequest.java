package com.best.vet.dto;

import com.best.vet.utils.FlexibleDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String gender;
    private String address;
    private String email;
    private String password;
    private Boolean lgpdAccepted;

    @JsonDeserialize(using = FlexibleDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private OffsetDateTime birthDate;
}

