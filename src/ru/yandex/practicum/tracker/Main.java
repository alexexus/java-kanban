package ru.yandex.practicum.tracker;

import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.Subtask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.model.TaskStatus;
import ru.yandex.practicum.tracker.service.HistoryManager;
import ru.yandex.practicum.tracker.service.InMemoryTaskManager;
import ru.yandex.practicum.tracker.service.Managers;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task1 = new Task();
        task1.setName("Задача #1");
        task1.setDescription("Описание задачи #1");
        task1.setStatus(TaskStatus.NEW);
        task1.setId(inMemoryTaskManager.getGeneratorId());

        Task task2 = new Task();
        task2.setName("Задача #2");
        task2.setDescription("Описание задачи #2");
        task2.setStatus(TaskStatus.NEW);
        task2.setId(inMemoryTaskManager.getGeneratorId());

        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);

        Epic epic1 = new Epic();
        epic1.setName("Эпик #1");
        epic1.setDescription("Описание эпика #1");
        epic1.setStatus(TaskStatus.NEW);
        epic1.setId(inMemoryTaskManager.getGeneratorId());
        epic1.setSubtaskIds(new ArrayList<>());

        inMemoryTaskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask();
        subtask1.setName("Подзадача #1 в эпике #1");
        subtask1.setDescription("Описание подзадачи #1 в эпике #1");
        subtask1.setStatus(TaskStatus.NEW);
        subtask1.setId(inMemoryTaskManager.getGeneratorId());
        subtask1.setEpicId(3);

        inMemoryTaskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask();
        subtask2.setName("Подзадача #2 в эпике #1");
        subtask2.setDescription("Описание подзадачи #2 в эпике #1");
        subtask2.setStatus(TaskStatus.NEW);
        subtask2.setId(inMemoryTaskManager.getGeneratorId());
        subtask2.setEpicId(3);

        inMemoryTaskManager.createSubtask(subtask2);

        Subtask subtask3 = new Subtask();
        subtask3.setName("Подзадача #3 в эпике #1");
        subtask3.setDescription("Описание подзадачи #3 в эпике #1");
        subtask3.setStatus(TaskStatus.NEW);
        subtask3.setId(inMemoryTaskManager.getGeneratorId());
        subtask3.setEpicId(3);

        inMemoryTaskManager.createSubtask(subtask3);

        Epic epic2 = new Epic();
        epic2.setName("Эпик #2");
        epic2.setDescription("Описание эпика #2");
        epic2.setStatus(TaskStatus.NEW);
        epic2.setId(inMemoryTaskManager.getGeneratorId());
        epic2.setSubtaskIds(new ArrayList<>());

        inMemoryTaskManager.createEpic(epic2);

        printAllTasks(inMemoryTaskManager);

        printHistory(historyManager);

        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getEpicById(3);
        inMemoryTaskManager.getEpicById(7);
        inMemoryTaskManager.getSubtaskById(4);
        inMemoryTaskManager.getSubtaskById(6);

        printHistory(historyManager);

        inMemoryTaskManager.removeTaskById(1);

        printHistory(historyManager);

        inMemoryTaskManager.removeSubtaskById(4);

        printHistory(historyManager);

        inMemoryTaskManager.removeEpicById(3);

        printHistory(historyManager);
    }

    private static void printHistory(HistoryManager historyManager) {
        System.out.println();
        System.out.println("История просмотров:");
        for (Task task : historyManager.getHistory()) {
            System.out.printf(task.getId() + " ");
        }
    }

    private static void printAllTasks(InMemoryTaskManager inMemoryTaskManager) {
        for (Task task : inMemoryTaskManager.getTasks()) {
            System.out.println("Имя: " + task.getName() + " " + "Описание: " + task.getDescription()
                    + " " + "Статус: " + task.getStatus() + " " + "ID: " + task.getId());
        }

        System.out.println();

        for (Epic epic : inMemoryTaskManager.getEpics()) {
            System.out.println("Имя: " + epic.getName() + " " + "Описание: " + epic.getDescription()
                    + " " + "Статус: " + epic.getStatus() + " " + "ID: " + epic.getId() + " " + "ID подзадач: " + epic.getSubtaskIds());
        }

        System.out.println();

        for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
            System.out.println("Имя: " + subtask.getName() + " " + "Описание: " + subtask.getDescription()
                    + " " + "Статус: " + subtask.getStatus() + " " + "ID: " + subtask.getId() + " " + "ID эпика: " + subtask.getEpicId());
        }
    }
}
