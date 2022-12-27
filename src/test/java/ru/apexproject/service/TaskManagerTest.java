package ru.apexproject.service;

import org.junit.jupiter.api.Test;
import ru.apexproject.config.ApplicationConfig;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private final String TEST_TASK = "validation test";
    private final String TEST_CHAT = "BotTest2";
    private final ChatService chatService = new ChatService();
    private final TaskManager taskManager = new TaskManager(chatService);

    @Test
    void createTask() {
        Supplier<Stream<String>> testMessageSupplier = () -> Arrays.stream(TEST_TASK.split(" "));

        assertDoesNotThrow(() -> taskManager.createTask(testMessageSupplier, TEST_CHAT));
        assertThrows(IllegalArgumentException.class,
                () -> taskManager.createTask(testMessageSupplier, "invalidChat"));
    }
}