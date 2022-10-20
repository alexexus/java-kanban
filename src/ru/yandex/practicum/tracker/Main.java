package ru.yandex.practicum.tracker;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        TasksManager tasksManager = new TasksManager();

        tasksManager.createTask(new Task("Задача 1", TaskStatus.NEW, tasksManager.getGeneratorId(), "Описание задачи 1"));
        tasksManager.createTask(new Task("Задача 2", TaskStatus.NEW, tasksManager.getGeneratorId(), "Описание задачи 2"));

        tasksManager.createEpic(new Epic("Эпик 1", TaskStatus.NEW, tasksManager.getGeneratorId(), "Описание эпика 1", new ArrayList<>()));
        tasksManager.createSubtask(new Subtask("Подзадача 1 в эпике 1", TaskStatus.NEW,
                tasksManager.getGeneratorId(), "Описание подзадачи 1 в эпике 1", 3));
        tasksManager.createSubtask(new Subtask("Подзадача 2 в эпике 1", TaskStatus.NEW,
                tasksManager.getGeneratorId(), "Описание подзадачи 2 в эпике 1", 3));

        tasksManager.createEpic(new Epic("Эпик 2", TaskStatus.NEW, tasksManager.getGeneratorId(), "Описание эпика 2", new ArrayList<>()));
        tasksManager.createSubtask(new Subtask("Подзадача 1 в эпике 2", TaskStatus.NEW,
                tasksManager.getGeneratorId(), "Описание подзадачи 1 в эпике 2", 6));

        for (Task task : tasksManager.getTasks().values()) {
            System.out.println("Имя: " + task.getName() + " " + "Описание: " + task.getDescription()
                    + " " + "Статус: " + task.getStatus() + " " + "Номер: " + task.getId());
        }

        for (Epic epic : tasksManager.getEpics().values()) {
            System.out.println("Имя: " + epic.getName() + " " + "Описание: " + epic.getDescription()
                    + " " + "Статус: " + epic.getStatus() + " " + "Номер: " + epic.getId() + " " + "Номера сабтасков: " + epic.getSubTaskIds());
        }

        for (Subtask subtask : tasksManager.getSubtasks().values()) {
            System.out.println("Имя: " + subtask.getName() + " " + "Описание: " + subtask.getDescription()
                    + " " + "Статус: " + subtask.getStatus() + " " + "Номер: " + subtask.getId() + " " + "Номер эпика: " + subtask.getEpicId());
        }

        tasksManager.updateTask(new Task("Задача 1 (update)", TaskStatus.IN_PROGRESS, 1, "Описание задачи 1 (update)"));
        tasksManager.updateTask(new Task("Задача 2 (update)", TaskStatus.IN_PROGRESS, 2, "Описание задачи 2 (update)"));

        for (Task task : tasksManager.getTasks().values()) {
            System.out.println("Имя: " + task.getName() + " " + "Описание: " + task.getDescription()
                    + " " + "Статус: " + task.getStatus() + " " + "Номер: " + task.getId());
        }

        tasksManager.updateEpic(new Epic("Эпик 1 (update)", TaskStatus.NEW, 3,
                "Описание эпика 1 (update)", tasksManager.getEpics().get(3).getSubTaskIds()));
        tasksManager.updateSubtask(new Subtask("Подзадача 1 в эпике 2 (update)", TaskStatus.DONE,
                4, "Описание подзадачи 1 в эпике 2 (update)", 6));
        tasksManager.updateSubtask(new Subtask("Подзадача 2 в эпике 1 (update)", TaskStatus.IN_PROGRESS,
                5, "Описание подзадачи 2 в эпике 1 (update)", 3));

        for (Epic epic : tasksManager.getEpics().values()) {
            System.out.println("Имя: " + epic.getName() + " " + "Описание: " + epic.getDescription()
                    + " " + "Статус: " + epic.getStatus() + " " + "Номер: " + epic.getId() + " " + "Номера сабтасков: " + epic.getSubTaskIds());
        }

        for (Subtask subtask : tasksManager.getSubtasks().values()) {
            System.out.println("Имя: " + subtask.getName() + " " + "Описание: " + subtask.getDescription()
                    + " " + "Статус: " + subtask.getStatus() + " " + "Номер: " + subtask.getId() + " " + "Номер эпика: " + subtask.getEpicId());
        }

        tasksManager.removeTaskById(2);
        tasksManager.removeEpicById(3);

        for (Epic epic : tasksManager.getEpics().values()) {
            System.out.println("Имя: " + epic.getName() + " " + "Описание: " + epic.getDescription()
                    + " " + "Статус: " + epic.getStatus() + " " + "Номер: " + epic.getId() + " " + "Номера сабтасков: " + epic.getSubTaskIds());
        }

        for (Task task : tasksManager.getTasks().values()) {
            System.out.println("Имя: " + task.getName() + " " + "Описание: " + task.getDescription()
                    + " " + "Статус: " + task.getStatus() + " " + "Номер: " + task.getId());
        }
    }
}
