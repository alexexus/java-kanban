package ru.yandex.practicum.tracker;

import java.util.ArrayList;
import java.util.HashMap;

public class TasksManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int generatorId = 0;

    public int getGeneratorId() {
        generatorId++;
        return generatorId;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void deleteAllTasks() {
        getTasks().clear();
    }

    public void deleteAllEpics() {
        getEpics().clear();
    }

    public void deleteAllSubtasks() {
        getSubtasks().clear();
    }

    public Task getTaskById(int taskId) {
        Task task = null;
        for (int ids : getTasks().keySet()) {
            if (ids == taskId) {
                task = getTasks().get(ids);
            }
        }
        return task;
    }

    public Epic getEpicById(int epicId) {
        Epic epic = null;
        for (int ids : getEpics().keySet()) {
            if (ids == epicId) {
                epic = getEpics().get(ids);
            }
        }
        return epic;
    }

    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = null;
        for (int ids : getSubtasks().keySet()) {
            if (ids == subtaskId) {
                subtask = getSubtasks().get(ids);
            }
        }
        return subtask;
    }

    public void createTask(Task task) {
        getTasks().put(task.getId(), task);
    }

    public void createEpic(Epic epic) {
        getEpics().put(epic.getId(), epic);
    }

    public void createSubtask(Subtask subtask) {
        getSubtasks().put(subtask.getId(), subtask);
        getEpics().get(subtask.getEpicId()).getSubTaskIds().add(subtask.getId());

        updateEpicStatusBySubtasks(subtask);
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

        if (epicToUpdate.getSubTaskIds().isEmpty()) {
            epicToUpdate.setStatus(TaskStatus.NEW);
        }
        for (Integer idSubtask : epicToUpdate.getSubTaskIds()) {
            boolean allSubtasksStatusIsNew = getSubtaskById(idSubtask).getStatus().equals(TaskStatus.NEW);
            boolean allSubtasksStatusIsDone = getSubtaskById(idSubtask).getStatus().equals(TaskStatus.DONE);
            if (allSubtasksStatusIsNew) {
                epicToUpdate.setStatus(TaskStatus.NEW);
            } else if (allSubtasksStatusIsDone) {
                epicToUpdate.setStatus(TaskStatus.DONE);
            } else {
                epicToUpdate.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    public void updateSubtask(Subtask subtask) {
        Subtask subtaskToUpdate = getSubtaskById(subtask.getId());
        subtaskToUpdate.setDescription(subtask.getDescription());
        subtaskToUpdate.setStatus(subtask.getStatus());
        subtaskToUpdate.setName(subtask.getName());
        subtaskToUpdate.setEpicId(subtask.getEpicId());

        updateEpicStatusBySubtasks(subtask);
    }

    public void removeTaskById(int taskId) {
        getTasks().remove(taskId);
    }

    public void removeEpicById(int epicId) {
        getEpics().remove(epicId);
    }

    public void removeSubtaskById(int subtaskId) {
        getSubtasks().remove(subtaskId);
    }

    public ArrayList<Integer> getAllSubtasksInEpic(int epicId) {
        return getEpics().get(epicId).getSubTaskIds();
    }

    private void updateEpicStatusBySubtasks(Subtask subtask) {
        for (Integer idSubtask : getEpics().get(subtask.getEpicId()).getSubTaskIds()) {
            boolean allSubtasksStatusIsNew = getSubtaskById(idSubtask).getStatus().equals(TaskStatus.NEW);
            boolean allSubtasksStatusIsDone = getSubtaskById(idSubtask).getStatus().equals(TaskStatus.DONE);
            if (allSubtasksStatusIsNew) {
                getEpics().get(subtask.getEpicId()).setStatus(TaskStatus.NEW);
            } else if (allSubtasksStatusIsDone) {
                getEpics().get(subtask.getEpicId()).setStatus(TaskStatus.DONE);
            } else {
                getEpics().get(subtask.getEpicId()).setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

}
