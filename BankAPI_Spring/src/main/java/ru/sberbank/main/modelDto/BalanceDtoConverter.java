package ru.sberbank.main.modelDto;


import org.springframework.stereotype.Component;
import ru.sberbank.main.modelDao.entity.Account;

@Component
public class BalanceDtoConverter {

    public BalanceDto balanceDTO(Account account) {
        return new BalanceDto(account.getBalance());
    }

}
