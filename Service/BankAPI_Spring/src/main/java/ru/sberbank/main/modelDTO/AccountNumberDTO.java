package ru.sberbank.main.modelDTO;

import lombok.Data;

@Data
public class AccountNumberDTO {
    private String accountNumber;

    public AccountNumberDTO(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
