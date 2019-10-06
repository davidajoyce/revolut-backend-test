package com.revolut.backend.db;

import com.revolut.backend.core.Account;
import com.revolut.backend.core.MoneyTransfer;
import com.revolut.backend.exception.ValidationException;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class MoneyTransferDAO extends AbstractDAO<MoneyTransfer> {

    public MoneyTransferDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<MoneyTransfer> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public MoneyTransfer create(MoneyTransfer moneyTransfer) throws ValidationException{
        return persist(moneyTransfer);
    }

    @SuppressWarnings("unchecked")
    public List<MoneyTransfer> findAll() {
        return list((Query<MoneyTransfer>) namedQuery("com.example.revolut.core.MoneyTransfer.findAll"));
    }

    public void removeAccount(MoneyTransfer moneyTransfer) {
        currentSession().delete(moneyTransfer);
    }

    public boolean checkTransfer(MoneyTransfer moneyTransfer, Account accountFrom, Account accountTo) throws ValidationException {

        if ( moneyTransfer.getAccountFrom() == moneyTransfer.getAccountTo() ){
            throw new ValidationException(402,"fromAccountId and toAccountId must not be equals");
        }

        if ( moneyTransfer.getValue().compareTo(BigDecimal.ZERO) < 1 ){
            throw new ValidationException(403,"Value transferred must be greater than 0");
        }

        BigDecimal newBalanceFromAccount = accountFrom.getBalance().subtract(moneyTransfer.getValue());

        if (newBalanceFromAccount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException(404, "The following account does not have enough money");
        }
        return true;
    }

}
