package com.revolut.backend.resources;

import com.revolut.backend.core.Account;
import com.revolut.backend.core.MoneyTransfer;
import com.revolut.backend.db.AccountDAO;
import com.revolut.backend.db.MoneyTransferDAO;
import com.revolut.backend.exception.ValidationException;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {

    private final MoneyTransferDAO moneyTransferDAO;
    private final AccountDAO accountDAO;

    public TransactionResource(MoneyTransferDAO moneyTransferDAO, AccountDAO accountdao) {
        this.moneyTransferDAO = moneyTransferDAO;
        this.accountDAO = accountdao;
    }

    @POST
    @UnitOfWork
    public MoneyTransfer createTransaction(MoneyTransfer moneyTransfer) throws ValidationException {
        Account accountFrom = accountDAO.findSafely(moneyTransfer.getAccountFrom());
        Account accountTo = accountDAO.findSafely(moneyTransfer.getAccountTo());

        if( moneyTransferDAO.checkTransfer( moneyTransfer, accountFrom, accountTo ) ){
            BigDecimal newBalanceFromAccount = accountFrom.getBalance().subtract(moneyTransfer.getValue());
            BigDecimal newBalanceToAccount = accountTo.getBalance().add(moneyTransfer.getValue());
            accountDAO.updateBalance(moneyTransfer.getAccountFrom(), newBalanceFromAccount);
            accountDAO.updateBalance(moneyTransfer.getAccountTo(), newBalanceToAccount);
            return moneyTransferDAO.create(moneyTransfer);
        }
        MoneyTransfer failedMoneyTransfer = new MoneyTransfer(accountFrom.getId(),accountTo.getId(),null);
        return failedMoneyTransfer;
    }

    @GET
    @UnitOfWork
    public List<MoneyTransfer> listTransactions() {
        return moneyTransferDAO.findAll();
    }

    @GET
    @Path("/{transactionId}")
    @UnitOfWork
    public MoneyTransfer getTransaction(@PathParam("transactionId") Long transactionId) {
        return findSafely(transactionId);
    }

    @DELETE
    @Path("/{transactionId}")
    @UnitOfWork
    public Response removeAccountById(@PathParam("transactionId") Long transactionId){
        MoneyTransfer moneyTransfer = findSafely(transactionId);
        if(moneyTransfer != null) {
            moneyTransferDAO.removeAccount(moneyTransfer);
            return Response.ok().build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    private MoneyTransfer findSafely(long transactionId) {
        return moneyTransferDAO.findById(transactionId).orElseThrow(() -> new NotFoundException("No such transaction."));
    }
}
