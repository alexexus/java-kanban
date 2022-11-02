package ru.yandex.practicum.tracker.service;

import ru.yandex.practicum.tracker.model.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();

}
