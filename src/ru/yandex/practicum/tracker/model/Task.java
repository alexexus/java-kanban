package ru.yandex.practicum.tracker.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private String name;
    private TaskStatus status;
    private int id;
    private String description;
    private int duration = 0;
    private LocalDateTime startTime = LocalDateTime.MIN;

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
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

    public String toCsvRow() {
        return String.format("%d,%s,%s,%s,%s,%d,%s",
                id, getClass().getSimpleName(), name, status, description, duration, startTime.toString());
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
                && Objects.equals(description, task.description)
                && startTime.isEqual(task.startTime)
                && duration == task.duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status, id, description, startTime, duration);
    }
}
