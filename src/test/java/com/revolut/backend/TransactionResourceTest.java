package com.revolut.backend;

import com.revolut.backend.core.Account;
import com.revolut.backend.core.MoneyTransfer;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TransactionResourceTest extends BaseResourceTest{

    @Test
    public void testMoneyTransferSuccess() throws Exception {
        Account account1 = new Account("Test Account 1", BigDecimal.valueOf(10000));
        Account newAccount1 = postAccount(account1);
        Account account2 = new Account("Test Account 1", BigDecimal.valueOf(10000));
        Account newAccount2 = postAccount(account2);

        MoneyTransfer moneyTransfer = postTransaction(newAccount1.getId(), newAccount2.getId(), BigDecimal.valueOf(5000));

        assertEquals(newAccount1.getId(), moneyTransfer.getAccountFrom());
        assertEquals(newAccount2.getId(), moneyTransfer.getAccountTo());
        assertEquals(BigDecimal.valueOf(5000), moneyTransfer.getValue());

        Account accountAfterTransfer1 = getAccountById(newAccount1);
        Account accountAfterTransfer2 = getAccountById(newAccount2);

        assertEquals(BigDecimal.valueOf(5000), accountAfterTransfer1.getBalance());
        assertEquals(BigDecimal.valueOf(15000), accountAfterTransfer2.getBalance());
    }

    @Test
    public void testMoneyTransferFailure_fromAccountNotEnoughBalance() throws Exception{
        Account account1 = new Account("Test Account 1", BigDecimal.valueOf(3000));
        Account newAccount1 = postAccount(account1);
        Account account2 = new Account("Test Account 1", BigDecimal.valueOf(10000));
        Account newAccount2 = postAccount(account2);

        Response moneyTransferResponse = postTransactionResponse(newAccount1.getId(), newAccount2.getId(), BigDecimal.valueOf(5000));
        assertEquals(404, moneyTransferResponse.getStatus());
    }
}
