package org.mehmetkatr.meridian.user_kyc.mapper;

import org.mehmetkatr.meridian.user_kyc.dto.UserResponse;
import org.mehmetkatr.meridian.user_kyc.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserResponse toResponse(User user){

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setKycStatus(user.getKycStatus());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }

    public List<UserResponse> toResponse(List<User> users){
        return users.stream().map(this::toResponse).toList();
    }
}
