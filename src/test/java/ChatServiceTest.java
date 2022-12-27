import org.junit.jupiter.api.*;
import ru.apexproject.service.ChatService;
import ru.apexproject.service.JsonHandler;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChatServiceTest {
    private final ChatService chatService = new ChatService();
    private final JsonHandler jsonHandler = new JsonHandler();
    private static final int MESSAGE_COUNT = 3;
    private static final int STARTING_SIZE = 3;
    private String registrationMessage;
    private String reRegistrationMessage;
    private String deletionMessage;
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
        Assertions.assertEquals(MESSAGE_COUNT + STARTING_SIZE,
                jsonHandler.loadDbFromJson().size());

        Assertions.assertTrue(chatService.chatDbContains("TestingChat1"));
        Assertions.assertFalse(chatService.chatDbContains("FalseQuery"));
    }

    @Test
    @Order(2)
    void reRegistrationTest() {
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            reRegistrationMessage = "#reregister TestingChat" + i + " = " + i + 1;
            streamSupplier = () -> Arrays
                    .stream(reRegistrationMessage.split(" "));
            this.chatService.reRegisterChat(streamSupplier);
        }
        Assertions.assertEquals(MESSAGE_COUNT + STARTING_SIZE,
                jsonHandler.loadDbFromJson().size());
        Assertions.assertTrue(chatService.chatDbContains("TestingChat1"));
        Assertions.assertFalse(chatService.chatDbContains("FalseQuery"));
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

        Assertions.assertEquals(STARTING_SIZE,
                jsonHandler.loadDbFromJson().size());
        Assertions.assertFalse(chatService.chatDbContains("TestingChat1"));
    }
}
