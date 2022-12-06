package ru.apexproject.dto.notion;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Paragraph implements NotionObject {
    public String object;
    public String type;
    public NotionObject paragraph;
    public NotionObject image;

    public Paragraph(String paragraph) {
        this.object = "block";
        this.type = "paragraph";
        this.paragraph = new RichText(paragraph);
    }

    public Paragraph(String type, String imageUrl) {
        this.object = "block";
        this.type = type;
        this.image = new Image(imageUrl);
    }
}
