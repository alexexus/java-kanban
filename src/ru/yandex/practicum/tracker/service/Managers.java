package ru.yandex.practicum.tracker.service;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        return new FileBackedTasksManager(new File("resources/empty-file.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
