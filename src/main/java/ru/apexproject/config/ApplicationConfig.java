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
    private static final Properties PROPERTIES;
    public static final File LOCATION;
    public static final String PARENT_LOCATION;
    public static final String BOT_NAME;
    public static final String BOT_TOKEN;
    public static final String NOTION_TOKEN;
    public static final String NOTION_API_URI;
    public static final String NOTION_VERSION;
    public static final String CHATS_DB;

    public static final ClassLoader CLASS_LOADER;

    public static final String TASK_COMMAND;
    public static final String REGISTER_COMMAND;
    public static final String REREGISTER_COMMAND;
    public static final String DELETE_COMMAND;
    public static final String EQUALS_COMMAND;

    static {
        PROPERTIES = new Properties();
        LOCATION = new File(ApplicationConfig.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath());

        PARENT_LOCATION = LOCATION.getParentFile().getAbsolutePath();
        CLASS_LOADER = ApplicationConfig.class.getClassLoader();

        loadProperties();
        CHATS_DB = PROPERTIES.getProperty("database.location");

        BOT_NAME = PROPERTIES.getProperty("bot.name");
        BOT_TOKEN = PROPERTIES.getProperty("bot.token");

        NOTION_TOKEN = PROPERTIES.getProperty("notion.token");
        NOTION_API_URI = PROPERTIES.getProperty("notion.pages_api_uri");
        NOTION_VERSION = PROPERTIES.getProperty("notion.version");

        TASK_COMMAND = PROPERTIES.getProperty("commands.task");
        REGISTER_COMMAND = PROPERTIES.getProperty("commands.register");
        REREGISTER_COMMAND = PROPERTIES.getProperty("commands.reregister");
        DELETE_COMMAND = PROPERTIES.getProperty("commands.delete");
        EQUALS_COMMAND = PROPERTIES.getProperty("commands.equals");
    }

    private static void loadProperties() {
        try (InputStream input = new FileInputStream(PARENT_LOCATION + PROPERTIES_NAME)) {
            PROPERTIES.load(input);
        } catch (IOException e) {
            log.error(e.getMessage());
            loadRecoverProperties();
        }
    }

    private static void loadRecoverProperties() {
        try (InputStream input = CLASS_LOADER.getResourceAsStream(RECOVER_PROPERTIES_NAME)) {
            PROPERTIES.load(input);
            log.info("recover properties loaded");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
