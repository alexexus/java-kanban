package ru.yandex.practicum.tracker.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.Subtask;
import ru.yandex.practicum.tracker.model.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    KVTaskClient kvTaskClient;
    Gson gson = new Gson();

    public HttpTaskManager(URI uri) {
        super();
        this.kvTaskClient = new KVTaskClient(uri);
    }

    public void loadFromServer() {
        String jsonTasks = kvTaskClient.load("Tasks");
        Type taskType = new TypeToken<List<Task>>() {}.getType();
        List<Task> tasks = gson.fromJson(jsonTasks, taskType);
        for (Task task : tasks) {
            this.tasks.put(task.getId(), task);
        }

        String jsonEpics = kvTaskClient.load("Epics");
        Type epicType = new TypeToken<List<Epic>>() {}.getType();
        List<Epic> epics = gson.fromJson(jsonEpics, epicType);
        for (Epic epic : epics) {
            this.epics.put(epic.getId(), epic);
        }

        String jsonSubtasks = kvTaskClient.load("Subtasks");
        Type subtaskType = new TypeToken<List<Subtask>>() {}.getType();
        List<Subtask> subtasks = gson.fromJson(jsonSubtasks, subtaskType);
        for (Subtask subtask : subtasks) {
            this.subtasks.put(subtask.getId(), subtask);
        }

        String jsonHistory = kvTaskClient.load("History");
        Type historyType = new TypeToken<List<Integer>>() {}.getType();
        List<Integer> history = gson.fromJson(jsonHistory, historyType);
        for (Integer integer : history) {
            getEpicById(integer);
            getSubtaskById(integer);
            getTaskById(integer);
        }
    }

    @Override
    public void save() {
        try {
            String str1 = gson.toJson(getTasks());
            kvTaskClient.put("Tasks", str1);

            String str2 = gson.toJson(getEpics());
            kvTaskClient.put("Epics", str2);

            String str3 = gson.toJson(getSubtasks());
            kvTaskClient.put("Subtasks", str3);

            String str4 = gson.toJson(historyManager.getHistory());
            kvTaskClient.put("History", str4);

        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
