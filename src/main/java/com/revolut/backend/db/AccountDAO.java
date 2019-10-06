package com.revolut.backend.db;

import com.revolut.backend.core.Account;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class AccountDAO extends AbstractDAO<Account> {
    public AccountDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public Account create(Account account) {
        return persist(account);
    }

    @SuppressWarnings("unchecked")
    public List<Account> findAll() {
        return list((Query<Account>) namedQuery("com.example.revolut.core.Account.findAll"));
    }

    public void removeAccount(Account account) {
        currentSession().delete(account);
    }

    public void updateBalance(long accountId, BigDecimal newBalance) {
        Account account = findSafely(accountId);
        account.setBalance(newBalance);
        currentSession().update(account);
   }

    public Account findSafely(long accountId) {
        return findById(accountId).orElseThrow(() -> new NotFoundException("No such account."));
    }

}
