package ru.sberbank.main.modelDTO;


import org.springframework.stereotype.Component;
import ru.sberbank.main.modelDAO.entity.Account;

@Component
public class BalanceDtoConverter {

    public BalanceDto balanceDTO(Account account) {
        return new BalanceDto(account.getBalance());
    }

}
