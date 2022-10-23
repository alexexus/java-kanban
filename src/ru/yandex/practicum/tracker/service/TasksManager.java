package ru.yandex.practicum.tracker.service;

import ru.yandex.practicum.tracker.TaskStatus;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.Subtask;
import ru.yandex.practicum.tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TasksManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int generatorId = 0;

    public int generateId() {
        return ++generatorId;
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : getEpics()) {
            epic.setStatus(TaskStatus.NEW);
            epic.getSubTaskIds().clear();
        }
    }

    public Task getTaskById(int taskId) {
        return tasks.get(taskId);
    }

    public Epic getEpicById(int epicId) {
        return epics.get(epicId);
    }

    public Subtask getSubtaskById(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    public void createTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void createEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void createSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).getSubTaskIds().add(subtask.getId());

        updateEpicStatus(epics.get(subtask.getEpicId()));
    }

    public void updateTask(Task task) {
        Task taskToUpdate = getTaskById(task.getId());
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setStatus(task.getStatus());
        taskToUpdate.setName(task.getName());
    }

    public void updateEpic(Epic epic) {
        Epic epicToUpdate = getEpicById(epic.getId());
        epicToUpdate.setDescription(epic.getDescription());
        epicToUpdate.setName(epic.getName());
        epicToUpdate.setSubTaskIds(epic.getSubTaskIds());

        updateEpicStatus(epicToUpdate);
    }

    public void updateSubtask(Subtask subtask) {
        Subtask subtaskToUpdate = getSubtaskById(subtask.getId());
        subtaskToUpdate.setDescription(subtask.getDescription());
        subtaskToUpdate.setStatus(subtask.getStatus());
        subtaskToUpdate.setName(subtask.getName());
        subtaskToUpdate.setEpicId(subtask.getEpicId());

        updateEpicStatus(epics.get(subtask.getEpicId()));
    }

    public void removeTaskById(int taskId) {
        tasks.remove(taskId);
    }

    public void removeEpicById(int epicId) {
        epics.remove(epicId);
        for (Subtask subtask : getSubtasks()) {
            /* Дело в том, что getSubtasksByEpicId() возвращает новый список который создан на основе настоящего,
             то есть он не берет из сабтасков объекты, а просто клонирует их в свой собственный список.*/
            if (subtask.getEpicId() == epicId) {
                subtasks.values().remove(subtask);
            }
        }
    }

    public void removeSubtaskById(Integer subtaskId) {
        subtasks.remove(subtaskId);
        for (Epic epic : getEpics()) {
            epic.getSubTaskIds().removeIf(ids -> ids.equals(subtaskId));
            updateEpicStatus(epic);
        }
    }

    public ArrayList<Subtask> getSubtasksByEpicId(int epicId) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Subtask subtask : getSubtasks())
            if (subtask.getEpicId() == epicId) {
                subtasks.add(subtask);
            }
        return subtasks;
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getSubTaskIds().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        }
        for (Integer idSubtask : epic.getSubTaskIds()) {
            boolean allSubtasksStatusIsNew = getSubtaskById(idSubtask).getStatus().equals(TaskStatus.NEW);
            boolean allSubtasksStatusIsDone = getSubtaskById(idSubtask).getStatus().equals(TaskStatus.DONE);
            if (allSubtasksStatusIsNew) {
                epic.setStatus(TaskStatus.NEW);
            } else if (allSubtasksStatusIsDone) {
                epic.setStatus(TaskStatus.DONE);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }
}
