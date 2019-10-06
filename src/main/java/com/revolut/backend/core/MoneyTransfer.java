package com.revolut.backend.core;

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
@Table(name = "moneytransfer")
@NamedQueries(
        {
                @NamedQuery(
                        name = "com.example.revolut.core.MoneyTransfer.findAll",
                        query = "SELECT m FROM MoneyTransfer m"
                )
        })
public class MoneyTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "accountfrom", nullable = false)
    private long accountfrom;

    @Column(name = "accountto", nullable = false)
    private long accountto;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    public MoneyTransfer() {
    }

    public MoneyTransfer(long accountfrom, long accountto, BigDecimal value) {
        this.accountfrom = accountfrom;
        this.accountto = accountto;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAccountFrom() {
        return accountfrom;
    }

    public void setAccountFrom(long accountfrom) {
        this.accountfrom = accountfrom;
    }

    public long getAccountTo() {
        return accountto;
    }

    public void setAccountTo(long accountto) {
        this.accountto = accountto;
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
                Objects.equals(this.accountfrom, that.accountfrom) &&
                Objects.equals(this.accountto, that.accountto) &&
                Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountfrom, accountto, value);
    }
}
