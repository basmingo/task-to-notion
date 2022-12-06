package ru.apexproject.dto.notion;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class RichText implements NotionObject {
    public List<RichTextParameters> rich_text;
    public RichText(String text){
        this.rich_text = new ArrayList<>();
        rich_text.add(new RichTextParameters(text));
    }
}
