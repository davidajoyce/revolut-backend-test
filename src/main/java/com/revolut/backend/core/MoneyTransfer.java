package com.revolut.backend.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "moneyTransfer")
@NamedQueries(
        {
                @NamedQuery(
                        name = "com.example.revolut.core.MoneyTransfer.findAll",
                        query = "SELECT t FROM Transaction t"
                )
        })
public class MoneyTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "accountFromId", nullable = false)
    private long accountFromId;

    @Column(name = "accountToId", nullable = false)
    private long accountToId;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    public MoneyTransfer() {
    }

    public MoneyTransfer(long accountFromId, long accountToId, BigDecimal value) {
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(long accountFromId) {
        this.accountFromId = accountFromId;
    }

    public long getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(long accountToId) {
        this.accountToId = accountToId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoneyTransfer)) {
            return false;
        }

        final MoneyTransfer that = (MoneyTransfer) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.accountFromId, that.accountFromId) &&
                Objects.equals(this.accountToId, that.accountToId) &&
                Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountFromId, accountToId, value);
    }
}
