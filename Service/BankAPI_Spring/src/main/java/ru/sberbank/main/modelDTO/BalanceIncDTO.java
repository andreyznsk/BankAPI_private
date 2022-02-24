package ru.sberbank.main.modelDTO;

import lombok.Data;

@Data
public class BalanceIncDTO {
    private String cardNumber;
    private Double amount;
    private int cvc;
}
