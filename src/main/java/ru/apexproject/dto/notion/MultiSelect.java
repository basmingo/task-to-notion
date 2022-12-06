package ru.apexproject.dto.notion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class MultiSelect implements NotionObject {
    @JsonProperty("multi_select")
    public List<Name> assignees;

    public MultiSelect(List<String> assignees) {
        this.assignees = new ArrayList<>();
        assignees.forEach(assignee -> this.assignees.add(new Name(assignee)));
    }
}
