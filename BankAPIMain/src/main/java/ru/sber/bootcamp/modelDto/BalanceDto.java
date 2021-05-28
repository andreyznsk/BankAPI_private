package ru.sber.bootcamp.modelDto;

import java.math.BigDecimal;
import java.util.Objects;

public class BalanceDto {

    private BigDecimal balance;

    public BalanceDto(BigDecimal balance) {
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
        if (!(o instanceof BalanceDto)) return false;
        BalanceDto that = (BalanceDto) o;
        return Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance);
    }

    @Override
    public String toString() {
        return "BalanceDTO{" +
                "balance=" + balance +
                '}';
    }
}
