package ru.apexproject.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BotCommands {
    CREATE_TASK(ApplicationConfig.TASK_COMMAND),
    REGISTER_DATABASE(ApplicationConfig.REGISTER_COMMAND),
    REREGISTER_DATABASE(ApplicationConfig.REREGISTER_COMMAND),
    DELETE_DATABASE(ApplicationConfig.DELETE_COMMAND);
    private final String message;
}
