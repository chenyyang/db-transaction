package com.chenyang.db.transaction.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;


public class DynamicDataSourceTransactionManager extends DataSourceTransactionManager {

    private DynamicDataSource daynamicDataSource;

    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceTransactionManager.class);

    /**
     * Return the JDBC DataSource that this instance manages transactions for.
     */
    @Override
    public DataSource getDataSource() {
        return daynamicDataSource.getDataSource();
    }

    public DynamicDataSource getDaynamicDataSource() {
        return daynamicDataSource;
    }

    public void setDaynamicDataSource(DynamicDataSource daynamicDataSource) {
        this.daynamicDataSource = daynamicDataSource;
        super.setDataSource(daynamicDataSource);
    }

    protected void doBegin(Object transaction, TransactionDefinition definition) {
        logger.debug("  doBegin ");
        super.doBegin(transaction, definition);
        TransactionSynchronizationManager.bindResource(super.getDataSource(),
            TransactionSynchronizationManager.getResource(getDataSource()));
    }

    protected void doCleanupAfterCompletion(Object transaction) {
        logger.debug("  doCleanupAfterCompletion ");
        TransactionSynchronizationManager.unbindResource(getDataSource());
        super.doCleanupAfterCompletion(transaction);
    }

}
