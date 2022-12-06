package ru.apexproject.dto.notion;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class Task implements NotionObject {
    public Map<String, String> parent = new HashMap<>();
    public NotionObject properties;
    public List<Paragraph> children;

    public Task(String comment, List<String> assignees, String DatabaseUrl) {
        this.parent.put("database_id", DatabaseUrl);
        this.properties = new Properties(
                new Title(comment),
                new MultiSelect(assignees));

        this.children = new ArrayList<>();
        children.add(new Paragraph(comment));
    }

    public Task(String comment, List<String> assignees, String imageURL, String DatabaseUrl) {
        this.parent.put("database_id", DatabaseUrl);
        this.properties = new Properties(
                new Title(comment),
                new MultiSelect(assignees));

        this.children = new ArrayList<>();
        children.add(new Paragraph(comment));
        children.add(new Paragraph("image", imageURL));
    }
}

