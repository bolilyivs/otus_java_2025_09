package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Manager;

public class DbServiceManagerWithCacheImpl implements DBServiceManager {
    private static final Logger log = LoggerFactory.getLogger(DbServiceManagerWithCacheImpl.class);

    private final DataTemplate<Manager> managerDataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<Long, Manager> hwCache;

    public DbServiceManagerWithCacheImpl(
            TransactionRunner transactionRunner,
            DataTemplate<Manager> managerDataTemplate,
            HwCache<Long, Manager> hwCache) {
        this.transactionRunner = transactionRunner;
        this.managerDataTemplate = managerDataTemplate;
        this.hwCache = hwCache;
    }

    @Override
    public Manager saveManager(Manager manager) {
        return transactionRunner.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                var managerNo = managerDataTemplate.insert(connection, manager);
                var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                log.info("created manager: {}", createdManager);
                return createdManager;
            }
            managerDataTemplate.update(connection, manager);
            log.info("updated manager: {}", manager);
            return manager;
        });
    }

    @Override
    public Optional<Manager> getManager(long no) {
        return transactionRunner.doInTransaction(connection -> {
            var managerOptional = Optional.ofNullable(hwCache.get(no));
            if (managerOptional.isPresent()) {
                return managerOptional;
            }

            managerOptional = managerDataTemplate.findById(connection, no);
            log.info("manager: {}", managerOptional);
            managerOptional.ifPresent(entity -> hwCache.put(no, entity));
            return managerOptional;
        });
    }

    @Override
    public List<Manager> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var managerList = managerDataTemplate.findAll(connection);
            log.info("managerList:{}", managerList);
            return managerList;
        });
    }
}
