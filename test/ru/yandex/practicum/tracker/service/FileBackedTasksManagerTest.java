package ru.yandex.practicum.tracker.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.Subtask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.model.TaskStatus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {

    @Override
    protected TaskManager createTaskManager() {
        try {
            new FileWriter("resources/empty-file.csv").close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FileBackedTasksManager.loadFromFile(new File("resources/empty-file.csv"));
    }

    @Test
    void loadFromFile_shouldCreateManagerFromFile() {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("resources/file-for-testload-from-file.csv"));

        Task task1 = task("Задача #1", "Описание задачи #1", TaskStatus.NEW, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Task task4 = task("Задача #1", "Описание задачи #1", TaskStatus.NEW, 4,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Epic epic2 = epic("Эпик #1", "Описание эпика #1", TaskStatus.NEW, 2, new ArrayList<>());
        epic2.setStartTime(LocalDateTime.of(-999999999, 1, 1, 0, 0));
        epic2.setDuration(Duration.ofMinutes(0));
        Subtask subtask3 = subtask("Подзадача #1 в эпике #1", "Описание подзадачи #1 в эпике #1", TaskStatus.NEW, 3, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));
        Subtask subtask5 = subtask("Подзадача #1 в эпике #1", "Описание подзадачи #1 в эпике #1", TaskStatus.NEW, 5, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2003, 1, 1, 1, 1));

        assertEquals(List.of(task1, task4), fileBackedTasksManager.getTasks(), "Check tasks list");
        assertEquals(List.of(epic2), fileBackedTasksManager.getEpics(), "Check epics list");
        assertEquals(List.of(subtask3, subtask5), fileBackedTasksManager.getSubtasks(), "Check subtasks list");
    }

    @Test
    void save_shouldSaveDataToFile() throws IOException {
        new FileWriter("resources/empty-file-for-test-save.csv").close();
        TaskManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("resources/empty-file-for-test-save.csv"));

        Task task1 = task("Задача #1", "Описание задачи #1", TaskStatus.NEW, 1,
                Duration.ofMinutes(0), LocalDateTime.of(2000, 1, 1, 1, 1));
        Task task4 = task("Задача #1", "Описание задачи #1", TaskStatus.NEW, 4,
                Duration.ofMinutes(0), LocalDateTime.of(2001, 1, 1, 1, 1));
        Epic epic2 = epic("Эпик #1", "Описание эпика #1", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask3 = subtask("Подзадача #1 в эпике #1", "Описание подзадачи #1 в эпике #1", TaskStatus.NEW, 3, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2002, 1, 1, 1, 1));
        Subtask subtask5 = subtask("Подзадача #1 в эпике #1", "Описание подзадачи #1 в эпике #1", TaskStatus.NEW, 5, 2,
                Duration.ofMinutes(0), LocalDateTime.of(2003, 1, 1, 1, 1));

        fileBackedTasksManager.createTask(task1);
        fileBackedTasksManager.createEpic(epic2);
        fileBackedTasksManager.createSubtask(subtask3);
        fileBackedTasksManager.createTask(task4);
        fileBackedTasksManager.createSubtask(subtask5);

        fileBackedTasksManager.getTaskById(4);
        fileBackedTasksManager.getEpicById(2);
        fileBackedTasksManager.getSubtaskById(5);

        String actual = Arrays.toString(Files.readAllBytes(Path.of("resources/saved-data-file.csv")));
        String expected = Arrays.toString(Files.readAllBytes(Path.of("resources/expected-data-for-test-save.csv")));

        assertEquals(expected, actual);
    }

    @Test
    void loadFromFile_shouldThrowManagerSaveException() {
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> FileBackedTasksManager.loadFromFile(new File("resources/nonExistentFile.csv")));
        assertEquals("File not found", exception.getMessage());
    }
}
