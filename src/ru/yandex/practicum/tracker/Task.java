package ru.yandex.practicum.tracker;

import java.util.Objects;

public class Task {

    private String name;
    private TaskStatus status;
    private int id;
    private String description;

    public Task(String name, TaskStatus status, int id, String description) {
        this.name = name;
        this.status = status;
        this.id = id;
        this.description = description;
    }

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && name.equals(task.name) && status == task.status && description.equals(task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status, id, description);
    }
}
