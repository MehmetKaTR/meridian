package org.mehmetkatr.meridian.account.repository;

import org.mehmetkatr.meridian.account.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

}
