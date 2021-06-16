package ru.sberbank.main.modelDto;

import lombok.Data;

@Data
public class AccountNumberDTO {
    private String accountNumber;

    public AccountNumberDTO(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
