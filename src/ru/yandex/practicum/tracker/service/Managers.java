package ru.yandex.practicum.tracker.service;

public class Managers {

    private static final TaskManager defaultTaskManager = new InMemoryTaskManager();
    private static final HistoryManager defaultHistoryManager = new InMemoryHistoryManager();

    public static TaskManager getDefault() {
        return defaultTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return defaultHistoryManager;
    }

}
