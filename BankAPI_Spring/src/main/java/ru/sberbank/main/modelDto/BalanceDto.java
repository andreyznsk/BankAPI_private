package ru.sberbank.main.modelDto;

import lombok.Data;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Objects;

@Data
public class BalanceDto {

    private BigDecimal balance;

    public BalanceDto(BigDecimal balance) {
        this.balance = balance;
    }
}
