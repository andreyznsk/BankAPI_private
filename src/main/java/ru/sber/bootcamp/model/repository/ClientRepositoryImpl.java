package ru.sber.bootcamp.model.repository;

import ru.sber.bootcamp.model.entity.Client;
import ru.sber.bootcamp.service.DataConnectionService;

public class ClientRepositoryImpl implements ClientRepository {
    private final DataConnectionService dataConnectionService;

    public ClientRepositoryImpl(DataConnectionService dataConnectionService) {
        this.dataConnectionService = dataConnectionService;
    }


    @Override
    public Client getClientByAccountNumber(Long accountNumber) {
        return dataConnectionService.getClientByAccountNumber(accountNumber);
    }
}
