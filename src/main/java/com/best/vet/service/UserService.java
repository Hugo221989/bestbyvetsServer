package com.best.vet.service;

import com.best.vet.dto.RegisterRequest;
import com.best.vet.dto.UserInfoDTO;
import com.best.vet.entity.User;
import com.best.vet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class UserService {

    @Value("${upload.profile.dir}")
    private String uploadDir;

    @Autowired
    private UserRepository userRepository;


    @Transactional
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public Boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public String storeProfileImage(MultipartFile file, String email) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Save image path to user
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String imageUrl = "/images/" + filename;
        user.setProfileImage(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }

    public UserInfoDTO updateUserInfo(RegisterRequest userInfoDTO, String email){
        User user = userRepository.findByEmail(email);
        user.setFirstName(userInfoDTO.getFirstName());
        user.setLastName(userInfoDTO.getLastName());
        user.setBirthDate(userInfoDTO.getBirthDate().toLocalDate());
        user.setGender(userInfoDTO.getGender());
        userRepository.save(user);
        return new UserInfoDTO(userInfoDTO.getFirstName(), userInfoDTO.getLastName(), userInfoDTO.getGender(), userInfoDTO.getBirthDate().toLocalDate());
    }

}
