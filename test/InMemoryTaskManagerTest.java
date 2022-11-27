import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.Subtask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.model.TaskStatus;
import ru.yandex.practicum.tracker.service.InMemoryTaskManager;
import ru.yandex.practicum.tracker.service.TaskManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryTaskManagerTest {

    private final TaskManager taskManager = new InMemoryTaskManager();

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
        Task task1 = task("name1", "description1", TaskStatus.NEW, 1);
        Task task2 = task("name2", "description2", TaskStatus.IN_PROGRESS, 2);
        Task task3 = task("name3", "description3", TaskStatus.DONE, 3);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        taskManager.deleteAllTasks();

        List<Task> expected = List.of();
        List<Task> actual = taskManager.getTasks();

        assertEquals(expected, actual);
    }

    @Test
    void deleteAllEpics_shouldDeleteAllEpics() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.IN_PROGRESS, 2, new ArrayList<>());
        Epic epic3 = epic("name3", "description3", TaskStatus.DONE, 3, new ArrayList<>());

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);

        taskManager.deleteAllEpics();

        List<Epic> expected = List.of();
        List<Epic> actual = taskManager.getEpics();

        assertEquals(expected, actual);
    }

    @Test
    void deleteAllSubtasks_shouldDeleteAllSubtasks() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask1 = subtask("name3", "description3", TaskStatus.NEW, 3, 1);
        Subtask subtask2 = subtask("name4", "description4", TaskStatus.IN_PROGRESS, 4, 1);
        Subtask subtask3 = subtask("name5", "description5", TaskStatus.DONE, 5, 2);

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        taskManager.deleteAllSubtasks();

        List<Subtask> expected = List.of();
        List<Subtask> actual = taskManager.getSubtasks();

        assertEquals(expected, actual);
    }

    @Test
    void getTaskById_shouldGetTaskById() {
        Task task1 = task("name1", "description1", TaskStatus.NEW, 1);
        taskManager.createTask(task1);

        Task actual = taskManager.getTaskById(1);

        assertEquals(task1, actual);
    }

    @Test
    void getEpicById_shouldGetEpicById() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        taskManager.createEpic(epic1);

        Epic actual = taskManager.getEpicById(1);

        assertEquals(epic1, actual);
    }

    @Test
    void getSubtaskById_shouldGetSubtaskById() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        taskManager.createEpic(epic1);
        Subtask subtask1 = subtask("name2", "description2", TaskStatus.NEW, 2, 1);
        taskManager.createSubtask(subtask1);

        Subtask actual = taskManager.getSubtaskById(2);

        assertEquals(subtask1, actual);
    }

    @Test
    void createTask_shouldCreateTask() {
        Task task1 = task("name1", "description1", TaskStatus.NEW, 1);
        Task task2 = task("name2", "description2", TaskStatus.IN_PROGRESS, 2);
        Task task3 = task("name3", "description3", TaskStatus.DONE, 3);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        List<Task> expected = List.of(task1, task2, task3);
        List<Task> actual = taskManager.getTasks();

        assertEquals(expected, actual);
    }

    @Test
    void createEpic_shouldCreateEpic() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.IN_PROGRESS, 2, new ArrayList<>());
        Epic epic3 = epic("name3", "description3", TaskStatus.DONE, 3, new ArrayList<>());
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);

        List<Epic> expected = List.of(epic1, epic2, epic3);
        List<Epic> actual = taskManager.getEpics();

        assertEquals(expected, actual);
    }

    @Test
    void createSubtask_shouldCreateSubtask() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask1 = subtask("name3", "description3", TaskStatus.NEW, 3, 1);
        Subtask subtask2 = subtask("name4", "description4", TaskStatus.IN_PROGRESS, 4, 1);
        Subtask subtask3 = subtask("name5", "description5", TaskStatus.DONE, 5, 2);

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        List<Subtask> expected = List.of(subtask1, subtask2, subtask3);
        List<Subtask> actual = taskManager.getSubtasks();

        assertEquals(expected, actual);
    }

    @Test
    void updateTask_shouldUpdateTask() {
        Task task1 = task("name1", "description1", TaskStatus.NEW, 1);
        Task task2 = task("name2", "description2", TaskStatus.IN_PROGRESS, 2);

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Task task3 = task("name1(update)", "description1(update)", TaskStatus.IN_PROGRESS, 1);
        Task task4 = task("name2(update)", "description2(update)", TaskStatus.NEW, 2);

        taskManager.updateTask(task3);
        taskManager.updateTask(task4);

        List<Task> expected = List.of(task3, task4);
        List<Task> actual = taskManager.getTasks();

        assertEquals(expected, actual);
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

        List<Epic> expected = List.of(epic3, epic4);
        List<Epic> actual = taskManager.getEpics();

        assertEquals(expected, actual);
    }

    @Test
    void updateSubtask_shouldUpdateSubtask() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask1 = subtask("name3", "description3", TaskStatus.NEW, 3, 1);
        Subtask subtask2 = subtask("name4", "description4", TaskStatus.IN_PROGRESS, 4, 2);

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Subtask subtask3 = subtask("name3(update)", "description3(update)", TaskStatus.IN_PROGRESS, 3, 2);
        Subtask subtask4 = subtask("name4(update)", "description4(update)", TaskStatus.DONE, 4, 1);

        taskManager.updateSubtask(subtask3);
        taskManager.updateSubtask(subtask4);

        List<Subtask> expected = List.of(subtask3, subtask4);
        List<Subtask> actual = taskManager.getSubtasks();

        assertEquals(expected, actual);
    }

    @Test
    void removeTaskById_shouldRemoveTaskById() {
        Task task1 = task("name1", "description1", TaskStatus.NEW, 1);
        Task task2 = task("name2", "description2", TaskStatus.IN_PROGRESS, 2);
        Task task3 = task("name3", "description3", TaskStatus.DONE, 3);
        Task task4 = task("name4", "description4", TaskStatus.NEW, 4);
        Task task5 = task("name5", "description5", TaskStatus.IN_PROGRESS, 5);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);

        taskManager.removeTaskById(1);
        taskManager.removeTaskById(3);
        taskManager.removeTaskById(5);

        List<Task> expected = List.of(task2, task4);
        List<Task> actual = taskManager.getTasks();

        assertEquals(expected, actual);
    }

    @Test
    void removeEpicById_shouldRemoveEpicById() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.IN_PROGRESS, 2, new ArrayList<>());
        Epic epic3 = epic("name3", "description3", TaskStatus.DONE, 3, new ArrayList<>());
        Epic epic4 = epic("name4", "description4", TaskStatus.NEW, 4, new ArrayList<>());
        Epic epic5 = epic("name5", "description5", TaskStatus.IN_PROGRESS, 5, new ArrayList<>());

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);
        taskManager.createEpic(epic4);
        taskManager.createEpic(epic5);

        taskManager.removeEpicById(1);
        taskManager.removeEpicById(3);
        taskManager.removeEpicById(5);

        List<Epic> expected = List.of(epic2, epic4);
        List<Epic> actual = taskManager.getEpics();

        assertEquals(expected, actual);
    }

    @Test
    void removeSubtaskById_shouldRemoveSubtaskById() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask3 = subtask("name3", "description3", TaskStatus.NEW, 3, 1);
        Subtask subtask4 = subtask("name4", "description4", TaskStatus.IN_PROGRESS, 4, 2);
        Subtask subtask5 = subtask("name5", "description5", TaskStatus.DONE, 5, 1);
        Subtask subtask6 = subtask("name6", "description6", TaskStatus.NEW, 6, 2);
        Subtask subtask7 = subtask("name7", "description7", TaskStatus.IN_PROGRESS, 7, 1);

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

        List<Subtask> expected = List.of(subtask4, subtask7);
        List<Subtask> actual = taskManager.getSubtasks();

        assertEquals(expected, actual);
    }

    @Test
    void getSubtasksByEpicId_shouldGetSubtasksByEpicId() {
        Epic epic1 = epic("name1", "description1", TaskStatus.NEW, 1, new ArrayList<>());
        Epic epic2 = epic("name2", "description2", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask3 = subtask("name3", "description3", TaskStatus.NEW, 3, 1);
        Subtask subtask4 = subtask("name4", "description4", TaskStatus.IN_PROGRESS, 4, 2);
        Subtask subtask5 = subtask("name5", "description5", TaskStatus.DONE, 5, 1);
        Subtask subtask6 = subtask("name6", "description6", TaskStatus.NEW, 6, 2);
        Subtask subtask7 = subtask("name7", "description7", TaskStatus.IN_PROGRESS, 7, 1);

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask3);
        taskManager.createSubtask(subtask4);
        taskManager.createSubtask(subtask5);
        taskManager.createSubtask(subtask6);
        taskManager.createSubtask(subtask7);

        List<Subtask> expected = List.of(subtask3, subtask5, subtask7);
        List<Subtask> actual = taskManager.getSubtasksByEpicId(1);

        assertEquals(expected, actual);
    }

    private static Task task(String name, String description, TaskStatus status, int id) {
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setStatus(status);
        task.setId(id);
        return task;
    }

    private static Epic epic(String name, String description, TaskStatus status, int id, List<Integer> subtaskIds) {
        Epic epic = new Epic();
        epic.setName(name);
        epic.setDescription(description);
        epic.setStatus(status);
        epic.setId(id);
        return epic;
    }

    private static Subtask subtask(String name, String description, TaskStatus status, int id, int epicId) {
        Subtask subtask = new Subtask();
        subtask.setName(name);
        subtask.setDescription(description);
        subtask.setStatus(status);
        subtask.setId(id);
        subtask.setEpicId(epicId);
        return subtask;
    }
}
