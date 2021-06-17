package ru.sberbank.main.modelDTO;

import lombok.Data;

@Data
public class ServerResponseDTO {

    private String ServerMessage;

    public ServerResponseDTO(String serverMessage) {
        ServerMessage = serverMessage;
    }
}
