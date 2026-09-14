package org.mehmetkatr.meridian.user_kyc.entity;

import jakarta.persistence.*;
import lombok.*;
import org.mehmetkatr.meridian.common.base.BaseEntity;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String phone;

    @Column(name = "kyc_status", nullable = false)
    private String kycStatus;

}
