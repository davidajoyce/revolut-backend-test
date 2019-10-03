package com.revolut.backend;

import com.revolut.backend.core.Account;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountResourceTest extends BaseResourceTest{

    @Test
    public void testPostAccount() throws Exception {
        final Account account = new Account("Dr. IntegrationTest", BigDecimal.valueOf(1000));
        final Account newAccount = postAccount(account);
        assertThat(newAccount.getId()).isNotNull();
        assertThat(newAccount.getName()).isEqualTo(account.getName());
        assertThat(newAccount.getBalance()).isEqualTo(account.getBalance());
        deleteAccount(newAccount);
    }

    @Test
    public void testGetAccounts() throws Exception{
        final Account account = new Account("Mr.Test", BigDecimal.valueOf(1000));
        postAccount(account);
        Response response = getAccounts();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
        List<Account> accounts = response.readEntity(new GenericType<List<Account>>() {});
        assertThat(accounts.size()).isEqualTo(1);
        assertThat(accounts.get(0).getName()).isEqualTo(account.getName());
        assertThat(accounts.get(0).getBalance()).isEqualTo(account.getBalance());
        deleteAccount(account);
    }

    @Test
    public void testGetAccountSingle() throws Exception {
        final Account account = new Account("Mr.Test", BigDecimal.valueOf(1000));
        final Account newAccount = postAccount(account);
        Account testAccount = getAccountById(newAccount);
        assertThat(testAccount.getId()).isNotNull();
        assertThat(testAccount.getName()).isEqualTo(newAccount.getName());
        assertThat(testAccount.getBalance()).isEqualTo(newAccount.getBalance());
        deleteAccount(account);
    }
}
