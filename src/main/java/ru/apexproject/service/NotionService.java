package ru.apexproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ru.apexproject.config.ApplicationConfig;
import ru.apexproject.dto.notion.Task;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 *Sends a post request with Task object to Notion server
 */
@Slf4j
public class NotionService {
    private HttpURLConnection con;

    public void sendPost(Task task)  {
        try {
            URL url = new URL(ApplicationConfig.NOTION_API_URI);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + ApplicationConfig.NOTION_TOKEN);
            con.setRequestProperty("Notion-Version", "2022-06-28");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Type", "application/json");

            con.setDoInput(true);
            con.setDoOutput(true);

            OutputStream output = con.getOutputStream();

            ObjectMapper mapper = new ObjectMapper();
            output.write(mapper
                    .writeValueAsString(task)
                    .getBytes(StandardCharsets.UTF_8));

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            output.close();

        } catch (IOException e) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()))){
                String line;
                while ((line = in.readLine()) != null) {
                    log.error(line);
                }
            } catch (IOException p) {
                log.error(e.getMessage());
            }
            log.error(e.getMessage());
        }
    }
}
