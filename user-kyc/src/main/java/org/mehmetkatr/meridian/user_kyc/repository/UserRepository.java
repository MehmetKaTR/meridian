package org.mehmetkatr.meridian.user_kyc.repository;

import org.mehmetkatr.meridian.user_kyc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
