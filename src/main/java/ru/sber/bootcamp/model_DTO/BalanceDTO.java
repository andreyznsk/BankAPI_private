package ru.sber.bootcamp.model_DTO;

import java.math.BigDecimal;
import java.util.Date;

public class BalanceDTO {

    private BigDecimal balance;

    public BalanceDTO(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
