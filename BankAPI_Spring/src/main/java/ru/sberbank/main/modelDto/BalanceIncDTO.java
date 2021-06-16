package ru.sberbank.main.modelDto;

import lombok.Data;

@Data
public class BalanceIncDTO {
    private String cardNumber;
    private Double amount;
    private int cvc;
}
