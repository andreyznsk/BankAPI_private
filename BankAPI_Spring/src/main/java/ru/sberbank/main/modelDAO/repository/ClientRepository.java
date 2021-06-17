package ru.sberbank.main.modelDAO.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberbank.main.modelDAO.entity.Client;

public interface ClientRepository extends JpaRepository<Client,Long> {

    Client getClientByAccount_AccountNumber(String accountNumber);
}
