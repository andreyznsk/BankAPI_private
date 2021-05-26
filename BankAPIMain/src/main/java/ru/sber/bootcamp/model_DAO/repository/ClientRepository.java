package ru.sber.bootcamp.model_DAO.repository;

import ru.sber.bootcamp.model_DAO.entity.Client;

public interface ClientRepository {

    Client getClientByAccountNumber(Long accountNumber);
}
