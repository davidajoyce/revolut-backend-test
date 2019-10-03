package com.revolut.backend.resources;

import com.revolut.backend.core.Account;
import com.revolut.backend.db.AccountDAO;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    private final AccountDAO accountDAO;

    public AccountResource(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @POST
    @UnitOfWork
    public Account createAccount(Account account) {
        return accountDAO.create(account);
    }

    @GET
    @UnitOfWork
    public List<Account> listPeople() {
        return accountDAO.findAll();
    }

}
