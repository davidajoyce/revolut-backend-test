package com.revolut.backend;

import com.revolut.backend.core.Account;
import com.revolut.backend.core.MoneyTransfer;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TransactionResourceTest extends BaseResourceTest{

    @Test
    public void testMoneyTransferSuccess() throws Exception {
        Account account1 = new Account("Test Account 1", BigDecimal.valueOf(10000));
        Account newAccount1 =  postAccount(account1);
        Account account2 = new Account("Test Account 1", BigDecimal.valueOf(10000));
        Account newAccount2 =  postAccount(account2);

        MoneyTransfer moneyTransfer = postTransaction(account1.getId(), account2.getId(), BigDecimal.valueOf(5000));

        assertEquals(account1.getId(), moneyTransfer.getFromAccount());
        assertEquals(account2.getId(), moneyTransfer.getToAccount());
        assertEquals(BigDecimal.valueOf(5000), moneyTransfer.getAmount());

        Account getAccount1 = getAccountById(account1);
        Account getAccount2 = getAccountById(account2);

        assertEquals(BigDecimal.valueOf(5000), getAccount1.getBalance());
        assertEquals(BigDecimal.valueOf(15000), getAccount2.getBalance());
    }

}
