package com.best.vet.service;

import com.best.vet.entity.User;
import com.best.vet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User registerUser(User user) {
        // Optionally hash the password before saving!
        return userRepository.save(user);
    }
}
