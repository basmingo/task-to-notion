import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.apexproject.config.ApplicationConfig;
import ru.apexproject.dto.notion.Task;
import ru.apexproject.service.NotionService;

public class NotionServiceTest {
    NotionService notionService = new NotionService(new ApplicationConfig());

    @Test
    void sendPost() {
        notionService.sendPost(new Task());
    }
}
