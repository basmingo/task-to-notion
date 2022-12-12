package ru.apexproject.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Properties;

@Getter
@Slf4j
public class ApplicationConfig {
    private static final String PROPERTIES_NAME = "/application.properties";
    private static final String RECOVER_PROPERTIES_NAME = "application.properties";
    private final File location;
    private final String parentLocation;
    private final Properties properties;
    private final String botName;
    private final String botToken;
    private final String notionToken;
    private final String notionApiURI;
    private final String notionVersion;
    private final String chatsDb;

    public ApplicationConfig() {
        properties = new Properties();
        location = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        parentLocation = location.getParentFile().getAbsolutePath();
        loadProperties();

        this.chatsDb = properties.getProperty("database.location");
        this.botName = properties.getProperty("bot.name");
        this.botToken = properties.getProperty("bot.token");
        this.notionToken = properties.getProperty("notion.token");
        this.notionApiURI = properties.getProperty("notion.pages_api_uri");
        this.notionVersion = properties.getProperty("notion.version");
    }

    private void loadProperties() {
        try (InputStream input = new FileInputStream(parentLocation + PROPERTIES_NAME)) {
            properties.load(input);

        } catch (IOException e) {
            log.error(e.getMessage());
            loadRecoverProperties();
        }
    }

    private void loadRecoverProperties() {
        try (InputStream input = this.getClass().getClassLoader().getResourceAsStream(RECOVER_PROPERTIES_NAME)) {
            properties.load(input);
            log.info("recover properties loaded");

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
