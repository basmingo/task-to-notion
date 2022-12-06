package ru.apexproject.dto.notion;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
class RichTextParameters implements NotionObject {
    public String type;
    public Map<String, String> text;

    public RichTextParameters(String text) {
        this.type = "text";
        this.text = new HashMap<>();
        this.text.put("content", text);
    }
}
