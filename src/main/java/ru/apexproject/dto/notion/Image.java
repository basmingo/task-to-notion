package ru.apexproject.dto.notion;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
class Image implements NotionObject {
    public String type;
    public Map<String, String> external;

    public Image(String url) {
        this.type = "external";
        this.external = new HashMap<>();
        external.put("url", url);
    }
}
