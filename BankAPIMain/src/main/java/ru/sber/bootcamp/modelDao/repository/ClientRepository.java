package ru.sber.bootcamp.modelDao.repository;

import ru.sber.bootcamp.exception.BankApiException;
import ru.sber.bootcamp.modelDao.entity.Client;

public interface ClientRepository {

    Client getClientByAccountNumber(String accountNumber) throws BankApiException;
}
