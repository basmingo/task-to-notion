package ru.apexproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ru.apexproject.config.ApplicationConfig;
import ru.apexproject.dto.ChatsDB;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * load map with chats data from json, write map with chats to json,
 */
@Slf4j
public class JsonHandler {
    private ChatsDB chatsDB;
    private final ObjectMapper mapper;
    private final String chatsDbOutsideJarPath;


    public JsonHandler() {
        this.chatsDbOutsideJarPath = ApplicationConfig.PARENT_LOCATION
                + ApplicationConfig.CHATS_DB;

        this.mapper = new ObjectMapper();
        this.chatsDB = new ChatsDB();
    }

    public Map<String, String> loadDbFromJson() {
        Map<String, String> result = new HashMap<>();
        try (InputStream reader = new FileInputStream(chatsDbOutsideJarPath)) {
            chatsDB = mapper.readValue(reader, ChatsDB.class);

        } catch (IOException e) {
            log.error("reading ChatsDb.json failed");
            log.error(e.getMessage());
            loadDbFromRecoverJson();
        }

        chatsDB.getChats().forEach(item -> result.put(item.chatId, item.databaseId));
        return result;
    }

    public void writeDbToJson(Map<String, String> map) {
        try (OutputStream writer = new FileOutputStream(chatsDbOutsideJarPath)) {
            chatsDB = new ChatsDB(map);
            mapper.writeValue(writer, chatsDB);

        } catch (IOException e) {
            log.error("writing ChatsDb.json failed");
            log.error(e.getMessage());
        }
    }

    private void loadDbFromRecoverJson() {
        chatsDB = new ChatsDB();
        try (InputStream reader = ApplicationConfig.CLASS_LOADER
                .getResourceAsStream(ApplicationConfig.CHATS_DB)) {

            chatsDB = mapper.readValue(reader, ChatsDB.class);
            log.info("recover ChatsDb.json loaded");

        } catch (IOException e) {
            log.error("reading recover ChatsDb.json failed");
            log.error(e.getMessage());
        }
    }
}
