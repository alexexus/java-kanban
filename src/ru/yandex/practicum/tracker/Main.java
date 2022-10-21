package ru.yandex.practicum.tracker;

import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.Subtask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.service.TasksManager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        TasksManager tasksManager = new TasksManager();

        Task task1 = new Task();
        task1.setName("Task #1");
        task1.setDescription("Description task #1");
        task1.setStatus(TaskStatus.NEW);
        task1.setId(tasksManager.getGeneratorId());

        Task task2 = new Task();
        task2.setName("Task #2");
        task2.setDescription("Description task #2");
        task2.setStatus(TaskStatus.NEW);
        task2.setId(tasksManager.getGeneratorId());

        tasksManager.createTask(task1);
        tasksManager.createTask(task2);

        Epic epic1 = new Epic();
        epic1.setName("Epic #1");
        epic1.setDescription("Description epic #1");
        epic1.setStatus(TaskStatus.NEW);
        epic1.setId(tasksManager.getGeneratorId());
        epic1.setSubTaskIds(new ArrayList<>());

        tasksManager.createEpic(epic1);

        Subtask subtask1 = new Subtask();
        subtask1.setName("Subtask #1 in epic #1");
        subtask1.setDescription("Description subtask #1 in epic #1");
        subtask1.setStatus(TaskStatus.NEW);
        subtask1.setId(tasksManager.getGeneratorId());
        subtask1.setEpicId(3);

        tasksManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask();
        subtask2.setName("Subtask #2 in epic #1");
        subtask2.setDescription("Description subtask #2 in epic #1");
        subtask2.setStatus(TaskStatus.NEW);
        subtask2.setId(tasksManager.getGeneratorId());
        subtask2.setEpicId(3);

        tasksManager.createSubtask(subtask2);

        Epic epic2 = new Epic();
        epic2.setName("Epic #2");
        epic2.setDescription("Description epic #2");
        epic2.setStatus(TaskStatus.NEW);
        epic2.setId(tasksManager.getGeneratorId());
        epic2.setSubTaskIds(new ArrayList<>());

        tasksManager.createEpic(epic2);

        Subtask subtask3 = new Subtask();
        subtask3.setName("Subtask #1 in epic #2");
        subtask3.setDescription("Description subtask #1 in epic #2");
        subtask3.setStatus(TaskStatus.NEW);
        subtask3.setId(tasksManager.getGeneratorId());
        subtask3.setEpicId(6);

        tasksManager.createSubtask(subtask3);

        for (Task task : tasksManager.getTasks()) {
            System.out.println("Name: " + task.getName() + " " + "Description: " + task.getDescription()
                    + " " + "Status: " + task.getStatus() + " " + "Id: " + task.getId());
        }

        System.out.println();

        for (Epic epic : tasksManager.getEpics()) {
            System.out.println("Name: " + epic.getName() + " " + "Description: " + epic.getDescription()
                    + " " + "Status: " + epic.getStatus() + " " + "Id: " + epic.getId() + " " + "Subtasks ids: " + epic.getSubTaskIds());
        }

        System.out.println();

        for (Subtask subtask : tasksManager.getSubtasks()) {
            System.out.println("Name: " + subtask.getName() + " " + "Description: " + subtask.getDescription()
                    + " " + "Status: " + subtask.getStatus() + " " + "Id: " + subtask.getId() + " " + "Epic id: " + subtask.getEpicId());
        }

        System.out.println();

        Task task3 = new Task();
        task3.setName("Task #1 (update)");
        task3.setDescription("Description task #1 (update)");
        task3.setStatus(TaskStatus.IN_PROGRESS);
        task3.setId(1);

        tasksManager.updateTask(task3);

        Task task4 = new Task();
        task4.setName("Task #2 (update)");
        task4.setDescription("Description task #2 (update)");
        task4.setStatus(TaskStatus.IN_PROGRESS);
        task4.setId(2);

        tasksManager.updateTask(task4);

        for (Task task : tasksManager.getTasks()) {
            System.out.println("Name: " + task.getName() + " " + "Description: " + task.getDescription()
                    + " " + "Status: " + task.getStatus() + " " + "Id: " + task.getId());
        }

        System.out.println();

        Epic epic3 = new Epic();
        epic3.setName("Epic #1 (update)");
        epic3.setDescription("Description epic #1 (update)");
        epic3.setStatus(TaskStatus.NEW);
        epic3.setId(3);
        epic3.setSubTaskIds(tasksManager.getEpicById(3).getSubTaskIds());

        tasksManager.updateEpic(epic3);

        Subtask subtask4 = new Subtask();
        subtask4.setName("Subtask #1 in epic #2 (update)");
        subtask4.setDescription("Description subtask #1 in epic #2 (update)");
        subtask4.setStatus(TaskStatus.DONE);
        subtask4.setId(4);
        subtask4.setEpicId(6);

        for (Epic epic : tasksManager.getEpics()) {
            System.out.println("Name: " + epic.getName() + " " + "Description: " + epic.getDescription()
                    + " " + "Status: " + epic.getStatus() + " " + "Id: " + epic.getId() + " " + "Subtasks ids: " + epic.getSubTaskIds());
        }

        System.out.println();

        tasksManager.updateSubtask(subtask4);

        Subtask subtask5 = new Subtask();
        subtask5.setName("Subtask #2 in epic #1 (update)");
        subtask5.setDescription("Description subtask #2 in epic #1 (update)");
        subtask5.setStatus(TaskStatus.IN_PROGRESS);
        subtask5.setId(5);
        subtask5.setEpicId(3);

        tasksManager.updateSubtask(subtask5);

        for (Subtask subtask : tasksManager.getSubtasks()) {
            System.out.println("Name: " + subtask.getName() + " " + "Description: " + subtask.getDescription()
                    + " " + "Status: " + subtask.getStatus() + " " + "Id: " + subtask.getId() + " " + "Epic id: " + subtask.getEpicId());
        }

        System.out.println();

        tasksManager.removeTaskById(2);
        tasksManager.removeEpicById(3);

        for (Epic epic : tasksManager.getEpics()) {
            System.out.println("Name: " + epic.getName() + " " + "Description: " + epic.getDescription()
                    + " " + "Status: " + epic.getStatus() + " " + "Id: " + epic.getId() + " " + "Subtasks ids: " + epic.getSubTaskIds());
        }

        System.out.println();

        for (Task task : tasksManager.getTasks()) {
            System.out.println("Name: " + task.getName() + " " + "Description: " + task.getDescription()
                    + " " + "Status: " + task.getStatus() + " " + "Id: " + task.getId());
        }
    }
}
