package ru.yandex.practicum.tracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.Subtask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
    }

    protected TaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Test
    void getTasks_shouldReturnEmptyTasks() {
        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void getEpics_shouldReturnEmptyEpics() {
        assertTrue(taskManager.getEpics().isEmpty());
    }

    @Test
    void getSubtasks_shouldReturnEmptySubtasks() {
        assertTrue(taskManager.getSubtasks().isEmpty());
    }

    @Test
    void deleteAllTasks_shouldDeleteAllTasks() {
        Task task1 = task("name1", "description1", TaskStatus.NEW, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Task task2 = task("name2", "description2", TaskStatus.IN_PROGRESS, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Task task3 = task("name3", "description3", TaskStatus.DONE, 3,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        taskManager.deleteAllTasks();

        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void deleteAllEpics_shouldDeleteAllEpicsAndSubtasks() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.IN_PROGRESS, 2, new ArrayList<>());
        Epic epic3 = epic("name3", "description3", TaskStatus.DONE, 3, new ArrayList<>());

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);

        taskManager.deleteAllEpics();

        assertTrue(taskManager.getEpics().isEmpty());
        assertTrue(taskManager.getSubtasks().isEmpty());
    }

    @Test
    void deleteAllSubtasks_shouldDeleteAllSubtasks() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask1 = subtask("name3", "description3", TaskStatus.NEW, 3, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Subtask subtask2 = subtask("name4", "description4", TaskStatus.IN_PROGRESS, 4, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Subtask subtask3 = subtask("name5", "description5", TaskStatus.DONE, 5, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        taskManager.deleteAllSubtasks();

        assertTrue(taskManager.getSubtasks().isEmpty());
        for (Epic epic : taskManager.getEpics()) {
            assertTrue(epic.getSubtaskIds().isEmpty());
        }
    }

    @Test
    void getTaskById_shouldReturnTaskById() {
        Task task1 = task("name1", "description1", TaskStatus.NEW, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        taskManager.createTask(task1);

        assertEquals(task1, taskManager.getTaskById(1));
    }

    @Test
    void getEpicById_shouldReturnEpicById() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        taskManager.createEpic(epic1);

        assertEquals(epic1, taskManager.getEpicById(1));
    }

    @Test
    void getSubtaskById_shouldReturnSubtaskById() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        taskManager.createEpic(epic1);
        Subtask subtask1 = subtask("name2", "description2", TaskStatus.NEW, 2, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        taskManager.createSubtask(subtask1);

        assertEquals(subtask1, taskManager.getSubtaskById(2));
    }

    @Test
    void createTask_shouldCreateTask() {
        Task task1 = task("name1", "description1", TaskStatus.NEW, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Task task2 = task("name2", "description2", TaskStatus.IN_PROGRESS, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Task task3 = task("name3", "description3", TaskStatus.DONE, 3,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        assertEquals(List.of(task1, task2, task3), taskManager.getTasks());
    }

    @Test
    void createEpic_shouldCreateEpic() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.IN_PROGRESS, 2, new ArrayList<>());
        Epic epic3 = epic("name3", "description3", TaskStatus.DONE, 3, new ArrayList<>());
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);

        assertEquals(List.of(epic1, epic2, epic3), taskManager.getEpics());
    }

    @Test
    void createSubtask_shouldCreateSubtask() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask1 = subtask("name3", "description3", TaskStatus.NEW, 3, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Subtask subtask2 = subtask("name4", "description4", TaskStatus.IN_PROGRESS, 4, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Subtask subtask3 = subtask("name5", "description5", TaskStatus.DONE, 5, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        assertEquals(List.of(subtask1, subtask2, subtask3), taskManager.getSubtasks());
        assertEquals(List.of(subtask1.getId(), subtask2.getId()), epic1.getSubtaskIds());
        assertEquals(List.of(subtask3.getId()), epic2.getSubtaskIds());
    }

    @Test
    void updateTask_shouldUpdateTask() {
        Task task1 = task("name1", "description1", TaskStatus.NEW, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Task task2 = task("name2", "description2", TaskStatus.IN_PROGRESS, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Task task3 = task("name1(update)", "description1(update)", TaskStatus.IN_PROGRESS, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));
        Task task4 = task("name2(update)", "description2(update)", TaskStatus.NEW, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2003, 1, 1, 1, 1));

        taskManager.updateTask(task3);
        taskManager.updateTask(task4);

        assertEquals(List.of(task3, task4), taskManager.getTasks());
    }

    @Test
    void updateEpic_shouldUpdateEpic() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.IN_PROGRESS, 2, new ArrayList<>());

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        Epic epic3 = epic("name1(update)", "description1(update)", TaskStatus.NEW, 1, epic1.getSubtaskIds());
        Epic epic4 = epic("name2(update)", "description2(update)", TaskStatus.NEW, 2, epic2.getSubtaskIds());

        taskManager.updateEpic(epic3);
        taskManager.updateEpic(epic4);

        assertEquals(List.of(epic3, epic4), taskManager.getEpics());
    }

    @Test
    void updateSubtask_shouldUpdateSubtask() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask1 = subtask("name3", "description3", TaskStatus.NEW, 3, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Subtask subtask2 = subtask("name4", "description4", TaskStatus.IN_PROGRESS, 4, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Subtask subtask3 = subtask("name3(update)", "description3(update)", TaskStatus.IN_PROGRESS, 3, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));
        Subtask subtask4 = subtask("name4(update)", "description4(update)", TaskStatus.DONE, 4, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2003, 1, 1, 1, 1));

        taskManager.updateSubtask(subtask3);
        taskManager.updateSubtask(subtask4);

        assertEquals(List.of(subtask3, subtask4), taskManager.getSubtasks());
    }

    @Test
    void removeTaskById_shouldRemoveTaskById() {
        Task task1 = task("name1", "description1", TaskStatus.NEW, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Task task2 = task("name2", "description2", TaskStatus.IN_PROGRESS, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.removeTaskById(1);

        assertEquals(List.of(task2), taskManager.getTasks());
    }

    @Test
    void removeEpicById_shouldRemoveEpicById() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.IN_PROGRESS, 2, new ArrayList<>());
        Subtask subtask1 = subtask("name3", "description3", TaskStatus.NEW, 3, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);

        taskManager.removeEpicById(1);

        assertEquals(List.of(epic2), taskManager.getEpics());
        assertTrue(epic1.getSubtaskIds().isEmpty());
    }

    @Test
    void removeSubtaskById_shouldRemoveSubtaskById() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask3 = subtask("name3", "description3", TaskStatus.NEW, 3, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Subtask subtask4 = subtask("name4", "description4", TaskStatus.IN_PROGRESS, 4, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Subtask subtask5 = subtask("name5", "description5", TaskStatus.DONE, 5, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));
        Subtask subtask6 = subtask("name6", "description6", TaskStatus.NEW, 6, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2003, 1, 1, 1, 1));
        Subtask subtask7 = subtask("name7", "description7", TaskStatus.IN_PROGRESS, 7, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2004, 1, 1, 1, 1));

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask3);
        taskManager.createSubtask(subtask4);
        taskManager.createSubtask(subtask5);
        taskManager.createSubtask(subtask6);
        taskManager.createSubtask(subtask7);

        taskManager.removeSubtaskById(3);
        taskManager.removeSubtaskById(5);
        taskManager.removeSubtaskById(6);

        assertEquals(List.of(subtask4, subtask7), taskManager.getSubtasks());
        assertEquals(List.of(subtask7.getId()), taskManager.getEpicById(1).getSubtaskIds());
        assertEquals(List.of(subtask4.getId()), taskManager.getEpicById(2).getSubtaskIds());
    }

    @Test
    void getSubtasksByEpicId_shouldGetSubtasksByEpicId() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask3 = subtask("name3", "description3", TaskStatus.NEW, 3, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Subtask subtask4 = subtask("name4", "description4", TaskStatus.IN_PROGRESS, 4, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Subtask subtask5 = subtask("name5", "description5", TaskStatus.DONE, 5, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));
        Subtask subtask6 = subtask("name6", "description6", TaskStatus.NEW, 6, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2003, 1, 1, 1, 1));
        Subtask subtask7 = subtask("name7", "description7", TaskStatus.IN_PROGRESS, 7, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2004, 1, 1, 1, 1));

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask3);
        taskManager.createSubtask(subtask4);
        taskManager.createSubtask(subtask5);
        taskManager.createSubtask(subtask6);
        taskManager.createSubtask(subtask7);

        assertEquals(List.of(subtask3, subtask5, subtask7), taskManager.getSubtasksByEpicId(1));
    }

    @Test
    void updateEpicStatus_shouldUpdateEpicStatus() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Subtask subtask2 = subtask("name2", "description2", TaskStatus.NEW, 2, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Subtask subtask3 = subtask("name3", "description3", TaskStatus.NEW, 3, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Subtask subtask4 = subtask("name4", "description4", TaskStatus.IN_PROGRESS, 4, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));
        Subtask subtask5 = subtask("name5", "description5", TaskStatus.IN_PROGRESS, 5, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2003, 1, 1, 1, 1));
        Subtask subtask6 = subtask("name6", "description6", TaskStatus.DONE, 6, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2004, 1, 1, 1, 1));
        Subtask subtask7 = subtask("name7", "description7", TaskStatus.DONE, 7, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2005, 1, 1, 1, 1));

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        assertEquals(TaskStatus.NEW, epic1.getStatus(),
                "Epic status should be NEW, " +
                        "because it has 2 subtasks with status NEW");

        taskManager.createSubtask(subtask4);
        taskManager.createSubtask(subtask5);

        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus(),
                "Epic status should be IN_PROGRESS, " +
                        "because it has 2 subtasks with status NEW" +
                        "and 2 subtasks with status IN_PROGRESS");

        taskManager.removeSubtaskById(4);
        taskManager.removeSubtaskById(5);

        assertEquals(TaskStatus.NEW, epic1.getStatus(),
                "Epic status should be NEW, " +
                        "because it has 2 subtasks with NEW status, " +
                        "and 2 subtasks with status IN_PROGRESS have been deleted");

        taskManager.createSubtask(subtask6);
        taskManager.createSubtask(subtask7);

        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus(),
                "Epic status should be IN_PROGRESS, " +
                        "because it has 2 subtasks with NEW status, " +
                        "and 2 subtasks with status DONE");

        taskManager.removeSubtaskById(2);
        taskManager.removeSubtaskById(3);

        assertEquals(TaskStatus.DONE, epic1.getStatus(),
                "Epic status should be DONE, " +
                        "because it has 2 subtasks with DONE status, " +
                        "and 2 subtasks with status NEW have been deleted");
    }

    @Test
    void setEndTimeForEpic_shouldSetEndTimeForEpic() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Subtask subtask2 = subtask("name2", "description2", TaskStatus.NEW, 2, 1,
                Duration.ofMinutes(20), LocalDateTime.of(2000, 1, 4, 0, 0));
        Subtask subtask3 = subtask("name3", "description3", TaskStatus.NEW, 3, 1,
                Duration.ofMinutes(30), LocalDateTime.of(2000, 1, 2, 0, 0));

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        assertEquals(LocalDateTime.of(2000, 1, 4, 0, 50), epic1.getEndTime());
    }

    @Test
    void getEndTime_shouldReturnEndTimeOfTask() {
        Task task1 = task("name1", "description1", TaskStatus.NEW, 1,
                Duration.ofMinutes(20), LocalDateTime.of(2001, 1, 1, 0, 0));
        assertEquals(LocalDateTime.of(2001, 1, 1, 0, 20), task1.getEndTime());
    }

    @Test
    void getPrioritizedTasks_shouldReturnSortedTasks() {
        Task task1 = task("name1", "description1", TaskStatus.NEW, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Task task2 = task("name2", "description2", TaskStatus.NEW, 2,
                Duration.ofMinutes(0), null);
        Subtask subtask3 = subtask("name3", "description3", TaskStatus.NEW, 3, 5,
                Duration.ofMinutes(0), LocalDateTime.of(2003, 1, 1, 1, 1));
        Task task4 = task("name4", "description4", TaskStatus.NEW, 4,
                Duration.ofMinutes(0), LocalDateTime.of(2004, 1, 1, 1, 1));
        Epic epic5 = epic("name5", "description5", TaskStatus.NEW, 5, new ArrayList<>());

        taskManager.updateTask(task1);
        taskManager.updateTask(task2);
        taskManager.updateEpic(epic5);
        taskManager.updateSubtask(subtask3);
        taskManager.updateTask(task4);

        assertEquals(List.of(task1, subtask3, task4, task2), new ArrayList<>(taskManager.getPrioritizedTasks()));
    }

    @Test
    void checkIntersection_shouldThrowIntersectionException() {
        final IntersectionException exception = assertThrows(
                IntersectionException.class,
                () -> {
                    Task task1 = task("name1", "description1", TaskStatus.NEW, 1,
                            Duration.ofMinutes(20), LocalDateTime.of(2000, 1, 1, 1, 1));
                    Task task2 = task("name2", "description2", TaskStatus.NEW, 2,
                            Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 20));
                    taskManager.createTask(task1);
                    taskManager.createTask(task2);
                });
        assertEquals("Task overlaps with another tasks", exception.getMessage());
    }

    protected static Task task(String name, String description, TaskStatus status, int id, Duration duration, LocalDateTime localDateTime) {
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setStatus(status);
        task.setId(id);
        task.setStartTime(localDateTime);
        task.setDuration(duration);
        return task;
    }

    protected static Epic epic(String name, String description, TaskStatus status, int id, List<Integer> subtaskIds) {
        Epic epic = new Epic();
        epic.setName(name);
        epic.setDescription(description);
        epic.setStatus(status);
        epic.setId(id);
        return epic;
    }

    protected static Subtask subtask(String name, String description, TaskStatus status, int id, int epicId, Duration duration, LocalDateTime localDateTime) {
        Subtask subtask = new Subtask();
        subtask.setName(name);
        subtask.setDescription(description);
        subtask.setStatus(status);
        subtask.setId(id);
        subtask.setEpicId(epicId);
        subtask.setDuration(duration);
        subtask.setStartTime(localDateTime);
        return subtask;
    }
}
