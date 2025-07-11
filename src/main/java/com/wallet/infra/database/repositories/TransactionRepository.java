package com.wallet.infra.database.repositories;

import com.wallet.infra.database.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {

    @Query(value = "SELECT " +
            "   SUM(CASE WHEN transaction_type = 1 THEN amount ELSE -amount END) " +
            "FROM transactions " +
            "WHERE wallet_id = :wallet_id" +
            "   AND created_at < :date",
            nativeQuery = true)
    Optional<BigDecimal> getBalanceByWalletIdAndDate(UUID wallet_id, ZonedDateTime date);

}
