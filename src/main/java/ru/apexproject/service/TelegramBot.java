package ru.apexproject.service;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.apexproject.config.ApplicationConfig;
import ru.apexproject.config.BotCommands;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**Telegram bot massages handler
 * */

@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private final GetFile getFile = new GetFile();
    private final ChatService chatService = new ChatService();
    private final TaskManager taskManager = new TaskManager(this.chatService);

    @Override
    public void onUpdateReceived(Update update) {
        if (isValid(update)) {
            Message message = update.getMessage();
            String chatName = message.getChat().getTitle();
            final Supplier<Stream<String>> wordsInMessage = getStreamSupplier(message);

            if (includeTaskCommand(wordsInMessage) && message.hasPhoto()) {
                List<PhotoSize> a = message.getPhoto();
                getFile.setFileId(a.get(a.size()-1).getFileId());
                try {
                    String telegramImageUrl = execute(getFile).getFileUrl(getBotToken());
                    taskManager.createTask(wordsInMessage, chatName, telegramImageUrl);
                } catch (TelegramApiException e) {
                    log.error(e.getMessage());
                }

            } else if (includeTaskCommand(wordsInMessage)) {
                taskManager.createTask(wordsInMessage, chatName);

            } else if (includeRegisterCommand(wordsInMessage)) {
                chatService.registerChat(wordsInMessage);

            } else if (includeReregisterCommand(wordsInMessage)) {
                chatService.reRegisterChat(wordsInMessage);

            } else if (includeDeleteCommand(wordsInMessage)) {
                chatService.deleteChat(wordsInMessage);
            }
        }
    }

    private Supplier<Stream<String>> getStreamSupplier(Message message) {
        String[] messageWords = (message.hasPhoto()) ?
                message.getCaption().split(" ") :
                message.getText().split(" ");
        return () -> Arrays.stream(messageWords);
    }

    private boolean isValid(Update update) {
        return update.hasMessage()
                && update.getMessage().hasText()
                || update.getMessage().getCaption() != null;
    }

    private boolean includeTaskCommand(Supplier<Stream<String>> streamSupplier) {
        return streamSupplier
                .get()
                .anyMatch(word -> word.equals(BotCommands.CREATE_TASK.getMessage()));
    }

    private boolean includeRegisterCommand(Supplier<Stream<String>> wordsInMessage) {
        return wordsInMessage
                .get()
                .anyMatch(word -> word.equals(BotCommands.REGISTER_DATABASE.getMessage()));
    }

    private boolean includeReregisterCommand(Supplier<Stream<String>> wordsInMessage) {
        return wordsInMessage
                .get()
                .anyMatch(word -> word.equals(BotCommands.REREGISTER_DATABASE.getMessage()));
    }

    private boolean includeDeleteCommand(Supplier<Stream<String>> wordsInMessage) {
        return wordsInMessage
                .get()
                .anyMatch(word -> word.equals(BotCommands.DELETE_DATABASE.getMessage()));
    }

    @Override
    public String getBotUsername() {
        return ApplicationConfig.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return ApplicationConfig.BOT_TOKEN;
    }
}
