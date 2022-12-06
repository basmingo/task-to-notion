package ru.apexproject.service;

import com.basmingo.tasktonotion.config.NotionConfig;
import com.basmingo.tasktonotion.dto.notion.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotionService {
    private final RestTemplate restTemplate;
    NotionConfig notionConfig;

    @Autowired
    public NotionService(NotionConfig notionConfig) {
        this.notionConfig = notionConfig;
        this.restTemplate = new RestTemplate();
    }

    public void send(Task task) {
        HttpEntity<Task> entity = new HttpEntity<>(task, getHttpHeaders());
        restTemplate.exchange("https://api.notion.com/v1/pages", HttpMethod.POST, entity, String.class);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(notionConfig.getNotionToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Notion-Version", "2022-06-28");
        return httpHeaders;
    }
}
