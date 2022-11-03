package ru.yandex.practicum.tracker.model;

import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subTaskIds;

    public List<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskIds(List<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Epic) || !super.equals(o)) {
            return false;
        }
        Epic epic = (Epic) o;
        return subTaskIds.equals(epic.subTaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTaskIds);
    }
}
