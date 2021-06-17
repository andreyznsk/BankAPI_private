package ru.sberbank.main.modelDAO.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberbank.main.modelDAO.entity.Account;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAll();

 /*   @Query(name = "SELECT A.* from CARD " +
            "join ACCOUNT A on A.ACCOUNT_NUMBER = CARD.ACCOUNT_NUMBER " +
            "WHERE CARD_NUMBER =?1", nativeQuery = true)
     Account specfindAccountByCardNumber(String cardNumber);*/

    Account getAccountById(long id);

    Account getAccountByAccountNumber(String accountNumber);
}
