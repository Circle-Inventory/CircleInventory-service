package com.toholanka.inventorybackend.controller;

import com.toholanka.inventorybackend.dto.ResponseDto;
import com.toholanka.inventorybackend.dto.user.SignInDto;
import com.toholanka.inventorybackend.dto.user.SignInReponseDto;
import com.toholanka.inventorybackend.dto.user.SignupDto;
import com.toholanka.inventorybackend.model.Users;
import com.toholanka.inventorybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody SignupDto signupDto) {
        return userService.signUp(signupDto);
    }

    @PostMapping("/signin")
    public SignInReponseDto signIn(@RequestBody SignInDto signInDto) {
        return userService.signIn(signInDto);
    }

    @GetMapping("/getUser/{token}")
    public Users getUser(@PathVariable String token) {return userService.getUserDetails(token); }
}
