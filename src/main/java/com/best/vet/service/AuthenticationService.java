package com.best.vet.service;

import com.best.vet.dto.AuthResponse;
import com.best.vet.dto.LoginRequest;
import com.best.vet.dto.UserInfoDTO;
import com.best.vet.entity.User;
import com.best.vet.repository.UserRepository;
import com.best.vet.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        UserInfoDTO userInfoDTO = new UserInfoDTO(user.getFirstName(), user.getLastName(), user.getGender(), user.getBirthDate());

        return new AuthResponse(accessToken, refreshToken, userInfoDTO);
    }

    public String refreshAccessToken(String refreshToken) {
        String email = jwtUtil.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email);

        if (!jwtUtil.validateToken(refreshToken, email)) {
            throw new RuntimeException("Invalid refresh token");
        }

        return jwtUtil.generateAccessToken(user);
    }

}
