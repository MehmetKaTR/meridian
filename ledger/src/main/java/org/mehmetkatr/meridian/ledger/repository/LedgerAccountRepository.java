package org.mehmetkatr.meridian.ledger.repository;

import org.mehmetkatr.meridian.ledger.entity.LedgerAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerAccountRepository extends JpaRepository<LedgerAccount, Long> {
}
