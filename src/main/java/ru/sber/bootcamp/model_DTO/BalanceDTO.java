package ru.sber.bootcamp.model_DTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BalanceDTO)) return false;
        BalanceDTO that = (BalanceDTO) o;
        return Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance);
    }
}
