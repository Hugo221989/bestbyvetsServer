package com.best.vet.dto;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}