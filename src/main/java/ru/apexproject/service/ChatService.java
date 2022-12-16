package ru.apexproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.apexproject.config.ApplicationConfig;
import ru.apexproject.config.BotCommands;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * handling registration of chat messages, saving new chats in ChatsDb.json
 */
@Getter
@Slf4j
public class ChatService {
    private final JsonHandler jsonHandler;
    private final Map<String, String> chatDbMap;
    private final ObjectMapper mapper;
    private final ApplicationConfig applicationConfig;

    public ChatService(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
        this.jsonHandler = new JsonHandler(applicationConfig);
        this.chatDbMap = jsonHandler.loadDbFromJson();
        this.mapper = new ObjectMapper();
    }

    public void registerChat(Supplier<Stream<String>> streamSupplier) {
        Supplier<Stream<String>> parsedSupplier = parseSupplier(streamSupplier);
        String chatName = getChatName(parsedSupplier);
        String databaseId = getDatabaseId(parsedSupplier);

        if (!this.chatDbMap.containsKey(chatName)) {
            this.chatDbMap.put(chatName, databaseId);
            this.jsonHandler.writeDbToJson(this.chatDbMap);
        } else {
            log.info("invalid registration message");
        }
    }

    public void reRegisterChat(Supplier<Stream<String>> streamSupplier) {
        Supplier<Stream<String>> parsedSupplier = parseSupplier(streamSupplier);
        String chatName = getChatName(parsedSupplier);
        String databaseId = getDatabaseId(parsedSupplier);

        if (this.chatDbMap.containsKey(chatName)) {
            this.chatDbMap.put(chatName, databaseId);
            this.jsonHandler.writeDbToJson(this.chatDbMap);
        } else {
            log.info("no such database name");
        }
    }

    public void deleteChat(Supplier<Stream<String>> streamSupplier) {
        Supplier<Stream<String>> parsedSupplier = parseSupplier(streamSupplier);
        String chatName = getChatName(parsedSupplier);

        if (this.chatDbMap.containsKey(chatName)) {
            this.chatDbMap.remove(chatName);
            this.jsonHandler.writeDbToJson(this.chatDbMap);
        } else {
            log.info("invalid deletion message");
        }
    }

    public boolean chatDbContains(String chatName) {
        if (this.chatDbMap.containsKey(chatName)) return true;
        else {
            log.error("chat {} is unregistered in chatsDB", chatName);
            return false;
        }
    }

    private static Supplier<Stream<String>> parseSupplier(Supplier<Stream<String>> streamSupplier) {
        return () -> streamSupplier
                .get()
                .filter(word -> !word.equals(BotCommands.REGISTER_DATABASE))
                .filter(word -> !word.equals(BotCommands.REREGISTER_DATABASE))
                .filter(word -> !word.equals(BotCommands.DELETE_DATABASE))
                .filter(word -> !word.equals("="))
                .filter(word -> !word.contains("="));
    }

    private String getChatName(Supplier<Stream<String>> streamSupplier) {
        long wordsCount = streamSupplier.get().count();

        if (wordsCount > 0 && wordsCount < 3) {
            return streamSupplier.get().toList().get(0);
        }
        throw new IllegalArgumentException();
    }

    private String getDatabaseId(Supplier<Stream<String>> streamSupplier) {
        long wordsCount = streamSupplier.get().count();

        if (wordsCount > 0 && wordsCount < 3) {
            return streamSupplier.get().toList().get(1);
        }
        throw new IllegalArgumentException();
    }
}