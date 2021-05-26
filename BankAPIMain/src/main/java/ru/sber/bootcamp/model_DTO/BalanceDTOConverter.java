package ru.sber.bootcamp.model_DTO;

import ru.sber.bootcamp.model_DAO.entity.Account;

public class BalanceDTOConverter {

    public BalanceDTO balanceDTO(Account account) {
        return new BalanceDTO(account.getBalance());
    }

}
