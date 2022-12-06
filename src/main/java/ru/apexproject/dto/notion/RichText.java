package ru.apexproject.dto.notion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class RichText implements NotionObject {

    @JsonProperty("rich_text")
    public List<RichTextParameters> richText;
    public RichText(String text){
        this.richText = new ArrayList<>();
        richText.add(new RichTextParameters(text));
    }
}
