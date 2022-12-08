package ru.apexproject.service;

import ru.apexproject.config.ApplicationConfig;
import ru.apexproject.dto.notion.Task;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *Sends a post request with Task object to Notion server
 */
public class NotionService {
    ApplicationConfig applicationConfig;
    HttpURLConnection httpURLConnection;
    ObjectOutputStream out;
    URL url;

    public NotionService(ApplicationConfig applicationConfig) throws IOException {
        this.applicationConfig = applicationConfig;
        this.url = new URL(applicationConfig.getNotionApiURI());
        this.httpURLConnection = (HttpURLConnection) url.openConnection();
    }

    public void doPost(Task task)  {
        try {
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + applicationConfig.getNotionToken());
            httpURLConnection.setRequestProperty("Notion-Version", "2022-06-28");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            httpURLConnection.setDoOutput(true);
            out = new ObjectOutputStream(httpURLConnection.getOutputStream());
            out.writeObject(task);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
