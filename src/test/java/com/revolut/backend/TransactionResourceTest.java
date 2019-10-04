package com.revolut.backend;

import com.revolut.backend.core.Account;
import com.revolut.backend.core.MoneyTransfer;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TransactionResourceTest extends BaseResourceTest{

    @Test
    public void testMoneyTransferSuccess() throws Exception {
        Account account1 = new Account("Test Account 1", BigDecimal.valueOf(10000));
        Account newAccount1 =  postAccount(account1);
        Account account2 = new Account("Test Account 1", BigDecimal.valueOf(10000));
        Account newAccount2 =  postAccount(account2);

        //String response = postTransactionString(newAccount1.getId(), newAccount2.getId(), BigDecimal.valueOf(5000));
        MoneyTransfer moneyTransfer = postTransaction(newAccount1.getId(), newAccount2.getId(), BigDecimal.valueOf(5000));

        assertEquals(account1.getId(), moneyTransfer.getAccountFromId());
        assertEquals(account2.getId(), moneyTransfer.getAccountToId());
        assertEquals(BigDecimal.valueOf(5000), moneyTransfer.getValue());

        Account getAccount1 = getAccountById(account1);
        Account getAccount2 = getAccountById(account2);

        assertEquals(BigDecimal.valueOf(5000), getAccount1.getBalance());
        assertEquals(BigDecimal.valueOf(15000), getAccount2.getBalance());
    }

}
