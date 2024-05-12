package com.ftn.sbnz.service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ftn.sbnz.model.models.users.User;
import com.ftn.sbnz.service.auth.JwtUtil;
import com.ftn.sbnz.service.dtos.request.LoginRequest;
import com.ftn.sbnz.service.dtos.response.ErrorResponse;
import com.ftn.sbnz.service.dtos.response.LoginResponse;
import com.ftn.sbnz.service.service.UserService;

@Controller
@RequestMapping("/rest/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginReq) {
        try {
            User user = userService.authenticateUser(loginReq.getEmail(), loginReq.getPassword());
            if (user == null) {
                throw new BadCredentialsException("Invalid username or password");
            }

            String token = jwtUtil.createToken(user);
            LoginResponse loginRes = new LoginResponse(user.getEmail(), token);
            return ResponseEntity.ok(loginRes);

        } catch (BadCredentialsException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        registeredUser.setPassword(null);
        return ResponseEntity.ok(registeredUser);
    }
}
