package org.mehmetkatr.meridian.user_kyc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mehmetkatr.meridian.user_kyc.dto.CreateUserRequest;
import org.mehmetkatr.meridian.user_kyc.dto.UserResponse;
import org.mehmetkatr.meridian.user_kyc.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

}
