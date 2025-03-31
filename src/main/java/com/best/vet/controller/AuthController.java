package com.best.vet.controller;
import com.best.vet.dto.LoginRequest;
import com.best.vet.dto.RegisterRequest;
import com.best.vet.entity.User;
import com.best.vet.entity.VerificationToken;
import com.best.vet.repository.VerificationTokenRepository;
import com.best.vet.service.AuthenticationService;
import com.best.vet.service.MailService;
import com.best.vet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api")
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setEnabled(false);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
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
        try {
            User user = authenticationService.login(request.getUsername(), request.getPassword());
            // Remove sensitive info before returning (e.g., password)
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
