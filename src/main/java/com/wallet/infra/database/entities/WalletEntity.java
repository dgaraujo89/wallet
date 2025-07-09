package com.wallet.infra.database.entities;

import com.wallet.domain.WalletStatus;
import com.wallet.infra.database.converters.WalletStatusConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "wallets")
public class WalletEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = UserEntity.class)
    @JoinColumn(referencedColumnName = "user_id")
    private UserEntity user;

    private BigDecimal balance;

    @Convert(converter = WalletStatusConverter.class)
    private WalletStatus status;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private ZonedDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP")
    private ZonedDateTime updatedAt;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private ZonedDateTime lastTransactionAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public WalletStatus getStatus() {
        return status;
    }

    public void setStatus(WalletStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ZonedDateTime getLastTransactionAt() {
        return lastTransactionAt;
    }

    public void setLastTransactionAt(ZonedDateTime lastTransactionAt) {
        this.lastTransactionAt = lastTransactionAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WalletEntity that = (WalletEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(balance, that.balance) && status == that.status && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(lastTransactionAt, that.lastTransactionAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, balance, status, createdAt, updatedAt, lastTransactionAt);
    }

}
