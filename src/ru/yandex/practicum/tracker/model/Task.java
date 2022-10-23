package ru.yandex.practicum.tracker.model;

import ru.yandex.practicum.tracker.TaskStatus;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        Task task = (Task) o;
        return id == task.id && name.equals(task.name) && status == task.status && description.equals(task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status, id, description);
    }
}
