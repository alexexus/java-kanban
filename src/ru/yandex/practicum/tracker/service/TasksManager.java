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

    public int getGeneratorId() {
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

        updateEpicStatusBySubtasks(subtask);
    }

    public void updateTask(Task task) { // Так рекомендовал делать наставник
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
        updateEpicStatus(epicToUpdate);
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
        tasks.remove(taskId);
    }

    public void removeEpicById(int epicId) {
        epics.remove(epicId);
        for (Subtask subtask : getSubtasks()) {
            if (subtask.getEpicId() == epicId) {
                getTasks().remove(subtask);
            }
        }
    }

    public void removeSubtaskById(int subtaskId) {
        subtasks.remove(subtaskId);
        getEpics().get(getSubtasks().get(subtaskId).getEpicId()).getSubTaskIds().remove(subtaskId);
        updateEpicStatusBySubtasks(getSubtaskById(subtaskId));
    }

    public ArrayList<Subtask> getAllSubtasksInEpic(int epicId) {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
        for (Subtask subtask : getSubtasks())
            if (subtask.getEpicId() == epicId) {
                subtaskArrayList.add(subtask);
            }
        return subtaskArrayList;
    }

    private void updateEpicStatusBySubtasks(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        updateEpicStatus(epic);
    }

    private void updateEpicStatus(Epic epic) {
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
