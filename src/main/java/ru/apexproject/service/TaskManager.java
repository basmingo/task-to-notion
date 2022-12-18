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
 * Creates different types of Task object and sends it to NotionService
 */
@Slf4j
public class TaskManager {
    private final ChatService chatService;
    private final NotionService notionService;
    public TaskManager(ChatService chatService, ApplicationConfig config) {
        this.chatService = chatService;
        this.notionService = new NotionService(config);
    }

    public void createTask(Supplier<Stream<String>> messageStream, String chatName) {
        if (chatService.chatDbContains(chatName)) {
            Task task = new Task(
                    getTaskMessage(messageStream),
                    getTaskAssignees(messageStream),
                    this.chatService.getChatDbMap().get(chatName));

            this.notionService.sendPost(task);
        } else throw new IllegalArgumentException();
    }

    public void createTask(
            Supplier<Stream<String>> messageStream,
            String projectName,
            String photo) {

        if (chatService.chatDbContains(projectName)) {
            Task task = new Task(
                    getTaskMessage(messageStream),
                    getTaskAssignees(messageStream),
                    photo,
                    this.chatService.getChatDbMap().get(projectName));

            this.notionService.sendPost(task);
        } else throw new IllegalArgumentException();
    }

    private List<String> getTaskAssignees(Supplier<Stream<String>> messageStream) {
        List<String> parsedTaskAssignees =  messageStream
                .get()
                .filter(word -> word.charAt(0) == '@')
                .toList();

        if (parsedTaskAssignees.isEmpty()) {
            return List.of("@all");
        }
        return parsedTaskAssignees;
    }

    private String getTaskMessage(Supplier<Stream<String>> messageStream) {
        return messageStream
                .get()
                .filter(word -> word.charAt(0) != '@')
                .filter(word -> !word.equals(BotCommands.CREATE_TASK))
                .collect(Collectors.joining(" "));
    }
}
