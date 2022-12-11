package ru.apexproject.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

@Getter
@Slf4j
public class ApplicationConfig {
    private final Properties properties;
    private final String botName;
    String botToken;

    private final String notionToken;
    private final String notionApiURI;
    private final String notionVersion;
    private final InputStream chatsDbInput;
    private PrintWriter chatsDbOutput;

    public ApplicationConfig() {
        properties = new Properties();
        try {
            properties.load(this.getClass()
                    .getClassLoader()
                    .getResourceAsStream("application.properties"));

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        chatsDbInput = this.getClass().getClassLoader().getResourceAsStream("chatsDB.json");
        try {
            this.chatsDbOutput = new PrintWriter((Objects
                    .requireNonNull(this.getClass()
                            .getClassLoader()
                            .getResource("chatsDB.json"))
                    .getPath()));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.botName = properties.getProperty("bot.name");
        this.botToken = properties.getProperty("bot.token");
        this.notionToken = properties.getProperty("notion.token");
        this.notionApiURI = properties.getProperty("notion.pages_api_uri");
        this.notionVersion = properties.getProperty("notion.version");

    }
}
