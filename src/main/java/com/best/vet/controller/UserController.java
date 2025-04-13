package com.best.vet.controller;

import com.best.vet.dto.RegisterRequest;
import com.best.vet.dto.UserInfoDTO;
import com.best.vet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadProfileImage(@RequestParam("file") MultipartFile file,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            String uploadPath = userService.storeProfileImage(file, email);
            return ResponseEntity.ok(Map.of("profileImage", uploadPath));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUserInfo(@RequestBody RegisterRequest userInfoDTO,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        UserInfoDTO response = userService.updateUserInfo(userInfoDTO, email);
        return ResponseEntity.ok(response);
    }

}
