import org.junit.jupiter.api.*;
import ru.apexproject.config.ApplicationConfig;
import ru.apexproject.dto.ChatsDB;
import ru.apexproject.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChatServiceTest {
    private final ApplicationConfig applicationConfig = new ApplicationConfig();
    private final ChatService chatService = new ChatService(applicationConfig);
    private static final int MESSAGE_COUNT = 10;
    private static final int STARTING_SIZE = 3;
    private String registrationMessage;
    private String reRegistrationMessage;
    private String deletionMessage;
    private ChatsDB chatsDB;
    private final ObjectMapper mapper = new ObjectMapper();
    private Supplier<Stream<String>> streamSupplier;

    @Test
    @Order(1)
    void registrationTest() {
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            registrationMessage = "#register TestingChat" + i + " = A";
            streamSupplier = () -> Arrays
                    .stream(registrationMessage.split(" "));
            this.chatService.registerChat(streamSupplier);
        }

        try (InputStream inputStream = new FileInputStream(applicationConfig.getChatsDb())) {
            chatsDB = mapper.readValue(inputStream, ChatsDB.class);
            Assertions.assertEquals(MESSAGE_COUNT + STARTING_SIZE, chatsDB.getChats().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    void reRegistrationTest() {
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            reRegistrationMessage = "#reregister TestingChat" + i + " = " + i;
            streamSupplier = () -> Arrays
                    .stream(reRegistrationMessage.split(" "));
            this.chatService.reRegisterChat(streamSupplier);
        }

        this.chatService.reRegisterChat(streamSupplier);
        try (InputStream inputStream = new FileInputStream(applicationConfig.getChatsDb())) {
            chatsDB = mapper.readValue(inputStream, ChatsDB.class);
            Assertions.assertEquals(MESSAGE_COUNT + STARTING_SIZE, chatsDB.getChats().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(3)
    void deletionTest() {
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            deletionMessage = "#delete TestingChat" + i;
            streamSupplier = () -> Arrays
                    .stream(deletionMessage.split(" "));
            this.chatService.deleteChat(streamSupplier);
        }

        this.chatService.deleteChat(streamSupplier);
        try (InputStream inputStream = new FileInputStream(applicationConfig.getChatsDb())) {
            chatsDB = mapper.readValue(inputStream, ChatsDB.class);
            Assertions.assertEquals(STARTING_SIZE, chatsDB.getChats().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
