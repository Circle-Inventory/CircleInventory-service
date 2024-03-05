package com.toholanka.inventorybackend.service;

import com.toholanka.inventorybackend.dto.ResponseDto;
import com.toholanka.inventorybackend.dto.user.EditUserDto;
import com.toholanka.inventorybackend.dto.user.SignInDto;
import com.toholanka.inventorybackend.dto.user.SignInReponseDto;
import com.toholanka.inventorybackend.dto.user.SignupDto;
import com.toholanka.inventorybackend.exceptions.AuthenticationFailException;
import com.toholanka.inventorybackend.exceptions.CustomException;
import com.toholanka.inventorybackend.exceptions.InvalidTokenException;
import com.toholanka.inventorybackend.model.AuthenticationToken;
import com.toholanka.inventorybackend.model.Users;
import com.toholanka.inventorybackend.repository.TokenRepository;
import com.toholanka.inventorybackend.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final AuthenticationService authenticationService;

    private final TokenRepository tokenRepository;

    private final EmailService emailService;

    public UserService(UserRepository userRepository, AuthenticationService authenticationService, TokenRepository tokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    @Transactional
    public ResponseDto signUp(SignupDto signupDto) {

        Optional<Users> existingUser = userRepository.findByEmail(signupDto.getEmail());
        if (existingUser.isPresent()) {

            throw new CustomException("User already present");
        }

        Users user = new Users();
        user.setLastName(signupDto.getLastName());
        user.setFirstName(signupDto.getFirstName());
        user.setEmail(signupDto.getEmail());
        user.setPassword(signupDto.getPassword());
        user.setInvite(false);
        user.setVerified(false);

        userRepository.save(user);

        final AuthenticationToken authenticationToken = new AuthenticationToken(user);

        authenticationService.saveConfirmationToken(authenticationToken);

        return new ResponseDto("Success", "User created successfully");
    }

    public SignInReponseDto signIn(SignInDto signInDto) {

        Optional<Users> optionalUser = userRepository.findByEmail(signInDto.getEmail());

        if (optionalUser.isEmpty()) {
            throw new AuthenticationFailException("User is not valid");
        }

        Users user = optionalUser.get();

        if (!Objects.equals(signInDto.getPassword(), user.getPassword())) {
            throw new AuthenticationFailException("Wrong password");
        }

        if (!user.getVerified()){
            throw new AuthenticationFailException ("User not yet joined");
        }

        AuthenticationToken token = authenticationService.getToken(user);

        if (Objects.isNull(token)) {
            throw new CustomException("Token is not present");
        }

        return new SignInReponseDto("Success", token.getToken());

    }

    public Users getUserDetails(String token) {

        Users user = authenticationService.getUserByToken(token);

        if (user==null){
            throw new AuthenticationFailException("User is not found");
        }
        return user;
    }

    public boolean verifyUser(String token) throws InvalidTokenException {
        AuthenticationToken emailConfirmationToken = tokenRepository.findByToken(token);
        if(Objects.isNull(emailConfirmationToken) || !token.equals(emailConfirmationToken.getToken())){
            throw new InvalidTokenException("Token is not valid");
        }
        Users user = emailConfirmationToken.getUser();
        if (Objects.isNull(user)){
            return false;
        }
        user.setVerified(true);
        user.setInvite(false);
        userRepository.save(user);
        return true;
    }

    public void editUserDetails(String token, EditUserDto userEditDto) {

        AuthenticationToken editUserToken = tokenRepository.findByToken(token);

        Users user = editUserToken.getUser();
        if (Objects.isNull(user)){
            throw new CustomException("User not found");
        }

        user.setUserName(userEditDto.getUserName());
        user.setEmail(userEditDto.getEmail());

        userRepository.save(user);
    }

    public void editUserPassword(String token, String newPassword) {

        Users user = authenticationService.getUserByToken(token);

        if (user==null){
            throw new AuthenticationFailException("User is not found");
        }

        String oldPassword = user.getPassword();

        if (Objects.equals(oldPassword, newPassword)) {
            throw new CustomException("Please give different password");
        }

        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public void forgotPassword(String email) throws MessagingException {
        emailService.sendFogotPasswordEmail(email);
    }
}
