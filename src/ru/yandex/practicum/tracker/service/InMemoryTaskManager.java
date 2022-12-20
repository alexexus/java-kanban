package ru.yandex.practicum.tracker.service;

import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.Subtask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected int generatorId = 0;

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private int generateId() {
        return ++generatorId;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.keySet().forEach(historyManager::remove);
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.keySet().forEach(historyManager::remove);
        subtasks.keySet().forEach(historyManager::remove);
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.keySet().forEach(historyManager::remove);
        subtasks.clear();
        for (Epic epic : getEpics()) {
            epic.setStatus(TaskStatus.NEW);
            epic.clearSubtaskIds();
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
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtaskId(subtask.getId());

        updateEpic(epics.get(subtask.getEpicId()));
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);

        updateEpicStatus(epic);
        getEndTimeForEpic(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);

        updateEpic(epics.get(subtask.getEpicId()));
    }

    @Override
    public void removeTaskById(int taskId) {
        historyManager.remove(taskId);
        tasks.remove(taskId);
    }

    @Override
    public void removeEpicById(int epicId) {
        for (Subtask subtask : getSubtasksByEpicId(epicId)) {
            historyManager.remove(subtask.getId());
            subtasks.remove(subtask.getId());
        }
        historyManager.remove(epicId);

        epics.get(epicId).clearSubtaskIds();
        epics.remove(epicId);
    }

    @Override
    public void removeSubtaskById(Integer subtaskId) {
        historyManager.remove(subtaskId);
        Epic epic = epics.get(subtasks.get(subtaskId).getEpicId());
        epic.removeSubtaskId(subtaskId);
        updateEpic(epic);
        subtasks.remove(subtaskId);
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        List<Subtask> subtasksInEpic = new ArrayList<>();
        for (Subtask subtask : getSubtasks()) {
            if (subtask.getEpicId() == epicId) {
                subtasksInEpic.add(subtask);
            }
        }
        return subtasksInEpic;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        Set<Task> taskList = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        taskList.addAll(tasks.values());
        taskList.addAll(epics.values());
        taskList.addAll(subtasks.values());
        return taskList;
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getSubtaskIds().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        for (Integer idSubtask : epic.getSubtaskIds()) {
            if (subtasks.get(idSubtask).getStatus().equals(TaskStatus.NEW)) {
                count1++;
            }
            if (subtasks.get(idSubtask).getStatus().equals(TaskStatus.IN_PROGRESS)) {
                count2++;
            }
            if (subtasks.get(idSubtask).getStatus().equals(TaskStatus.DONE)) {
                count3++;
            }
        }
        if (count1 > 0 && count2 == 0 && count3 == 0) {
            epic.setStatus(TaskStatus.NEW);
        } else if (count1 == 0 && count2 == 0 && count3 > 0) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    private void getEndTimeForEpic(Epic epic) {
        int duration = 0;
        LocalDateTime startTime = LocalDateTime.MAX;
        for (Integer integer : epic.getSubtaskIds()) {
            duration += subtasks.get(integer).getDuration();
            if (subtasks.get(integer).getStartTime().isBefore(startTime)) {
                startTime = subtasks.get(integer).getStartTime();
            }
        }
        epic.setDuration(duration);
        epic.setStartTime(startTime);
    }
}
