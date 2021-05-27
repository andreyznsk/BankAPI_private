package ru.sber.bootcamp.configuration;

public enum MyErrorMessage {
    ERROR_MESSAGE("Error!"),
    SERVER_OK("Server_OK!"),
    SERVER_ERROR("Server_error!"),
    DATA_ERROR("Data_format_error");

    public final String message;
    MyErrorMessage(String message) {
        this.message = message;
    }

}
