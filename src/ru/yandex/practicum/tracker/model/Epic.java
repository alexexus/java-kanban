package ru.yandex.practicum.tracker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subtaskIds = new ArrayList<>();

    public List<Integer> getSubtaskIds() {
        return Collections.unmodifiableList(subtaskIds);
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    public void clearSubtaskIds() {
        subtaskIds.clear();
    }


    public String toCsvRow(Epic epic) {
        return epic.getId() + ","
                + epic.getClass().getSimpleName() + ","
                + epic.getName() + ","
                + epic.getStatus() + ","
                + epic.getDescription();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Epic) || !super.equals(o)) {
            return false;
        }
        Epic epic = (Epic) o;
        return subtaskIds.equals(epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }
}
