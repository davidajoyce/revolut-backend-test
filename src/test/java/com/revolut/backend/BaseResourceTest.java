package com.revolut.backend;

import com.revolut.backend.core.Account;
import com.revolut.backend.core.MoneyTransfer;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
public class BaseResourceTest {
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

    public Account postAccount(Account account) {
        return RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/account")
                .request()
                .post(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Account.class);
    }

    public Response getAccounts() {
        return RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/account")
                .request()
                .get();
    }

    public Account getAccountById(Account newAccount) {
        final String url = "http://localhost:" + RULE.getLocalPort() + "/account/" + newAccount.getId();
        Response response = RULE.client().target(url).request().get();
        Account account = response.readEntity(Account.class);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
        return account;
    }

    public void deleteAccount(Account newAccount) {
        final String url = "http://localhost:" + RULE.getLocalPort() + "/account/" + newAccount.getId();
        Response response = RULE.client().target(url).request().delete();
    }

    public MoneyTransfer postTransaction(Long fromAccountId, Long toAccountId, BigDecimal value){
        MoneyTransfer moneyTransfer = new MoneyTransfer();

        return moneyTransfer;
    }

}
