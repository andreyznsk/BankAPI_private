package ru.sberbank.main.modelDTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceDto {

    private BigDecimal balance;

    public BalanceDto(BigDecimal balance) {
        this.balance = balance;
    }
}
