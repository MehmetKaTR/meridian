package org.mehmetkatr.meridian.user_kyc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message="Ad zorunlu")
    private String fullName;

    @NotBlank
    private String username;

    @NotBlank
    @Email(message="Gecerli email girin")
    private String email;

    @Pattern(regexp="^\\+?[0-9]{10,15}$", message="Gecersiz telefon")
    private String phone;
}
