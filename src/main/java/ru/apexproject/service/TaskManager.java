package ru.apexproject.service;

import ru.apexproject.config.BotCommands;
import ru.apexproject.dto.notion.Task;
import ru.apexproject.config.ApplicationConfig;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
*Creates different types of Task object and sends it to NotionService
*/
public class TaskManager {
    ChatService chatService;
    NotionService notionService;
    ApplicationConfig botCommands;
    String description;
    List<String> assignees;

    public TaskManager(ChatService chatService,
                       NotionService notionService,
                       ApplicationConfig applicationConfig) {

        this.chatService = chatService;
        this.notionService = notionService;
        this.botCommands = applicationConfig;
    }

    public void createTask(Supplier<Stream<String>> messageStream, String projectName) {
        adjustParameters(messageStream);
        Task task = new Task(
                description,
                assignees,
                chatService.getChatDbMap().get(projectName));

        notionService.doPost(task);
    }

    public void createTask(
            Supplier<Stream<String>> messageStream,
            String projectName,
            String photo) {

        adjustParameters(messageStream);
        Task task = new Task(
                description,
                assignees,
                photo,
                chatService.getChatDbMap().get(projectName));
        notionService.doPost(task);
    }

    private void adjustParameters(Supplier<Stream<String>> messageStream) {
        description = messageStream
                .get()
                .filter(word -> word.charAt(0) != '@')
                .filter(word -> !word.equals(BotCommands.CREATE_TASK))
                .collect(Collectors.joining(" "));

        assignees = messageStream
                .get()
                .filter(word -> word.charAt(0) == '@')
                .collect(Collectors.toList());

        if (assignees.isEmpty()) assignees.add("@all");
    }
}
