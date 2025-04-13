package com.best.vet.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private UserInfoDTO UserInfoDTO;

    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public AuthResponse(String accessToken, String refreshToken, UserInfoDTO UserInfoDTO) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.UserInfoDTO = UserInfoDTO;
    }
}