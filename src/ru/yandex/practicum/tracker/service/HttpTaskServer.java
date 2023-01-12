package ru.yandex.practicum.tracker.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.Subtask;
import ru.yandex.practicum.tracker.model.Task;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    public static final int PORT = 8081;
    protected HttpServer httpServer;
    protected Gson gson;
    protected TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this(new FileBackedTasksManager(new File("resources/empty-file.csv")));
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", this::handleTasks);
        gson = new GsonBuilder().create();
    }

    private void handleTasks(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET":
                    if (Pattern.matches("^/tasks/subtask/epic/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/subtask/epic/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getSubtasksByEpicId(id));
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Получен неверный id задачи");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                        break;
                    }

                    if (Pattern.matches("^/tasks/history/$", path)) {
                        String response = gson.toJson(taskManager.getHistory());
                        sendText(httpExchange, response);
                        break;
                    }

                    if (Pattern.matches("^/tasks/$", path)) {
                        String response = gson.toJson(taskManager.getPrioritizedTasks());
                        sendText(httpExchange, response);
                        break;
                    }

                    if (Pattern.matches("^/tasks/task/$", path)) {
                        String response = gson.toJson(taskManager.getTasks());
                        sendText(httpExchange, response);
                        break;
                    }
                    if (Pattern.matches("^/tasks/epic/$", path)) {
                        String response = gson.toJson(taskManager.getEpics());
                        sendText(httpExchange, response);
                        break;
                    }
                    if (Pattern.matches("^/tasks/subtask/$", path)) {
                        String response = gson.toJson(taskManager.getSubtasks());
                        sendText(httpExchange, response);
                        break;
                    }

                    if (Pattern.matches("^/tasks/task/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/task/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getTaskById(id));
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Получен неверный id задачи");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                        break;
                    }
                    if (Pattern.matches("^/tasks/epic/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/epic/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getEpicById(id));
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Получен неверный id задачи");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                        break;
                    }
                    if (Pattern.matches("^/tasks/subtask/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/subtask/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getSubtaskById(id));
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Получен неверный id задачи");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                        break;
                    }
                    break;
                case "POST":
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        try (InputStream inputStream = httpExchange.getRequestBody()) {
                            String body = new String(inputStream.readAllBytes());
                            gson.toJson(body);
                            if (!(body.contains("name"))
                                    || !(body.contains("status"))
                                    || !(body.contains("description"))
                                    || !(body.contains("duration"))
                                    || !(body.contains("startTime"))) {
                                System.out.println("Поля не могут быть пустыми");
                                httpExchange.sendResponseHeaders(400, 0);
                                break;
                            }
                            Task task = gson.fromJson(body, Task.class);
                            if (body.contains("id")) {
                                taskManager.updateTask(task);
                                System.out.println("Задача обновлена");
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                taskManager.createTask(task);
                                System.out.println("Задача добавлена");
                                httpExchange.sendResponseHeaders(200, 0);
                            }
                        } catch (JsonSyntaxException e) {
                            System.out.println("Получен некорректный JSON");
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                        break;
                    }
                    if (Pattern.matches("^/tasks/epic/$", path)) {
                        try (InputStream inputStream = httpExchange.getRequestBody()) {
                            String body = new String(inputStream.readAllBytes());
                            gson.toJson(body);
                            if (!(body.contains("name"))
                                    || !(body.contains("status"))
                                    || !(body.contains("description"))
                                    || !(body.contains("subtaskIds"))) {
                                System.out.println("Поля не могут быть пустыми");
                                httpExchange.sendResponseHeaders(400, 0);
                                break;
                            }
                            Epic epic = gson.fromJson(body, Epic.class);
                            if (body.contains("id")) {
                                taskManager.updateEpic(epic);
                                System.out.println("Эпик обновлен");
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                taskManager.createEpic(epic);
                                System.out.println("Эпик добавлен");
                                httpExchange.sendResponseHeaders(200, 0);
                            }
                        } catch (JsonSyntaxException e) {
                            System.out.println("Получен некорректный JSON");
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                        break;
                    }
                    if (Pattern.matches("^/tasks/subtask/$", path)) {
                        try (InputStream inputStream = httpExchange.getRequestBody()) {
                            String body = new String(inputStream.readAllBytes());
                            gson.toJson(body);
                            if (!(body.contains("name"))
                                    || !(body.contains("status"))
                                    || !(body.contains("description"))
                                    || !(body.contains("duration"))
                                    || !(body.contains("startTime"))
                                    || !(body.contains("epicId"))) {
                                System.out.println("Поля не могут быть пустыми");
                                httpExchange.sendResponseHeaders(400, 0);
                                break;
                            }
                            Subtask subtask = gson.fromJson(body, Subtask.class);
                            if (body.contains("id")) {
                                taskManager.updateSubtask(subtask);
                                System.out.println("Подзадача обновлена");
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                taskManager.createSubtask(subtask);
                                System.out.println("Подзадача добавлена");
                                httpExchange.sendResponseHeaders(200, 0);
                            }
                        } catch (JsonSyntaxException e) {
                            System.out.println("Получен некорректный JSON");
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                        break;
                    }
                    break;
                case "DELETE":
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        taskManager.deleteAllTasks();
                        System.out.println("Удалены все задачи");
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    }
                    if (Pattern.matches("^/tasks/epic/$", path)) {
                        taskManager.deleteAllEpics();
                        System.out.println("Удалены все эпики");
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    }
                    if (Pattern.matches("^/tasks/subtask/$", path)) {
                        taskManager.deleteAllSubtasks();
                        System.out.println("Удалены все подзадачи");
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    }

                    if (Pattern.matches("^/tasks/task/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/task/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.removeTaskById(id);
                            System.out.println("Удалили задачу с id = " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Получен неверный id задачи");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                        break;
                    }
                    if (Pattern.matches("^/tasks/epic/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/epic/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.removeEpicById(id);
                            System.out.println("Удалили эпик с id = " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Получен неверный id задачи");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                        break;
                    }
                    if (Pattern.matches("^/tasks/subtask/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/subtask/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.removeSubtaskById(id);
                            System.out.println("Удалили подзадачу с id = " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Получен неверный id задачи");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                        break;
                    }
                    break;
                default:
                    System.out.println("Указан необрабатываемый запрос");
                    httpExchange.sendResponseHeaders(405, 0);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
