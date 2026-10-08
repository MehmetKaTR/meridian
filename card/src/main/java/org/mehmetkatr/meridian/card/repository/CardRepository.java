package org.mehmetkatr.meridian.card.repository;

import org.mehmetkatr.meridian.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByWalletId(Long walletId);
}
