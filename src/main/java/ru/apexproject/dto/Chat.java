package ru.apexproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Chat implements Serializable {
    public String chatId;
    public String databaseId;

    public Chat(String chatId, String databaseId) {
        this.chatId = chatId;
        this.databaseId = databaseId;
    }
}
