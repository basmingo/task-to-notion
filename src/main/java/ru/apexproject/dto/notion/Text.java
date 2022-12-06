package ru.apexproject.dto.notion;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
class Text implements NotionObject {
    public Map<String, String> text = new HashMap<>();

    public Text(String content) {
        text.put("content", content);
    }
}
