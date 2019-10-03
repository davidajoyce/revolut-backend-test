package com.revolut.backend;

import com.revolut.backend.core.Account;
import com.revolut.backend.db.AccountDAO;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MoneyTransferApplication extends Application<MoneyTransferConfiguration> {
    public static void main(String[] args) throws Exception {
        new MoneyTransferApplication().run(args);
    }

    private final HibernateBundle<MoneyTransferConfiguration> hibernateBundle =
            new HibernateBundle<MoneyTransferConfiguration>(Account.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(MoneyTransferConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<MoneyTransferConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        bootstrap.addBundle(new MigrationsBundle<MoneyTransferConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(MoneyTransferConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(MoneyTransferConfiguration configuration, Environment environment) {
        final AccountDAO accountdao = new AccountDAO(hibernateBundle.getSessionFactory());
    }
}

