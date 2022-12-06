package ru.apexproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.apexproject.config.ApplicationConfig;
import ru.apexproject.config.BotCommands;
import ru.apexproject.dto.ChatsDB;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;
@Getter
@Slf4j
public class ChatService {
    ApplicationConfig applicationConfig;
    BotCommands botCommands;
    ObjectMapper mapper;
    ChatsDB chatDB;
    private final Map<String, String> chatDbMap;

    public ChatService(ApplicationConfig applicationConfig, BotCommands botCommands) {
        this.botCommands = botCommands;
        this.applicationConfig = applicationConfig;
        this.mapper = new ObjectMapper();
        this.chatDbMap = readDbFromJson();
    }

    public void registerChat(Supplier<Stream<String>> streamSupplier) {
        Supplier<Stream<String>> parsedData = () -> streamSupplier
                .get()
                .filter(word -> !word.equals(BotCommands.REGISTER_DATABASE))
                .filter(word -> !word.equals("="))
                .filter(word -> !word.contains("="));

        if (parsedData.get().count() == 2) {
            addToDB(parsedData.get().toList());
        }
        else {
            log.info("invalid registration message");
        }
    }

    public void reRegisterChat(Supplier<Stream<String>> streamSupplier) {
        Supplier<Stream<String>> parsedData = () -> streamSupplier
                .get()
                .filter(word -> !word.equals(BotCommands.REREGISTER_DATABASE))
                .filter(word -> !word.equals("="))
                .filter(word -> !word.contains("="));

        if (parsedData.get().count() == 2) {
            rewriteToDB(parsedData.get().toList());
        }
        else {
            log.info("invalid reregistration message");
        }
    }

    public void deleteChat(Supplier<Stream<String>> streamSupplier) {
        Supplier<Stream<String>> parsedData = () -> streamSupplier
                .get()
                .filter(word -> !word.equals(BotCommands.DELETE_DATABASE));

        if (parsedData.get().count() == 1) {
            deleteFromDB(parsedData.get().toList());
        }
        else {
            log.info("invalid deletion message");
        }
    }

    private void addToDB(List<String> data) {
        String chatName = data.get(0);
        String databaseId = data.get(1);

        if (!this.chatDbMap.containsKey(chatName)) {
            this.chatDbMap.put(chatName, databaseId);
            writeDbToJson(this.chatDbMap);
        } else {
            log.info("name or database id is already in DB");
        }
    }

    private void rewriteToDB(List<String> data) {
        String chatName = data.get(0);
        String databaseId = data.get(1);

        if (this.chatDbMap.containsKey(chatName)) {
            this.chatDbMap.put(chatName, databaseId);
            writeDbToJson(this.chatDbMap);
        } else {
            log.info("no such database name");
        }
    }

    private void deleteFromDB(List<String> data) {
        String chatName = data.get(0);

        if (this.chatDbMap.containsKey(chatName)) {
            this.chatDbMap.remove(chatName);
            writeDbToJson(this.chatDbMap);
        } else {
            log.info("no such database name");
        }
    }

    private Map<String, String> readDbFromJson() {
        Map<String, String> result = new HashMap<>();
        try {
            chatDB = mapper.readValue(applicationConfig.getChatsDbInput(), ChatsDB.class);

            chatDB.getChats().forEach(item -> result.put(item.chatId, item.databaseId));
        } catch (IOException e) {
            e.printStackTrace();
            log.info("can't read chats");
        }
        return result;
    }

    private void writeDbToJson(Map<String, String> map) {
        try {
            this.chatDB = new ChatsDB(map);
            mapper.writeValue(applicationConfig.getChatsDbOutput(), this.chatDB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}