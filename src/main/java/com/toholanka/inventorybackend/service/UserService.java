package com.toholanka.inventorybackend.service;

import com.toholanka.inventorybackend.dto.ResponseDto;
import com.toholanka.inventorybackend.dto.user.SignInDto;
import com.toholanka.inventorybackend.dto.user.SignInReponseDto;
import com.toholanka.inventorybackend.dto.user.SignupDto;
import com.toholanka.inventorybackend.exceptions.AuthenticationFailException;
import com.toholanka.inventorybackend.exceptions.CustomException;
import com.toholanka.inventorybackend.model.AuthenticationToken;
import com.toholanka.inventorybackend.model.Users;
import com.toholanka.inventorybackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseDto signUp(SignupDto signupDto) {

        Optional<Users> existingUser = userRepository.findByEmail(signupDto.getEmail());
        if (!existingUser.isEmpty()) {

            throw new CustomException("user already present");
        }

        Users user = new Users();
        user.setLastName(signupDto.getLastName());
        user.setFirstName(signupDto.getFirstName());
        user.setEmail(signupDto.getEmail());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        userRepository.save(user);

        final AuthenticationToken authenticationToken = new AuthenticationToken(user);

        authenticationService.saveConfirmationToken(authenticationToken);

        ResponseDto responseDto = new ResponseDto("success", "user created succesfully");
        return responseDto;
    }

    public SignInReponseDto signIn(SignInDto signInDto) {

        Optional<Users> optionalUser = userRepository.findByEmail(signInDto.getEmail());

        if (!optionalUser.isPresent()) {
            throw new AuthenticationFailException("user is not valid");
        }

        Users user = optionalUser.get();

        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            throw new AuthenticationFailException("wrong password");
        }

        AuthenticationToken token = authenticationService.getToken(user);

        if (Objects.isNull(token)) {
            throw new CustomException("token is not present");
        }

        return new SignInReponseDto("sucess", token.getToken());

    }

    public Users getUserDetails(String token) {

        Users user = authenticationService.getUserByToken(token);

        if (user==null){
            throw new AuthenticationFailException("user is not found");
        }
        return user;
    }
}
