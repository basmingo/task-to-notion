package ru.apexproject.dto.notion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
class Properties implements NotionObject {
    @NonNull
    public Title title;
    @NonNull
    @JsonProperty("Assignee")
    public NotionObject assignee;
}
