package ru.sber.bootcamp.model.repository;

import ru.sber.bootcamp.model.entity.Client;

public interface ClientRepository {

    Client getClientByAccountNumber(Long accountNumber);
}
