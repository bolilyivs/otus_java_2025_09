package ru.otus.crm.service;

import java.util.List;
import ru.otus.crm.model.Client;

public interface ClientService {

    Client saveClient(Client client);

    List<Client> findAll();
}
