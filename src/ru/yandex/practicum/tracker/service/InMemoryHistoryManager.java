package ru.yandex.practicum.tracker.service;

import ru.yandex.practicum.tracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final int INITIAL_CAPACITY_OF_HISTORY = 10;

    private final List<Task> history = new ArrayList<>(INITIAL_CAPACITY_OF_HISTORY);

    @Override
    public void add(Task task) {
        if (history.size() >= INITIAL_CAPACITY_OF_HISTORY) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
