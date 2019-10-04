package com.revolut.backend.resources;

import com.revolut.backend.core.Account;
import com.revolut.backend.db.AccountDAO;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public List<Account> listAccounts() {
        return accountDAO.findAll();
    }

    @GET
    @Path("/{accountId}")
    @UnitOfWork
    public Account getAccount(@PathParam("accountId") Long accountId) {
        return findSafely(accountId);
    }

    @DELETE
    @Path("/{accountId}")
    @UnitOfWork
    public Response removeAccountById(@PathParam("accountId") Long accountId){
        Account account = findSafely(accountId);
        if(account != null) {
            accountDAO.removeAccount(account);
            return Response.ok().build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

    private Account findSafely(long accountId) {
        return accountDAO.findById(accountId).orElseThrow(() -> new NotFoundException("No such account."));
    }
}
