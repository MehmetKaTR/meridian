package org.mehmetkatr.meridian.user_kyc.service;

import lombok.RequiredArgsConstructor;
import org.mehmetkatr.meridian.user_kyc.dto.CreateUserRequest;
import org.mehmetkatr.meridian.user_kyc.dto.UserResponse;
import org.mehmetkatr.meridian.user_kyc.entity.User;
import org.mehmetkatr.meridian.user_kyc.mapper.UserMapper;
import org.mehmetkatr.meridian.user_kyc.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse createUser(CreateUserRequest request){

        User newUser = User.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .kycStatus("PENDING")
                .build();

        User saved = userRepository.save(newUser);
        return userMapper.toResponse(saved);
    }

    public List<UserResponse> getAllUsers(){
        return userMapper.toResponse(userRepository.findAll());
    }

}
