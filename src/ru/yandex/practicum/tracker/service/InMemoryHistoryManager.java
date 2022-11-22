package ru.yandex.practicum.tracker.service;

import ru.yandex.practicum.tracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> history = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        history.add(task);
    }

    @Override
    public void remove(int id) {
        history.removeNode(history.get(id));
        history.remove(id);
    }

    @Override
    public List<Integer> getHistory() {
        return new ArrayList<>(history.getMap().keySet());
    }
}
