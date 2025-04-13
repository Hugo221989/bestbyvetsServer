package com.best.vet.controller;

import com.best.vet.dto.AuthResponse;
import com.best.vet.dto.LoginRequest;
import com.best.vet.dto.RegisterRequest;
import com.best.vet.dto.TokenRefreshRequest;
import com.best.vet.entity.User;
import com.best.vet.entity.VerificationToken;
import com.best.vet.error.ApiError;
import com.best.vet.repository.VerificationTokenRepository;
import com.best.vet.service.AuthenticationService;
import com.best.vet.service.MailService;
import com.best.vet.service.UserService;
import com.best.vet.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (request == null || request.getEmail() == null || request.getEmail().isEmpty()) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), "Email is required.");
            return ResponseEntity.badRequest().body(error);
        }
        if (request.getBirthDate() == null) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), "Birth date is required.");
            return ResponseEntity.badRequest().body(error);
        }
        if (userService.emailExists(request.getEmail())) {
            ApiError error = new ApiError(HttpStatus.CONFLICT.value(), "Email already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());
        user.setEmail(request.getEmail());
        user.setEnabled(false);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBirthDate(request.getBirthDate().toLocalDate());
        user.setLgpdAccepted(request.getLgpdAccepted());
        User savedUser = userService.registerUser(user);

        // Generate activation token
        String token = generateActivationToken(savedUser);

        // Send activation email
        mailService.sendActivationEmail(savedUser, token);

        return ResponseEntity.ok("Registration successful, please check your email for the activation link.");
    }

    private String generateActivationToken (User savedUser){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(savedUser);
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1)); // token valid for 1 day
        tokenRepository.save(verificationToken);
        return token;
    }

    @GetMapping("/activate")
    public ResponseEntity<?> activateAccount(@RequestParam("token") String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return ResponseEntity.badRequest().body("Invalid activation token.");
        }
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token expired.");
        }
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userService.registerUser(user); // save the updated user status
        return ResponseEntity.ok("Account activated successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        AuthResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest request) {
        String newAccessToken = authenticationService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(new AuthResponse(newAccessToken, request.getRefreshToken()));
    }
}
