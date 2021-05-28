package ru.sber.bootcamp.modelDto;

import ru.sber.bootcamp.modelDao.entity.Account;

public class BalanceDtoConverter {

    public BalanceDto balanceDTO(Account account) {
        return new BalanceDto(account.getBalance());
    }

}
