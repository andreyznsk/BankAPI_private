package ru.sber.bootcamp.exception;

public class BankApiException extends Exception {
    private String message;

    public BankApiException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
