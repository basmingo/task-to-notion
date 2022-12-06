package ru.apexproject.dto.notion;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Name implements NotionObject {
    @JsonProperty("name")
    public String name;

    public Name(String name) {
        this.name = name;
    }
}
