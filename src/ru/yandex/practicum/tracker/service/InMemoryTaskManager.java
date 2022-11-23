package ru.yandex.practicum.tracker.service;

import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.Subtask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private int generatorId = 0;

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private void generateId() {
        ++generatorId;
    }

    public int getGeneratorId() {
        generateId();
        return generatorId;
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
        for (int taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (int epicId : epics.keySet()) {
            historyManager.remove(epicId);
        }
        for (int subtaskId : subtasks.keySet()) {
            historyManager.remove(subtaskId);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (int subtaskId : subtasks.keySet()) {
            historyManager.remove(subtaskId);
        }
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
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtaskId(subtask.getId());

        updateEpicStatus(epics.get(subtask.getEpicId()));
    }

    @Override
    public void updateTask(Task task) {
        Task taskToUpdate = tasks.get(task.getId());
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setStatus(task.getStatus());
        taskToUpdate.setName(task.getName());
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic epicToUpdate = epics.get(epic.getId());
        epicToUpdate.setDescription(epic.getDescription());
        epicToUpdate.setName(epic.getName());
        epicToUpdate.setSubTaskIds(epic.getSubTaskIds());

        updateEpicStatus(epicToUpdate);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask subtaskToUpdate = subtasks.get(subtask.getId());
        subtaskToUpdate.setDescription(subtask.getDescription());
        subtaskToUpdate.setStatus(subtask.getStatus());
        subtaskToUpdate.setName(subtask.getName());
        subtaskToUpdate.setEpicId(subtask.getEpicId());

        updateEpicStatus(epics.get(subtask.getEpicId()));
    }

    @Override
    public void removeTaskById(int taskId) {
        historyManager.remove(taskId);
        tasks.remove(taskId);
    }

    @Override
    public void removeEpicById(int epicId) {
//        for (Subtask subtask : getSubtasksByEpicId(epicId)) {  <--- Если оставить так, то приложение падает с нулпоинтром
//            historyManager.remove(subtask.getId());            в строчке (InMemoryHistoryManager.java:49)
//        }
//        historyManager.remove(epicId);

        epics.remove(epicId);
        for (Subtask subtask : getSubtasksByEpicId(epicId)) {
            subtasks.remove(subtask.getId());
        }

        for (Subtask subtask : getSubtasksByEpicId(epicId)) {   // <--- А если так, то не при удалении эпика не удаляет сабтаски
            historyManager.remove(subtask.getId());
        }
        historyManager.remove(epicId);
    }

    @Override
    public void removeSubtaskById(Integer subtaskId) {
        Epic epic = epics.get(getSubtaskById(subtaskId).getEpicId());
        epic.removeSubtaskId(subtaskId);
        updateEpicStatus(epic);
        subtasks.remove(subtaskId);
        historyManager.remove(subtaskId);
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Subtask subtask : getSubtasks()) {
            if (subtask.getEpicId() == epicId) {
                subtasks.add(subtask);
            }
        }
        return subtasks;
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getSubTaskIds().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        boolean allSubtasksStatusIsNew = false;
        boolean allSubtasksStatusIsDone = false;
        for (Integer idSubtask : epic.getSubTaskIds()) {
            if (!(getSubtasks().isEmpty())) {
                allSubtasksStatusIsNew = subtasks.get(idSubtask).getStatus().equals(TaskStatus.NEW);
                allSubtasksStatusIsDone = subtasks.get(idSubtask).getStatus().equals(TaskStatus.DONE);
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
