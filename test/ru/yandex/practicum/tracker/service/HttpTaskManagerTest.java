package ru.yandex.practicum.tracker.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.Subtask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.model.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends FileBackedTasksManagerTest {

    @BeforeAll
    static void launch() throws IOException {
        new KVServer().start();
    }

    @Override
    protected TaskManager createTaskManager() {
        return new HttpTaskManager(URI.create("http://localhost:8080/register/"));
    }

    @Test
    void loadFromServer_shouldLoadDataFromServer() throws IOException, InterruptedException {
        HttpTaskManager httpTaskManager = new HttpTaskManager(URI.create("http://localhost:8080/register/"));
        httpTaskManager.kvTaskClient.put("Tasks", "[{\"name\":\"Задача #1\",\"status\":\"NEW\",\"id\":1,\"description\":\"Описание задачи #1\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2000,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}},{\"name\":\"Задача #1\",\"status\":\"NEW\",\"id\":4,\"description\":\"Описание задачи #1\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2001,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}}]\n");
        httpTaskManager.kvTaskClient.put("Epics", "[{\"subtaskIds\":[3,5],\"name\":\"Эпик #1\",\"status\":\"NEW\",\"id\":2,\"description\":\"Описание эпика #1\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2003,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}}]\n");
        httpTaskManager.kvTaskClient.put("Subtasks", "[{\"epicId\":2,\"name\":\"Подзадача #1 в эпике #1\",\"status\":\"NEW\",\"id\":3,\"description\":\"Описание подзадачи #1 в эпике #1\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2002,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}},{\"epicId\":2,\"name\":\"Подзадача #1 в эпике #1\",\"status\":\"NEW\",\"id\":5,\"description\":\"Описание подзадачи #1 в эпике #1\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2003,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}}]\n");
        httpTaskManager.kvTaskClient.put("History", "[4,2,5]");
        httpTaskManager.loadFromServer();

        Task task1 = task("Задача #1", "Описание задачи #1", TaskStatus.NEW, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Task task4 = task("Задача #1", "Описание задачи #1", TaskStatus.NEW, 4,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Epic epic2 = epic("Эпик #1", "Описание эпика #1", TaskStatus.NEW, 2, new ArrayList<>());
        epic2.setStartTime(LocalDateTime.of(2003, 1, 1, 1, 1, 0, 0));
        epic2.setDuration(Duration.ofSeconds(0));
        epic2.addSubtaskId(3);
        epic2.addSubtaskId(5);
        Subtask subtask3 = subtask("Подзадача #1 в эпике #1", "Описание подзадачи #1 в эпике #1", TaskStatus.NEW, 3, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));
        Subtask subtask5 = subtask("Подзадача #1 в эпике #1", "Описание подзадачи #1 в эпике #1", TaskStatus.NEW, 5, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2003, 1, 1, 1, 1));


        assertEquals(List.of(task1, task4), httpTaskManager.getTasks());
        assertEquals(List.of(epic2), httpTaskManager.getEpics());
        assertEquals(List.of(subtask3, subtask5), httpTaskManager.getSubtasks());
        assertEquals(List.of(4, 2, 5), httpTaskManager.historyManager.getHistory());
    }

    @Test
    void save_shouldSaveDataToServer() {
        HttpTaskManager httpTaskManager = new HttpTaskManager(URI.create("http://localhost:8080/register/"));

        Task task1 = task("Задача #1", "Описание задачи #1", TaskStatus.NEW, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Task task4 = task("Задача #1", "Описание задачи #1", TaskStatus.NEW, 4,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Epic epic2 = epic("Эпик #1", "Описание эпика #1", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask3 = subtask("Подзадача #1 в эпике #1", "Описание подзадачи #1 в эпике #1", TaskStatus.NEW, 3, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));
        Subtask subtask5 = subtask("Подзадача #1 в эпике #1", "Описание подзадачи #1 в эпике #1", TaskStatus.NEW, 5, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2003, 1, 1, 1, 1));

        httpTaskManager.createTask(task1);
        httpTaskManager.createEpic(epic2);
        httpTaskManager.createSubtask(subtask3);
        httpTaskManager.createTask(task4);
        httpTaskManager.createSubtask(subtask5);

        httpTaskManager.getTaskById(4);
        httpTaskManager.getEpicById(2);
        httpTaskManager.getSubtaskById(5);

        assertEquals(List.of(task1, task4), httpTaskManager.getTasks());
        assertEquals(List.of(epic2), httpTaskManager.getEpics());
        assertEquals(List.of(subtask3, subtask5), httpTaskManager.getSubtasks());
        assertEquals(List.of(4, 2, 5), httpTaskManager.historyManager.getHistory());
    }
}
