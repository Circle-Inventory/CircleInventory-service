package com.toholanka.inventorybackend.controller;

import com.toholanka.inventorybackend.common.ApiResponse;
import com.toholanka.inventorybackend.dto.ResponseDto;
import com.toholanka.inventorybackend.dto.user.*;
import com.toholanka.inventorybackend.exceptions.AuthenticationFailException;
import com.toholanka.inventorybackend.model.Users;
import com.toholanka.inventorybackend.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody SignupDto signupDto) {
        try {
            userService.signUp(signupDto);
            return ResponseEntity.ok(new ApiResponse(true, "User Signup successfully."));
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse> signIn(@RequestBody SignInDto signInDto) {
        try {
            SignInReponseDto signInReponseDto = userService.signIn(signInDto);
            return ResponseEntity.ok(new ApiResponse(true, signInReponseDto.getToken()));
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/getUser/{token}")
    public Users getUser(@PathVariable String token) {return userService.getUserDetails(token); }

    @GetMapping("/confirm-email")
    public ModelAndView confirmEmail(@RequestParam("token") String token) {
        ModelAndView modelAndView = new ModelAndView();
        if (userService.verifyUser(token)) {
            modelAndView.setViewName("conform-success-page");
            modelAndView.addObject("message", "Your email has been successfully confirmed!");
        } else {
            modelAndView.setViewName("conform-failure-page");
            modelAndView.addObject("message", "Invalid or expired confirmation link.");
        }

        return modelAndView;
    }

    @PutMapping("/edit")
    public ResponseEntity<ApiResponse> editUserDetails(@RequestHeader("Authorization") String authHeader, @RequestBody EditUserDto userEditDto) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Invalid Authorization header format."));
        }

        String token = authHeader.substring(7);

        try {
            userService.editUserDetails(token, userEditDto);
            return ResponseEntity.ok(new ApiResponse(true, "User Details edit successfully."));
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/editPassword")
    public ResponseEntity<ApiResponse> editUserPassword(@RequestHeader("Authorization") String authHeader, @RequestBody EditPasswordDto editPasswordDto) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Invalid Authorization header format."));
        }

        String token = authHeader.substring(7);

        try {
            userService.editUserPassword(token, editPasswordDto.getPassword());
            return ResponseEntity.ok(new ApiResponse(true, "User Password edit successfully."));
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody EmailDto emailDTO) {

        try {
            userService.forgotPassword(emailDTO.getEmail());
            return ResponseEntity.ok(new ApiResponse(true, "Email send successfully."));
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
