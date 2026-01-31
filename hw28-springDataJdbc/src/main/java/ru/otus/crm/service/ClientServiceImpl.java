package ru.otus.crm.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.crm.core.TransactionManager;
import ru.otus.crm.model.Client;
import ru.otus.crm.repository.ClientRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final TransactionManager transactionManager;
    private final ClientRepository clientRepository;

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(() -> clientRepository.save(client));
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInTransaction(clientRepository::findAll);
    }
}
