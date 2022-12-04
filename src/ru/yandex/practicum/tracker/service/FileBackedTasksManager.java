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

    File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {

//        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(new File("src/ru/yandex/practicum/tracker/service/file.csv"));
//        Task task1 = new Task();
//        task1.setName("Задача #1");
//        task1.setDescription("Описание задачи #1");
//        task1.setStatus(TaskStatus.NEW);
//        fileBackedTasksManager.createTask(task1);
//        Epic epic1 = new Epic();
//        epic1.setName("Эпик #1");
//        epic1.setDescription("Описание эпика #1");
//        epic1.setStatus(TaskStatus.NEW);
//        fileBackedTasksManager.createEpic(epic1);
//        Subtask subtask1 = new Subtask();
//        subtask1.setName("Подзадача #1 в эпике #1");
//        subtask1.setDescription("Описание подзадачи #1 в эпике #1");
//        subtask1.setStatus(TaskStatus.NEW);
//        subtask1.setEpicId(2);
//        fileBackedTasksManager.createSubtask(subtask1);
//        fileBackedTasksManager.getTaskById(1);
//        fileBackedTasksManager.getEpicById(2);
//        fileBackedTasksManager.getSubtaskById(3);

        FileBackedTasksManager fileBackedTasksManager1 = loadFromFile(new File("src/ru/yandex/practicum/tracker/service/file.csv"));
//        System.out.println(fileBackedTasksManager1.getTaskById(1));
//        System.out.println(fileBackedTasksManager1.getEpicById(2));
//        System.out.println(fileBackedTasksManager1.getSubtaskById(3));
//        System.out.println(fileBackedTasksManager1.getTaskById(4));
//        System.out.println(fileBackedTasksManager1.getSubtaskById(5));
//        System.out.println(Managers.getDefaultHistory().getHistory());

    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter("src/ru/yandex/practicum/tracker/service/file.csv")) {
            for (Task task : super.getTasks()) {
                fileWriter.write(toString(task) + "\n");
            }
            for (Epic epic : super.getEpics()) {
                fileWriter.write(toString(epic) + "\n");
            }
            for (Subtask subtask : super.getSubtasks()) {
                fileWriter.write(toString(subtask) + "\n");
            }

            fileWriter.write("\n");
            fileWriter.write(historyToString(Managers.getDefaultHistory()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer task : manager.getHistory()) {
            stringBuilder.append(task).append(",");
        }
        return stringBuilder.toString();
    }

    public static List<Task> historyFromString(String value) {
        List<Task> tasks = new ArrayList<>();
        String[] split = value.split(",");
        for (String str : split) {
            Task task = new Task();
            task.setId(Integer.parseInt(str));
            tasks.add(task);
        }
        return tasks;
    }

    public String toString(Task task) {
        String str = "";
        switch (task.getClass().getSimpleName()) {
            case "Task":
                str = task.getId() + ","
                        + task.getClass().getSimpleName() + ","
                        + task.getName() + ","
                        + task.getStatus() + ","
                        + task.getDescription();
                break;
            case "Epic":
                Epic epic = (Epic) task;
                str = epic.getId() + ","
                        + epic.getClass().getSimpleName() + ","
                        + epic.getName() + ","
                        + epic.getStatus() + ","
                        + epic.getDescription();
                break;
            case "Subtask":
                Subtask subtask = (Subtask) task;
                str = subtask.getId() + ","
                        + subtask.getClass().getSimpleName() + ","
                        + subtask.getName() + ","
                        + subtask.getStatus() + ","
                        + subtask.getDescription() + ","
                        + subtask.getEpicId();
                break;
        }
        return str;
    }

    public static Task fromString(String value) {
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

    /*
    Пара вопросов у меня появилось:
    1) Если загружаем из файла "file1" где была такая структура:

    1,Task,Задача #1,NEW,Описание задачи #1
    2,Epic,Эпик #1,NEW,Описание эпика #1
    3,Subtask,Подзадача #1 в эпике #1,NEW,Описание подзадачи #1 в эпике #1,2
    4,Task,Задача #1,NEW,Описание задачи #1
    5,Subtask,Подзадача #1 в эпике #1,NEW,Описание подзадачи #1 в эпике #1,2

    То на выходе в файле "file" структура будет в виде:

    1,Task,Задача #1,NEW,Описание задачи #1
    4,Task,Задача #1,NEW,Описание задачи #1
    2,Epic,Эпик #1,NEW,Описание эпика #1
    3,Subtask,Подзадача #1 в эпике #1,NEW,Описание подзадачи #1 в эпике #1,2
    5,Subtask,Подзадача #1 в эпике #1,NEW,Описание подзадачи #1 в эпике #1,2

    Во-первых, меняется очередность, не понятно почему, во-вторых, если попробовать загрузить с этого файла еще раз,
    то у первой "Task" будет "id" - 1, потом создается вторая "Task", но "id" у нее не 4, а почему-то 2, что неверно.

    В общем не могу понять как присваивать "id" который в строке, а не который генерирует "generatorId".
     */
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
                        fileBackedTasksManager.createTask(fromString(str));
                        break;
                    case "Epic":
                        fileBackedTasksManager.createEpic((Epic) fromString(str));
                        break;
                    case "Subtask":
                        fileBackedTasksManager.createSubtask((Subtask) fromString(str));
                        break;
                    default:
                        for (Task task : historyFromString(str)) {
                            Managers.getDefaultHistory().add(task);
                        }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBackedTasksManager;
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
