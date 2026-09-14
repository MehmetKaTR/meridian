package org.mehmetkatr.meridian.account.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountResponse {
    private Long id;
    private Long userId;
    private String iban;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
