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

    private static final int ID_INDEX_IN_CSV_ROW = 0;
    private static final int CLASS_INDEX_IN_CSV_ROW = 1;
    private static final int NAME_INDEX_IN_CSV_ROW = 2;
    private static final int STATUS_INDEX_IN_CSV_ROW = 3;
    private static final int DESCRIPTION_INDEX_IN_CSV_ROW = 4;
    private static final int EPIC_ID_INDEX_IN_CSV_ROW = 5;

    private FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
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

    @Override
    public Task getTaskById(int taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = super.getEpicById(epicId);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = super.getSubtaskById(subtaskId);
        save();
        return subtask;
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
                switch (split[CLASS_INDEX_IN_CSV_ROW]) {
                    case "Task":
                        Task task = fromString(str);
                        fileBackedTasksManager.updateTask(task);
                        if (task.getId() > fileBackedTasksManager.generatorId) {
                            fileBackedTasksManager.generatorId = task.getId();
                        }
                        break;
                    case "Epic":
                        Epic epic = (Epic) fromString(str);
                        fileBackedTasksManager.updateEpic(epic);
                        if (epic.getId() > fileBackedTasksManager.generatorId) {
                            fileBackedTasksManager.generatorId = epic.getId();
                        }
                        break;
                    case "Subtask":
                        Subtask subtask = (Subtask) fromString(str);
                        fileBackedTasksManager.updateSubtask(subtask);
                        if (subtask.getId() > fileBackedTasksManager.generatorId) {
                            fileBackedTasksManager.generatorId = subtask.getId();
                        }
                        break;
                    default:
                        for (Task tasksInHistory : historyFromString(str)) {
                            Managers.getDefaultHistory().add(tasksInHistory);
                        }
                }
            }
            fileBackedTasksManager.save();
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        return fileBackedTasksManager;
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter("resources/file.csv")) {
            for (Task task : getTasks()) {
                fileWriter.write(task.toCsvRow() + "\n");
            }
            for (Epic epic : getEpics()) {
                fileWriter.write(epic.toCsvRow() + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                fileWriter.write(subtask.toCsvRow() + "\n");
            }

            fileWriter.write("\n");
            fileWriter.write(historyToString(Managers.getDefaultHistory().getHistory()));

        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    private static String historyToString(List<Integer> taskIds) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer taskId : taskIds) {
            stringBuilder.append(taskId).append(",");
        }
        return stringBuilder.toString();
    }

    private static List<Task> historyFromString(String value) {
        List<Task> tasks = new ArrayList<>();
        String[] ids = value.split(",");
        for (String id : ids) {
            Task task = new Task();
            task.setId(Integer.parseInt(id));
            tasks.add(task);
        }
        return tasks;
    }

    private static Task fromString(String value) {
        Task task = new Task();
        String[] split = value.split(",");
        switch (split[1]) {
            case "Task":
                task.setId(Integer.parseInt(split[ID_INDEX_IN_CSV_ROW]));
                task.setDescription(split[DESCRIPTION_INDEX_IN_CSV_ROW]);
                task.setStatus(TaskStatus.valueOf(split[STATUS_INDEX_IN_CSV_ROW]));
                task.setName(split[NAME_INDEX_IN_CSV_ROW]);
                break;
            case "Epic":
                Epic epic = new Epic();
                epic.setId(Integer.parseInt(split[ID_INDEX_IN_CSV_ROW]));
                epic.setDescription(split[DESCRIPTION_INDEX_IN_CSV_ROW]);
                epic.setStatus(TaskStatus.valueOf(split[STATUS_INDEX_IN_CSV_ROW]));
                epic.setName(split[NAME_INDEX_IN_CSV_ROW]);
                task = epic;
                break;
            case "Subtask":
                Subtask subtask = new Subtask();
                subtask.setId(Integer.parseInt(split[ID_INDEX_IN_CSV_ROW]));
                subtask.setDescription(split[DESCRIPTION_INDEX_IN_CSV_ROW]);
                subtask.setStatus(TaskStatus.valueOf(split[STATUS_INDEX_IN_CSV_ROW]));
                subtask.setName(split[NAME_INDEX_IN_CSV_ROW]);
                subtask.setEpicId(Integer.parseInt(split[EPIC_ID_INDEX_IN_CSV_ROW]));
                task = subtask;
                break;
        }
        return task;
    }

}
