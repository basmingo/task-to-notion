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
    GetFile getFile;
    ApplicationConfig config;
    TaskManager taskManager;
    Long chatId;
    ChatService chatService;
    BotCommands botCommands;
    String telegramImageUrl;

    public TelegramBot() {
        this.config = new ApplicationConfig();
        this.getFile = new GetFile();
        this.chatService = new ChatService(this.config);
        this.taskManager = new TaskManager(this.chatService, this.config);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (isValid(update)) {
            Message message = update.getMessage();
            String chatName = message.getChat().getTitle();

            this.chatId = message.getChatId();
            final Supplier<Stream<String>> wordsInMessage = getStreamSupplier(message);

            if (includeTaskCommand(wordsInMessage) && message.hasPhoto()) {
                List<PhotoSize> a = message.getPhoto();
                getFile.setFileId(a.get(a.size()-1).getFileId());
                try {
                    telegramImageUrl = execute(getFile).getFileUrl(getBotToken());
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
                .anyMatch(word -> word.equals(BotCommands.CREATE_TASK));
    }

    private boolean includeRegisterCommand(Supplier<Stream<String>> wordsInMessage) {
        return wordsInMessage
                .get()
                .anyMatch(word -> word.equals(BotCommands.REGISTER_DATABASE));
    }

    private boolean includeReregisterCommand(Supplier<Stream<String>> wordsInMessage) {
        return wordsInMessage
                .get()
                .anyMatch(word -> word.equals(BotCommands.REREGISTER_DATABASE));
    }

    private boolean includeDeleteCommand(Supplier<Stream<String>> wordsInMessage) {
        return wordsInMessage
                .get()
                .anyMatch(word -> word.equals(BotCommands.DELETE_DATABASE));
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }
}
