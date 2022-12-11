package ru.apexproject.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Properties;

@Getter
@Slf4j
public class ApplicationConfig {
    private final Properties properties;
    private final String botName;
    private final String botToken;
    private final String notionToken;
    private final String notionApiURI;
    private final String notionVersion;
    private final String chatsDb;

    public ApplicationConfig() {
        properties = new Properties();
        try (InputStream input = new FileInputStream("application.properties")){
            properties.load(input);

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        this.chatsDb = properties.getProperty("database.location");
        this.botName = properties.getProperty("bot.name");
        this.botToken = properties.getProperty("bot.token");
        this.notionToken = properties.getProperty("notion.token");
        this.notionApiURI = properties.getProperty("notion.pages_api_uri");
        this.notionVersion = properties.getProperty("notion.version");

    }
}
