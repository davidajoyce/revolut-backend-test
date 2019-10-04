package com.revolut.backend.resources;

import com.revolut.backend.core.MoneyTransfer;
import com.revolut.backend.db.MoneyTransferDAO;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {

    private final MoneyTransferDAO moneyTransferDAO;

    public TransactionResource(MoneyTransferDAO moneyTransferDAO) {
        this.moneyTransferDAO = moneyTransferDAO;
    }

    @POST
    @UnitOfWork
    public MoneyTransfer createTransaction(MoneyTransfer moneyTransfer) {
        return moneyTransferDAO.create(moneyTransfer);
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
