package ru.yandex.practicum.tracker.model;

import java.util.Objects;

public class Task {

    private String name;
    private TaskStatus status;
    private int id;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toCsvRow(Task task) {
        return task.getId() + ","
                + task.getClass().getSimpleName() + ","
                + task.getName() + ","
                + task.getStatus() + ","
                + task.getDescription();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        Task task = (Task) o;
        return id == task.id
                && Objects.equals(name, task.name)
                && status == task.status
                && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status, id, description);
    }
}
