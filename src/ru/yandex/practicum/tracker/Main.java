package ru.yandex.practicum.tracker;

import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.Subtask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.model.TaskStatus;
import ru.yandex.practicum.tracker.service.*;

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
        epic1.setSubTaskIds(new ArrayList<>());

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

        Epic epic2 = new Epic();
        epic2.setName("Эпик #2");
        epic2.setDescription("Описание эпика #2");
        epic2.setStatus(TaskStatus.NEW);
        epic2.setId(inMemoryTaskManager.getGeneratorId());
        epic2.setSubTaskIds(new ArrayList<>());

        inMemoryTaskManager.createEpic(epic2);

        Subtask subtask3 = new Subtask();
        subtask3.setName("Подзадача #1 в эпике #2");
        subtask3.setDescription("Описание подзадачи #1 в эпике #2");
        subtask3.setStatus(TaskStatus.NEW);
        subtask3.setId(inMemoryTaskManager.getGeneratorId());
        subtask3.setEpicId(6);

        inMemoryTaskManager.createSubtask(subtask3);

        for (Task task : inMemoryTaskManager.getTasks()) {
            System.out.println("Имя: " + task.getName() + " " + "Описание: " + task.getDescription()
                    + " " + "Статус: " + task.getStatus() + " " + "ID: " + task.getId());
        }

        System.out.println();

        for (Epic epic : inMemoryTaskManager.getEpics()) {
            System.out.println("Имя: " + epic.getName() + " " + "Описание: " + epic.getDescription()
                    + " " + "Статус: " + epic.getStatus() + " " + "ID: " + epic.getId() + " " + "ID подзадач: " + epic.getSubTaskIds());
        }

        System.out.println();

        for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
            System.out.println("Имя: " + subtask.getName() + " " + "Описание: " + subtask.getDescription()
                    + " " + "Статус: " + subtask.getStatus() + " " + "ID: " + subtask.getId() + " " + "ID эпика: " + subtask.getEpicId());
        }

        System.out.println();

        Task task3 = new Task();
        task3.setName("Задача №1 (update)");
        task3.setDescription("Описание задача №1 (update)");
        task3.setStatus(TaskStatus.IN_PROGRESS);
        task3.setId(1);

        inMemoryTaskManager.updateTask(task3);

        Task task4 = new Task();
        task4.setName("Задача #2 (update)");
        task4.setDescription("Описание задачи #2 (update)");
        task4.setStatus(TaskStatus.IN_PROGRESS);
        task4.setId(2);

        inMemoryTaskManager.updateTask(task4);

        for (Task task : inMemoryTaskManager.getTasks()) {
            System.out.println("Имя: " + task.getName() + " " + "Описание: " + task.getDescription()
                    + " " + "Статус: " + task.getStatus() + " " + "ID: " + task.getId());
        }

        System.out.println();

        Epic epic3 = new Epic();
        epic3.setName("Эпик #1 (update)");
        epic3.setDescription("Описание эпика #1 (update)");
        epic3.setStatus(TaskStatus.NEW);
        epic3.setId(3);
        epic3.setSubTaskIds(inMemoryTaskManager.getEpicById(3).getSubTaskIds());

        inMemoryTaskManager.updateEpic(epic3);

        Subtask subtask4 = new Subtask();
        subtask4.setName("Подзадача #1 в эпике #1 (update)");
        subtask4.setDescription("Описание подзадачи #1 в эпике #1 (update)");
        subtask4.setStatus(TaskStatus.DONE);
        subtask4.setId(4);
        subtask4.setEpicId(3);

        for (Epic epic : inMemoryTaskManager.getEpics()) {
            System.out.println("Имя: " + epic.getName() + " " + "Описание: " + epic.getDescription()
                    + " " + "Статус: " + epic.getStatus() + " " + "ID: " + epic.getId() + " " + "ID подзадач: " + epic.getSubTaskIds());
        }

        System.out.println();

        inMemoryTaskManager.updateSubtask(subtask4);

        Subtask subtask5 = new Subtask();
        subtask5.setName("Подзадача #2 в эпике #1 (update)");
        subtask5.setDescription("Описание подзадачи #2 в эпике #1 (update)");
        subtask5.setStatus(TaskStatus.DONE);
        subtask5.setId(5);
        subtask5.setEpicId(3);

        inMemoryTaskManager.updateSubtask(subtask5);

        for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
            System.out.println("Имя: " + subtask.getName() + " " + "Описание: " + subtask.getDescription()
                    + " " + "Статус: " + subtask.getStatus() + " " + "ID: " + subtask.getId() + " " + "ID эпика: " + subtask.getEpicId());
        }

        System.out.println();

        for (Epic epic : inMemoryTaskManager.getEpics()) {
            System.out.println("Имя: " + epic.getName() + " " + "Описание: " + epic.getDescription()
                    + " " + "Статус: " + epic.getStatus() + " " + "ID: " + epic.getId() + " " + "ID подзадач: " + epic.getSubTaskIds());
        }

        System.out.println();

        inMemoryTaskManager.removeTaskById(2);
        inMemoryTaskManager.removeEpicById(6);
        inMemoryTaskManager.removeSubtaskById(5);

        for (Task task : inMemoryTaskManager.getTasks()) {
            System.out.println("Имя: " + task.getName() + " " + "Описание: " + task.getDescription()
                    + " " + "Статус: " + task.getStatus() + " " + "ID: " + task.getId());
        }

        System.out.println();

        for (Epic epic : inMemoryTaskManager.getEpics()) {
            System.out.println("Имя: " + epic.getName() + " " + "Описание: " + epic.getDescription()
                    + " " + "Статус: " + epic.getStatus() + " " + "ID: " + epic.getId() + " " + "ID подзадач: " + epic.getSubTaskIds());
        }

        System.out.println();

        for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
            System.out.println("Имя: " + subtask.getName() + " " + "Описание: " + subtask.getDescription()
                    + " " + "Статус: " + subtask.getStatus() + " " + "ID: " + subtask.getId() + " " + "ID эпика: " + subtask.getEpicId());
        }

        System.out.println("История просмотров:");

        for (Task task : historyManager.getHistory()) {
            System.out.printf(task.getId() + " ");
        }
    }
}
