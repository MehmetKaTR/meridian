package org.mehmetkatr.meridian.mock_bank.repository;

import org.mehmetkatr.meridian.mock_bank.entity.ExternalTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExternalTransferRepository extends JpaRepository<ExternalTransfer, Long> {
    Optional<ExternalTransfer> findByReference(String reference);
}
