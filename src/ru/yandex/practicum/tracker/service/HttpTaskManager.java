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
        kvTaskClient = new KVTaskClient(uri);
    }

    public void loadFromServer() {
        String jsonTasks = kvTaskClient.load("Tasks");
        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(jsonTasks, taskType);
        for (Task task : tasks) {
            this.tasks.put(task.getId(), task);
        }

        String jsonEpics = kvTaskClient.load("Epics");
        Type epicType = new TypeToken<List<Epic>>() {
        }.getType();
        List<Epic> epics = gson.fromJson(jsonEpics, epicType);
        for (Epic epic : epics) {
            this.epics.put(epic.getId(), epic);
        }

        String jsonSubtasks = kvTaskClient.load("Subtasks");
        Type subtaskType = new TypeToken<List<Subtask>>() {
        }.getType();
        List<Subtask> subtasks = gson.fromJson(jsonSubtasks, subtaskType);
        for (Subtask subtask : subtasks) {
            this.subtasks.put(subtask.getId(), subtask);
        }

        String jsonHistory = kvTaskClient.load("History");
        Type historyType = new TypeToken<List<Integer>>() {
        }.getType();
        List<Integer> history = gson.fromJson(jsonHistory, historyType);
        for (Integer integer : history) {
            getEpicById(integer);
            getSubtaskById(integer);
            getTaskById(integer);
        }
    }

    @Override
    protected void save() {
        try {
            String jsonTasks = gson.toJson(getTasks());
            kvTaskClient.put("Tasks", jsonTasks);

            String jsonEpics = gson.toJson(getEpics());
            kvTaskClient.put("Epics", jsonEpics);

            String jsonSubtasks = gson.toJson(getSubtasks());
            kvTaskClient.put("Subtasks", jsonSubtasks);

            String jsonHistory = gson.toJson(historyManager.getHistory());
            kvTaskClient.put("History", jsonHistory);

        } catch (InterruptedException | IOException e) {
            throw new ManagerSaveException("An error occurred while parsing tasks and history in JSON format");
        }
    }
}
