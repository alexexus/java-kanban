package ru.yandex.practicum.tracker.service;

import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.Subtask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.model.TaskStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    private FileBackedTasksManager(File file) {
        this.file = file;
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter("resources/file.csv")) {
            for (Task task : getTasks()) {
                fileWriter.write(task.toCsvRow(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                fileWriter.write(epic.toCsvRow(epic) + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                fileWriter.write(subtask.toCsvRow(subtask) + "\n");
            }

            fileWriter.write("\n");
            fileWriter.write(historyToString(Managers.getDefaultHistory()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String historyToString(HistoryManager manager) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer task : manager.getHistory()) {
            stringBuilder.append(task).append(",");
        }
        return stringBuilder.toString();
    }

    private static List<Task> historyFromString(String value) {
        List<Task> tasks = new ArrayList<>();
        String[] split = value.split(",");
        for (String str : split) {
            Task task = new Task();
            task.setId(Integer.parseInt(str));
            tasks.add(task);
        }
        return tasks;
    }

    private static Task fromString(String value) {
        Task task = new Task();
        String[] split = value.split(",");
        switch (split[1]) {
            case "Task":
                task.setId(Integer.parseInt(split[0]));
                task.setDescription(split[4]);
                task.setStatus(TaskStatus.valueOf(split[3]));
                task.setName(split[2]);
                break;
            case "Epic":
                Epic epic = new Epic();
                epic.setId(Integer.parseInt(split[0]));
                epic.setDescription(split[4]);
                epic.setStatus(TaskStatus.valueOf(split[3]));
                epic.setName(split[2]);
                task = epic;
                break;
            case "Subtask":
                Subtask subtask = new Subtask();
                subtask.setId(Integer.parseInt(split[0]));
                subtask.setDescription(split[4]);
                subtask.setStatus(TaskStatus.valueOf(split[3]));
                subtask.setName(split[2]);
                subtask.setEpicId(Integer.parseInt(split[5]));
                task = subtask;
                break;
        }
        return task;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            while (fileReader.ready()) {
                String str = fileReader.readLine();
                if (str.isEmpty()) {
                    continue;
                }
                String[] split = str.split(",");
                switch (split[1]) {
                    case "Task":
                        Task task = fromString(str);
                        //generatorId = 0;
                        fileBackedTasksManager.createTask(task);
                        break;
                    case "Epic":
                        Epic epic = (Epic) fromString(str);
                        fileBackedTasksManager.createEpic(epic);
                        break;
                    case "Subtask":
                        Subtask subtask = (Subtask) fromString(str);
                        fileBackedTasksManager.createSubtask(subtask);
                        break;
                    default:
                        for (Task tasksInHistory : historyFromString(str)) {
                            Managers.getDefaultHistory().add(tasksInHistory);
                        }
                }
            }
            fileBackedTasksManager.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBackedTasksManager;
    }

    @Override
    public void createTask(Task task) {
        generatorId = task.getId() - 1;
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        generatorId = epic.getId() - 1;
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        generatorId = subtask.getId() - 1;
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeTaskById(int taskId) {
        super.removeTaskById(taskId);
        save();
    }

    @Override
    public void removeEpicById(int epicId) {
        super.removeEpicById(epicId);
        save();
    }

    @Override
    public void removeSubtaskById(Integer subtaskId) {
        super.removeSubtaskById(subtaskId);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    /*
    Если убрать из методов getById() первую строчку с super-ом,
    то он не будет сохранять в истории последний вызов getById()
    потому что save() будет идти перед самим получение таска.
    То есть мы его получим и в истории он будет, но в файле нет.

    А как вызвать метод save() после return я не знаю.
     */

    @Override
    public Task getTaskById(int taskId) {
        super.getTaskById(taskId);
        save();
        return super.getTaskById(taskId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        super.getEpicById(epicId);
        save();
        return super.getEpicById(epicId);
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        super.getSubtaskById(subtaskId);
        save();
        return super.getSubtaskById(subtaskId);
    }

}
