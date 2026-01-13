package ru.otus;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;
import ru.otus.crm.service.DbServiceClientWithCacheImpl;
import ru.otus.crm.service.DbServiceManagerWithCacheImpl;
import ru.otus.jdbc.mapper.DataTemplateJdbc;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.impl.EntityClassMetaDataImpl;
import ru.otus.jdbc.mapper.impl.EntitySQLMetaDataImpl;
import ru.otus.jdbc.mapper.util.EntityReflection;
import ru.otus.util.WatchDogUtil;

@SuppressWarnings({"java:S125", "java:S1481"})
public class HomeWorkWithCache {
    private static final String URL = "jdbc:postgresql://192.168.1.90:5432/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWorkWithCache.class);

    public static void main(String[] args) {

        HwCache<Long, Client> clientHwCache = new MyCache<>();
        clientHwCache.addListener((key, value, action) -> log.info("Client: {}, {}, {}", key, value, action));

        HwCache<Long, Manager> managerHwCache = new MyCache<>();
        managerHwCache.addListener((key, value, action) -> log.info("Manager: {}, {}, {}", key, value, action));

        // Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        // Работа с клиентом
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<Client>(
                dbExecutor,
                entitySQLMetaDataClient,
                entityClassMetaDataClient,
                new EntityReflection<>(Client.class)); // реализация DataTemplate, универсальная

        // Код дальше должен остаться
        var dbServiceClient = new DbServiceClientWithCacheImpl(transactionRunner, dataTemplateClient, clientHwCache);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));

        Runnable getClient = () -> {
            var clientSecondSelected = dbServiceClient
                    .getClient(clientSecond.getId())
                    .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
            log.info("clientSecondSelected:{}", clientSecondSelected);
        };

        WatchDogUtil.runWithLog("Client with cache 1", getClient);
        WatchDogUtil.runWithLog("Client with cache 2", getClient);

        // Сделайте тоже самое с классом Manager (для него надо сделать свою таблицу)

        EntityClassMetaData<Manager> entityClassMetaDataManager = new EntityClassMetaDataImpl<>(Manager.class);
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl(entityClassMetaDataManager);
        var dataTemplateManager = new DataTemplateJdbc<Manager>(
                dbExecutor,
                entitySQLMetaDataManager,
                entityClassMetaDataManager,
                new EntityReflection<>(Manager.class));

        var dbServiceManager =
                new DbServiceManagerWithCacheImpl(transactionRunner, dataTemplateManager, managerHwCache);
        dbServiceManager.saveManager(new Manager("ManagerFirst"));

        var managerSecond = dbServiceManager.saveManager(new Manager("ManagerSecond"));

        Runnable getManager = () -> {
            var managerSecondSelected = dbServiceManager
                    .getManager(managerSecond.getNo())
                    .orElseThrow(() -> new RuntimeException("Manager not found, id:" + managerSecond.getNo()));
            log.info("managerSecondSelected:{}", managerSecondSelected);
        };

        WatchDogUtil.runWithLog("Manager with cache 1", getManager);
        WatchDogUtil.runWithLog("Manager with cache 2", getManager);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
