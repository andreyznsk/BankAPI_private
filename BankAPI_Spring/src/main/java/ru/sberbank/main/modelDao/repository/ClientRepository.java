package ru.sberbank.main.modelDao.repository;


import org.hibernate.secure.spi.JaccPermissionDeclarations;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberbank.main.modelDao.entity.Client;

public interface ClientRepository extends JpaRepository<Client,Long> {

    Client getClientByAccount_AccountNumber(String accountNumber);
}
