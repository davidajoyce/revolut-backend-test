package com.revolut.backend.core;

import java.math.BigDecimal;

public class MoneyTransfer {

    private long fromId;
    private long toId;
    private BigDecimal value;

    public long getFromAccount() {
        return fromId;
    }

    public long getToAccount() {
        return toId;
    }

    public BigDecimal getAmount() {
        return value;
    }
}
