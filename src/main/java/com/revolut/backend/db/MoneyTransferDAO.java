package com.revolut.backend.db;

import com.revolut.backend.core.MoneyTransfer;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class MoneyTransferDAO extends AbstractDAO<MoneyTransfer> {

    public MoneyTransferDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<MoneyTransfer> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public MoneyTransfer create(MoneyTransfer moneyTransfer) {
        return persist(moneyTransfer);
    }

    @SuppressWarnings("unchecked")
    public List<MoneyTransfer> findAll() {
        return list((Query<MoneyTransfer>) namedQuery("com.example.helloworld.core.MoneyTransfer.findAll"));
    }

    public void removeAccount(MoneyTransfer moneyTransfer) {
        currentSession().delete(moneyTransfer);
    }

}
