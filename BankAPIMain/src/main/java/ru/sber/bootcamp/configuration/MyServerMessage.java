package ru.sber.bootcamp.configuration;

public enum MyServerMessage {
    ERROR_MESSAGE("Error!"),
    SERVER_OK("Server_OK!"),
    SERVER_ERROR("Server_error!"),
    DATA_ERROR("Data_format_error");

    public final String message;
    MyServerMessage(String message) {
        this.message = message;
    }

}
