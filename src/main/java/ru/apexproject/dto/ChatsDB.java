package ru.apexproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class ChatsDB {
    public List<Chat> chats;

    public ChatsDB(Map<String, String> map) {
        this.chats = new ArrayList<>();
        map.forEach((key, value) -> this.chats.add(new Chat(key, value)));
    }

    public void add(String a, String b) {
        this.chats.add(new Chat(a, b));
    }
}
