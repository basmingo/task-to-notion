package ru.apexproject.service;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TaskManager {
    private final ChatService chatService;
    private final NotionService notionService;
    private String description;
    private List<String> assignees;

    public TaskManager(ChatService chatService,
                       ApplicationConfig applicationConfig) {

        this.chatService = chatService;
        this.notionService = new NotionService(applicationConfig);
    }

    public void createTask(Supplier<Stream<String>> messageStream, String projectName) {
        if (chatService.chatDbContains(projectName)) {
            adjustParameters(messageStream);
            Task task = new Task(
                    this.description,
                    this.assignees,
                    this.chatService.getChatDbMap().get(projectName));

            this.notionService.sendPost(task);
        }
    }

    public void createTask(
            Supplier<Stream<String>> messageStream,
            String projectName,
            String photo) {

        if (chatService.chatDbContains(projectName)) {
            adjustParameters(messageStream);
            Task task = new Task(
                    this.description,
                    this.assignees,
                    photo,
                    this.chatService.getChatDbMap().get(projectName));

            this.notionService.sendPost(task);
        }
    }

    private void adjustParameters(Supplier<Stream<String>> messageStream) {
        this.description = messageStream
                .get()
                .filter(word -> word.charAt(0) != '@')
                .filter(word -> !word.equals(BotCommands.CREATE_TASK))
                .collect(Collectors.joining(" "));

        this.assignees = messageStream
                .get()
                .filter(word -> word.charAt(0) == '@')
                .collect(Collectors.toList());

        if (this.assignees.isEmpty()) {
            this.assignees.add("@all");
        }
    }
}
