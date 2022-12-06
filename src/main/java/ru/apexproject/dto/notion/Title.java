package ru.apexproject.dto.notion;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
class Title implements NotionObject {
    public List<Text> title = new ArrayList<>();

    public Title(String title) {
        this.title.add(new Text(title));
    }
}
