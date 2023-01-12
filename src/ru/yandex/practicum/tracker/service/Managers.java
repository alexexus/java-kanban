package ru.yandex.practicum.tracker.service;

import java.net.URI;

public class Managers {
    public static TaskManager getDefault() {
        return new HttpTaskManager(URI.create("http://localhost:8080/register"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
