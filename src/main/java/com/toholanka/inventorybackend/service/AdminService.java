package com.toholanka.inventorybackend.service;

import com.toholanka.inventorybackend.dto.user.UserCreateDto;
import com.toholanka.inventorybackend.dto.user.UserInviteDto;
import com.toholanka.inventorybackend.exceptions.CustomException;
import com.toholanka.inventorybackend.model.AuthenticationToken;
import com.toholanka.inventorybackend.model.Users;
import com.toholanka.inventorybackend.repository.TokenRepository;
import com.toholanka.inventorybackend.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdminService {

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final AuthenticationService authenticationService;

    private final EmailService emailService;

    public AdminService(UserRepository userRepository, TokenRepository tokenRepository, AuthenticationService authenticationService, EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.authenticationService = authenticationService;
        this.emailService = emailService;
    }

    public List<Users> getAllUsers() {
        List<Users> usersList = userRepository.findAll();
        List<Users> users = new ArrayList<>();
        for (int i = 0; i < usersList.size(); i++) {
            Users user = usersList.get(i);
            if (!Objects.equals(user.getRole(), "ADMIN")) {
                users.add(user);
            }
        }
        return users;
    }

    public void assignRoleToUser(String email, String role) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));
        user.setRole(role);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    @Transactional
    public void createUser(UserCreateDto userCreateDto) throws MessagingException{
        Optional<Users> existingUser = userRepository.findByEmail(userCreateDto.getEmail());
        if (existingUser.isPresent()) {

            throw new CustomException("User already present");
        }

        Users user = new Users();
        user.setUserName(userCreateDto.getUserName());
        user.setEmail(userCreateDto.getEmail());
        user.setPassword(userCreateDto.getPassword());
        user.setPhoneNumber(userCreateDto.getPhoneNumber());
        user.setAddDate(userCreateDto.getAddDate());
        user.setRole(userCreateDto.getRole());
        user.setInvite(false);
        user.setVerified(false);

        userRepository.save(user);

        final AuthenticationToken authenticationToken = new AuthenticationToken(user);

        authenticationService.saveConfirmationToken(authenticationToken);
    }

    public void inviteMailSend(UserInviteDto userInviteDto) throws MessagingException {

        Users user = userRepository.findByEmail(userInviteDto.getEmail())
                .orElseThrow(() -> new CustomException("User not found"));

        final AuthenticationToken authenticationToken  = tokenRepository.findByUser(user);

        try {
            emailService.sendConfirmationEmail(authenticationToken);
            
            user.setInvite(true);
            userRepository.save(user);
        } catch (MessagingException e) {

            throw e;
        }
    }

    @Transactional
    public void editUserDetails(String email, UserCreateDto userCreateDto) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));

        user.setUserName(userCreateDto.getUserName());
        user.setEmail(userCreateDto.getEmail());
        user.setPassword(userCreateDto.getPassword());
        user.setPhoneNumber(userCreateDto.getPhoneNumber());
        user.setRole(userCreateDto.getRole());

        userRepository.save(user);
    }
}
