package ru.otus.crm.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.crm.model.Client;

@Repository
public interface ClientRepository extends ListCrudRepository<Client, Long> {}
