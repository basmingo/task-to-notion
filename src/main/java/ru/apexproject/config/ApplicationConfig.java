package ru.apexproject.config;

import lombok.Getter;
import java.io.*;
import java.util.Properties;

@Getter
public class ApplicationConfig {
    private final Properties properties;
    private final String botName;
    String botToken;

    private final String notionToken;
    private final String notionApiURI;
    private final String notionVersion;

    public ApplicationConfig() {
        properties = new Properties();
        try (InputStream input = new FileInputStream("${:classpath:chatsbase/chatsBase.json")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.botName = properties.getProperty("bot.name");
        this.botToken = properties.getProperty("bot.token");
        this.notionToken = properties.getProperty("notion.token");
        this.notionApiURI = properties.getProperty("notion.pages_api_uri");
        this.notionVersion = properties.getProperty("notion.version");
    }
}
