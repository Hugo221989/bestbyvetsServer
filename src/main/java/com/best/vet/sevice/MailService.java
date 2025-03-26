package com.best.vet.sevice;

import com.best.vet.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendActivationEmail(User user, String token) {
        String activationUrl = "http://localhost:8080/api/activate?token=" + token; // update your domain accordingly

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Activate your account");
        mailMessage.setText("Please click the following link to activate your account: " + activationUrl);

        mailSender.send(mailMessage);
    }
}

