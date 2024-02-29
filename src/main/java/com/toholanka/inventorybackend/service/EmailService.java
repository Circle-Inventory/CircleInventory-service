package com.toholanka.inventorybackend.service;

import com.toholanka.inventorybackend.exceptions.AuthenticationFailException;
import com.toholanka.inventorybackend.model.AuthenticationToken;
import com.toholanka.inventorybackend.model.Users;
import com.toholanka.inventorybackend.repository.TokenRepository;
import com.toholanka.inventorybackend.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class EmailService {
    private final JavaMailSender sender;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public EmailService(JavaMailSender sender, UserRepository userRepository, TokenRepository tokenRepository) {
        this.sender = sender;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    public void sendConfirmationEmail(AuthenticationToken emailConfirmationToken) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(emailConfirmationToken.getUser().getEmail());
        helper.setSubject("Confirm you TOHO LANKA Inventory Registration");
        helper.setText("<html>" +
                        "<body>" +
                        "<h2>Dear "+ emailConfirmationToken.getUser().getUserName() + ",</h2>"
                        + "<br/> We're excited to have you get started. " +
                        "Please click on below link to confirm your account."
                        + "<br/> "  + generateConfirmationLink(emailConfirmationToken.getToken())+"" +
                        "<br/> Regards,<br/>" +
                        "TOHO-LANKA Administration" +
                        "</body>" +
                        "</html>"
                , true);

        sender.send(message);
    }

    @Transactional
    public void sendFogotPasswordEmail(String email) throws MessagingException {

        Optional<Users> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new AuthenticationFailException("User is not valid");
        }

        Users user = optionalUser.get();

        AuthenticationToken forgotPasswordToken = tokenRepository.findByUser(user);

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject("Confirm you TOHO LANKA Inventory Registration");
        helper.setText("<html>" +
                        "<body>" +
                        "<h2>Dear "+ user.getUserName() + ",</h2>"
                        + "<br/> We're excited to have you get started. " +
                        "Please click on below link to edit your password."
                        + "<br/> "  + generateForgotPasswordLink(forgotPasswordToken.getToken())+"" +
                        "<br/> Regards,<br/>" +
                        "TOHO-LANKA Administration" +
                        "</body>" +
                        "</html>"
                , true);

        sender.send(message);
    }

    private String generateConfirmationLink(String token){
        return "<a href=http://localhost:8080/users/confirm-email?token="+token+">Confirm Email</a>";
    }

    private String generateForgotPasswordLink(String token){
        return "<a href=http://localhost:3000/login/newPassword?token="+token+">Forgot Password</a>";
    }

}
