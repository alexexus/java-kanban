package ru.yandex.practicum.tracker.service;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.Subtask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.model.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {

    @Test
    void getTasks_shouldGetAllTasks() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Task task1 = task("name1", "description1", TaskStatus.NEW, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Task task2 = task("name2", "description2", TaskStatus.IN_PROGRESS, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Task task3 = task("name3", "description3", TaskStatus.DONE, 3,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));

        httpTaskServer.taskManager.createTask(task1);
        httpTaskServer.taskManager.createTask(task2);
        httpTaskServer.taskManager.createTask(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("[{\"name\":\"name1\",\"status\":\"NEW\",\"id\":1,\"description\":\"description1\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2000,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}},{\"name\":\"name2\",\"status\":\"IN_PROGRESS\",\"id\":2,\"description\":\"description2\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2001,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}},{\"name\":\"name3\",\"status\":\"DONE\",\"id\":3,\"description\":\"description3\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2002,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}}]",
                response.body());
        httpTaskServer.stop();
    }

    @Test
    void getEpics_shouldGetAllEpics() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.IN_PROGRESS, 2, new ArrayList<>());
        Epic epic3 = epic("name3", "description3", TaskStatus.DONE, 3, new ArrayList<>());

        httpTaskServer.taskManager.createEpic(epic1);
        httpTaskServer.taskManager.createEpic(epic2);
        httpTaskServer.taskManager.createEpic(epic3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("[{\"subtaskIds\":[],\"name\":\"name1\",\"status\":\"NEW\",\"id\":1,\"description\":\"description1\"},{\"subtaskIds\":[],\"name\":\"name2\",\"status\":\"IN_PROGRESS\",\"id\":2,\"description\":\"description2\"},{\"subtaskIds\":[],\"name\":\"name3\",\"status\":\"DONE\",\"id\":3,\"description\":\"description3\"}]",
                response.body());
        httpTaskServer.stop();
    }

    @Test
    void getSubtasks_shouldGetAllSubtasks() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask1 = subtask("name3", "description3", TaskStatus.NEW, 3, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Subtask subtask2 = subtask("name4", "description4", TaskStatus.IN_PROGRESS, 4, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Subtask subtask3 = subtask("name5", "description5", TaskStatus.DONE, 5, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));

        httpTaskServer.taskManager.createEpic(epic1);
        httpTaskServer.taskManager.createEpic(epic2);
        httpTaskServer.taskManager.createSubtask(subtask1);
        httpTaskServer.taskManager.createSubtask(subtask2);
        httpTaskServer.taskManager.createSubtask(subtask3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("[{\"epicId\":1,\"name\":\"name3\",\"status\":\"NEW\",\"id\":3,\"description\":\"description3\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2000,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}},{\"epicId\":1,\"name\":\"name4\",\"status\":\"IN_PROGRESS\",\"id\":4,\"description\":\"description4\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2001,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}},{\"epicId\":2,\"name\":\"name5\",\"status\":\"DONE\",\"id\":5,\"description\":\"description5\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2002,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}}]",
                response.body());
        httpTaskServer.stop();
    }

    @Test
    void getTaskById_shouldGetTaskById() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Task task1 = task("name1", "description1", TaskStatus.NEW, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Task task2 = task("name2", "description2", TaskStatus.IN_PROGRESS, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Task task3 = task("name3", "description3", TaskStatus.DONE, 3,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));

        httpTaskServer.taskManager.createTask(task1);
        httpTaskServer.taskManager.createTask(task2);
        httpTaskServer.taskManager.createTask(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/task/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("{\"name\":\"name1\",\"status\":\"NEW\",\"id\":1,\"description\":\"description1\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2000,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}}",
                response.body());
        httpTaskServer.stop();
    }

    @Test
    void getEpicById_shouldGetEpicById() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.IN_PROGRESS, 2, new ArrayList<>());
        Epic epic3 = epic("name3", "description3", TaskStatus.DONE, 3, new ArrayList<>());

        httpTaskServer.taskManager.createEpic(epic1);
        httpTaskServer.taskManager.createEpic(epic2);
        httpTaskServer.taskManager.createEpic(epic3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/epic/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("{\"subtaskIds\":[],\"name\":\"name2\",\"status\":\"IN_PROGRESS\",\"id\":2,\"description\":\"description2\"}",
                response.body());
        httpTaskServer.stop();
    }

    @Test
    void getSubtaskById_shouldGetSubtaskById() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask1 = subtask("name3", "description3", TaskStatus.NEW, 3, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Subtask subtask2 = subtask("name4", "description4", TaskStatus.IN_PROGRESS, 4, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Subtask subtask3 = subtask("name5", "description5", TaskStatus.DONE, 5, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));

        httpTaskServer.taskManager.createEpic(epic1);
        httpTaskServer.taskManager.createEpic(epic2);
        httpTaskServer.taskManager.createSubtask(subtask1);
        httpTaskServer.taskManager.createSubtask(subtask2);
        httpTaskServer.taskManager.createSubtask(subtask3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/subtask/5");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("{\"epicId\":2,\"name\":\"name5\",\"status\":\"DONE\",\"id\":5,\"description\":\"description5\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2002,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}}",
                response.body());
        httpTaskServer.stop();
    }

    @Test
    void getSubtasksByEpicId_shouldGetSubtasksByEpicId() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask1 = subtask("name3", "description3", TaskStatus.NEW, 3, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Subtask subtask2 = subtask("name4", "description4", TaskStatus.IN_PROGRESS, 4, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Subtask subtask3 = subtask("name5", "description5", TaskStatus.DONE, 5, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));

        httpTaskServer.taskManager.createEpic(epic1);
        httpTaskServer.taskManager.createEpic(epic2);
        httpTaskServer.taskManager.createSubtask(subtask1);
        httpTaskServer.taskManager.createSubtask(subtask2);
        httpTaskServer.taskManager.createSubtask(subtask3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/subtask/epic/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("[{\"epicId\":1,\"name\":\"name3\",\"status\":\"NEW\",\"id\":3,\"description\":\"description3\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2000,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}},{\"epicId\":1,\"name\":\"name4\",\"status\":\"IN_PROGRESS\",\"id\":4,\"description\":\"description4\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2001,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}}]",
                response.body());
        httpTaskServer.stop();
    }

    @Test
    void getHistory_shouldGetHistory() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Task task1 = task("name1", "description1", TaskStatus.NEW, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Task task2 = task("name2", "description2", TaskStatus.IN_PROGRESS, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Task task3 = task("name3", "description3", TaskStatus.DONE, 3,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));

        httpTaskServer.taskManager.createTask(task1);
        httpTaskServer.taskManager.createTask(task2);
        httpTaskServer.taskManager.createTask(task3);

        httpTaskServer.taskManager.getTaskById(2);
        httpTaskServer.taskManager.getTaskById(3);
        httpTaskServer.taskManager.getTaskById(1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("[2,3,1]",
                response.body());
        httpTaskServer.stop();
    }

    @Test
    void getPrioritizedTasks_shouldReturnSortedTasks() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Task task1 = task("name1", "description1", TaskStatus.NEW, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Task task2 = task("name2", "description2", TaskStatus.NEW, 2,
                Duration.ofMinutes(0), null);
        Subtask subtask3 = subtask("name3", "description3", TaskStatus.NEW, 3, 5,
                Duration.ofMinutes(0), LocalDateTime.of(2003, 1, 1, 1, 1));
        Task task4 = task("name4", "description4", TaskStatus.NEW, 4,
                Duration.ofMinutes(0), LocalDateTime.of(2004, 1, 1, 1, 1));
        Epic epic5 = epic("name5", "description5", TaskStatus.NEW, 5, new ArrayList<>());

        httpTaskServer.taskManager.updateTask(task1);
        httpTaskServer.taskManager.updateTask(task2);
        httpTaskServer.taskManager.updateEpic(epic5);
        httpTaskServer.taskManager.updateSubtask(subtask3);
        httpTaskServer.taskManager.updateTask(task4);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("[{\"name\":\"name1\",\"status\":\"NEW\",\"id\":1,\"description\":\"description1\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2001,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}},{\"epicId\":5,\"name\":\"name3\",\"status\":\"NEW\",\"id\":3,\"description\":\"description3\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2003,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}},{\"name\":\"name4\",\"status\":\"NEW\",\"id\":4,\"description\":\"description4\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2004,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}},{\"name\":\"name2\",\"status\":\"NEW\",\"id\":2,\"description\":\"description2\",\"duration\":{\"seconds\":0,\"nanos\":0}}]",
                response.body());
        httpTaskServer.stop();
    }

    @Test
    void deleteAllTasks_shouldDeleteAllTasks() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Task task1 = task("name1", "description1", TaskStatus.NEW, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Task task2 = task("name2", "description2", TaskStatus.IN_PROGRESS, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Task task3 = task("name3", "description3", TaskStatus.DONE, 3,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));

        httpTaskServer.taskManager.createTask(task1);
        httpTaskServer.taskManager.createTask(task2);
        httpTaskServer.taskManager.createTask(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpRequest request1 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals("[]", response1.body());
        httpTaskServer.stop();
    }

    @Test
    void deleteAllEpics_shouldDeleteAllEpicsAndSubtasks() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.IN_PROGRESS, 2, new ArrayList<>());
        Epic epic3 = epic("name3", "description3", TaskStatus.DONE, 3, new ArrayList<>());

        httpTaskServer.taskManager.createEpic(epic1);
        httpTaskServer.taskManager.createEpic(epic2);
        httpTaskServer.taskManager.createEpic(epic3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpRequest request1 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals("[]", response1.body());
        httpTaskServer.stop();
    }

    @Test
    void deleteAllSubtasks_shouldDeleteAllSubtasks() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask1 = subtask("name3", "description3", TaskStatus.NEW, 3, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Subtask subtask2 = subtask("name4", "description4", TaskStatus.IN_PROGRESS, 4, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Subtask subtask3 = subtask("name5", "description5", TaskStatus.DONE, 5, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));

        httpTaskServer.taskManager.createEpic(epic1);
        httpTaskServer.taskManager.createEpic(epic2);
        httpTaskServer.taskManager.createSubtask(subtask1);
        httpTaskServer.taskManager.createSubtask(subtask2);
        httpTaskServer.taskManager.createSubtask(subtask3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpRequest request1 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals("[]", response1.body());
        httpTaskServer.stop();
    }

    @Test
    void removeTaskById_shouldRemoveTaskById() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Task task1 = task("name1", "description1", TaskStatus.NEW, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Task task2 = task("name2", "description2", TaskStatus.IN_PROGRESS, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Task task3 = task("name3", "description3", TaskStatus.DONE, 3,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));

        httpTaskServer.taskManager.createTask(task1);
        httpTaskServer.taskManager.createTask(task2);
        httpTaskServer.taskManager.createTask(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/task/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8081/tasks/task/");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals("[{\"name\":\"name1\",\"status\":\"NEW\",\"id\":1,\"description\":\"description1\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2000,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}},{\"name\":\"name3\",\"status\":\"DONE\",\"id\":3,\"description\":\"description3\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2002,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}}]",
                response1.body());
        httpTaskServer.stop();
    }

    @Test
    void removeEpicById_shouldRemoveEpicById() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.IN_PROGRESS, 2, new ArrayList<>());
        Epic epic3 = epic("name3", "description3", TaskStatus.DONE, 3, new ArrayList<>());

        httpTaskServer.taskManager.createEpic(epic1);
        httpTaskServer.taskManager.createEpic(epic2);
        httpTaskServer.taskManager.createEpic(epic3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/epic/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8081/tasks/epic/");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals("[{\"subtaskIds\":[],\"name\":\"name1\",\"status\":\"NEW\",\"id\":1,\"description\":\"description1\"},{\"subtaskIds\":[],\"name\":\"name3\",\"status\":\"DONE\",\"id\":3,\"description\":\"description3\"}]",
                response1.body());
        httpTaskServer.stop();
    }

    @Test
    void removeSubtaskById_shouldRemoveSubtaskById() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask1 = subtask("name3", "description3", TaskStatus.NEW, 3, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Subtask subtask2 = subtask("name4", "description4", TaskStatus.IN_PROGRESS, 4, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Subtask subtask3 = subtask("name5", "description5", TaskStatus.DONE, 5, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));

        httpTaskServer.taskManager.createEpic(epic1);
        httpTaskServer.taskManager.createEpic(epic2);
        httpTaskServer.taskManager.createSubtask(subtask1);
        httpTaskServer.taskManager.createSubtask(subtask2);
        httpTaskServer.taskManager.createSubtask(subtask3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/subtask/4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8081/tasks/subtask/");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals("[{\"epicId\":1,\"name\":\"name3\",\"status\":\"NEW\",\"id\":3,\"description\":\"description3\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2000,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}},{\"epicId\":2,\"name\":\"name5\",\"status\":\"DONE\",\"id\":5,\"description\":\"description5\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2002,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}}]",
                response1.body());
        httpTaskServer.stop();
    }

    @Test
    void createTask_shouldCreateTask() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Task task1 = task("name1", "description1", TaskStatus.NEW, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/task/");
        Gson gson = new Gson();
        String json = gson.toJson(task1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8081/tasks/task/");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals("[{\"name\":\"name1\",\"status\":\"NEW\",\"id\":1,\"description\":\"description1\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2000,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}}]",
                response1.body());
        httpTaskServer.stop();
    }

    @Test
    void createEpic_shouldCreateEpic() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/epic/");
        Gson gson = new Gson();
        String json = gson.toJson(epic1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8081/tasks/epic/");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals("[{\"subtaskIds\":[],\"name\":\"name1\",\"status\":\"NEW\",\"id\":1,\"description\":\"description1\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":-999999999,\"month\":1,\"day\":1},\"time\":{\"hour\":0,\"minute\":0,\"second\":0,\"nano\":0}}}]",
                response1.body());
        httpTaskServer.stop();
    }

    @Test
    void createSubtask_shouldCreateSubtask() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Subtask subtask1 = subtask("name3", "description3", TaskStatus.NEW, 3, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));

        httpTaskServer.taskManager.createEpic(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/subtask/");
        Gson gson = new Gson();
        String json = gson.toJson(subtask1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8081/tasks/subtask/");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals("[{\"epicId\":1,\"name\":\"name3\",\"status\":\"NEW\",\"id\":3,\"description\":\"description3\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2000,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}}]",
                response1.body());
        httpTaskServer.stop();
    }

    @Test
    void updateTask_shouldUpdateTask() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Task task1 = task("name1", "description1", TaskStatus.NEW, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Task task2 = task("name2", "description2", TaskStatus.IN_PROGRESS, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));

        httpTaskServer.taskManager.createTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/task/");
        Gson gson = new Gson();
        String json = gson.toJson(task2);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8081/tasks/task/");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals("[{\"name\":\"name2\",\"status\":\"IN_PROGRESS\",\"id\":1,\"description\":\"description2\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2001,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}}]",
                response1.body());
        httpTaskServer.stop();
    }

    @Test
    void updateEpic_shouldUpdateEpic() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.NEW, 1, new ArrayList<>());

        httpTaskServer.taskManager.createEpic(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/epic/");
        Gson gson = new Gson();
        String json = gson.toJson(epic2);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8081/tasks/epic/");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals("[{\"subtaskIds\":[],\"name\":\"name2\",\"status\":\"NEW\",\"id\":1,\"description\":\"description2\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":-999999999,\"month\":1,\"day\":1},\"time\":{\"hour\":0,\"minute\":0,\"second\":0,\"nano\":0}}}]",
                response1.body());
        httpTaskServer.stop();
    }

    @Test
    void updateSubtask_shouldUpdateSubtask() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Subtask subtask1 = subtask("name3", "description3", TaskStatus.NEW, 2, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Subtask subtask2 = subtask("name4", "description4", TaskStatus.IN_PROGRESS, 2, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));

        httpTaskServer.taskManager.createEpic(epic1);
        httpTaskServer.taskManager.createSubtask(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks/subtask/");
        Gson gson = new Gson();
        String json = gson.toJson(subtask2);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8081/tasks/subtask/");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals("[{\"epicId\":1,\"name\":\"name4\",\"status\":\"IN_PROGRESS\",\"id\":2,\"description\":\"description4\",\"duration\":{\"seconds\":0,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2001,\"month\":1,\"day\":1},\"time\":{\"hour\":1,\"minute\":1,\"second\":0,\"nano\":0}}}]",
                response1.body());
        httpTaskServer.stop();
    }

    protected static Task task(String name, String description, TaskStatus status, int id, Duration duration, LocalDateTime localDateTime) {
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setStatus(status);
        task.setId(id);
        task.setStartTime(localDateTime);
        task.setDuration(duration);
        return task;
    }

    protected static Epic epic(String name, String description, TaskStatus status, int id, List<Integer> subtaskIds) {
        Epic epic = new Epic();
        epic.setName(name);
        epic.setDescription(description);
        epic.setStatus(status);
        epic.setId(id);
        return epic;
    }

    protected static Subtask subtask(String name, String description, TaskStatus status, int id, int epicId, Duration duration, LocalDateTime localDateTime) {
        Subtask subtask = new Subtask();
        subtask.setName(name);
        subtask.setDescription(description);
        subtask.setStatus(status);
        subtask.setId(id);
        subtask.setEpicId(epicId);
        subtask.setDuration(duration);
        subtask.setStartTime(localDateTime);
        return subtask;
    }
}
