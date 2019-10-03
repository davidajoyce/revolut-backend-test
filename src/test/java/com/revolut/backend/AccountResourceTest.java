package com.revolut.backend;

import com.revolut.backend.core.Account;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
public class AccountResourceTest {

    private static final String TMP_FILE = createTempFile();
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-example.yml");

    public static final DropwizardAppExtension<MoneyTransferConfiguration> RULE = new DropwizardAppExtension<>(
            MoneyTransferApplication.class, CONFIG_PATH,
            ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE));

    @BeforeAll
    public static void migrateDb() throws Exception {
        RULE.getApplication().run("db", "migrate", CONFIG_PATH);
    }

    private static String createTempFile() {
        try {
            return File.createTempFile("test-example", null).getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testPostAccount() throws Exception {
        final Account account = new Account("Dr. IntegrationTest", BigDecimal.valueOf(1000));
        final Account newAccount = postAccount(account);
        assertThat(newAccount.getId()).isNotNull();
        assertThat(newAccount.getName()).isEqualTo(account.getName());
        assertThat(newAccount.getBalance()).isEqualTo(account.getBalance());
        deleteAccount(newAccount);
    }

    private void deleteAccount(Account newAccount) {
        final String url = "http://localhost:" + RULE.getLocalPort() + "/account/" + newAccount.getId();
        Response response = RULE.client().target(url).request().delete();
    }

    private Account postAccount(Account account) {
        return RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/account")
                .request()
                .post(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Account.class);
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
    }

    private Response getAccounts() {
        return RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/account")
                .request()
                .get();
    }

    @Test
    public void testGetAccountSingle() throws Exception {
        final Account account = new Account("Mr.Test", BigDecimal.valueOf(1000));
        final Account newAccount = postAccount(account);
        final String url = "http://localhost:" + RULE.getLocalPort() + "/account/" + newAccount.getId();
        Response response = RULE.client().target(url).request().get();
        Account testAccount = response.readEntity(Account.class);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
        assertThat(testAccount.getId()).isNotNull();
        assertThat(testAccount.getName()).isEqualTo(newAccount.getName());
        assertThat(testAccount.getBalance()).isEqualTo(newAccount.getBalance());
    }


}
