package ru.sber.bootcamp.modelDao.repository;

import ru.sber.bootcamp.modelDao.entity.Client;

public interface ClientRepository {

    Client getClientByAccountNumber(String accountNumber);
}
