package ru.sberbank.main.modelDAO.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberbank.main.modelDAO.entity.Card;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAll();
    Optional<Card> getCardByCardNumber(String cardNumber);

    Card getCardById(Long id);

    List<Card> getAllCardsByAccountNumber(String accountNumber);

    //int addCardByAccountNumber(Card card);

    boolean existsByCardNumber(String cartNumber);
    //boolean isCardExist(String cartNumber);

}
