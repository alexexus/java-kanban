package ru.yandex.practicum.tracker.service;

import ru.yandex.practicum.tracker.TaskStatus;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.Subtask;
import ru.yandex.practicum.tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int generatorId = 0;

    HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public int generateId() {
        return ++generatorId;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : getEpics()) {
            epic.setStatus(TaskStatus.NEW);
            epic.getSubTaskIds().clear();
        }
    }

    @Override
    public Task getTaskById(int taskId) {
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        historyManager.add(epics.get(epicId));
        return epics.get(epicId);
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        historyManager.add(subtasks.get(subtaskId));
        return subtasks.get(subtaskId);
    }

    @Override
    public void createTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).getSubTaskIds().add(subtask.getId());

        updateEpicStatus(epics.get(subtask.getEpicId()));
    }

    @Override
    public void updateTask(Task task) {
        Task taskToUpdate = getTaskById(task.getId());
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setStatus(task.getStatus());
        taskToUpdate.setName(task.getName());
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic epicToUpdate = getEpicById(epic.getId());
        epicToUpdate.setDescription(epic.getDescription());
        epicToUpdate.setName(epic.getName());
        epicToUpdate.setSubTaskIds(epic.getSubTaskIds());

        updateEpicStatus(epicToUpdate);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask subtaskToUpdate = getSubtaskById(subtask.getId());
        subtaskToUpdate.setDescription(subtask.getDescription());
        subtaskToUpdate.setStatus(subtask.getStatus());
        subtaskToUpdate.setName(subtask.getName());
        subtaskToUpdate.setEpicId(subtask.getEpicId());

        updateEpicStatus(epics.get(subtask.getEpicId()));
    }

    @Override
    public void removeTaskById(int taskId) {
        tasks.remove(taskId);
    }

    @Override
    public void removeEpicById(int epicId) {
        epics.remove(epicId);
        for (Subtask subtask : getSubtasks()) {
            if (subtask.getEpicId() == epicId) {
                subtasks.remove(subtask.getId());
            }
        }
    }

    @Override
    public void removeSubtaskById(Integer subtaskId) {
        Epic epic = getEpicById(getSubtaskById(subtaskId).getEpicId());
        epic.getSubTaskIds().remove(subtaskId);
        updateEpicStatus(epic);
        subtasks.remove(subtaskId);
    }

    @Override
    public ArrayList<Subtask> getSubtasksByEpicId(int epicId) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Subtask subtask : getSubtasks())
            if (subtask.getEpicId() == epicId) {
                subtasks.add(subtask);
            }
        return subtasks;
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        if (epic.getSubTaskIds().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            boolean allSubtasksStatusIsNew = false;
            boolean allSubtasksStatusIsDone = false;
            for (Integer idSubtask : epic.getSubTaskIds()) {
                if (!(getSubtasks().isEmpty())) {
                    allSubtasksStatusIsNew = getSubtaskById(idSubtask).getStatus().equals(TaskStatus.NEW);
                    allSubtasksStatusIsDone = getSubtaskById(idSubtask).getStatus().equals(TaskStatus.DONE);
                }
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
}
