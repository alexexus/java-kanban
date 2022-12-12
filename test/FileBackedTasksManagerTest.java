import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.Subtask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.model.TaskStatus;
import ru.yandex.practicum.tracker.service.FileBackedTasksManager;
import ru.yandex.practicum.tracker.service.TaskManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {

    @Override
    protected TaskManager createTaskManager() {
        return new FileBackedTasksManager(new File("resources/file3.csv"));
    }

    @Test
    void loadFromFile_shouldCreateManagerFromFile() {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("resources/file2.csv"));
        Task task1 = task("Задача #1", "Описание задачи #1", TaskStatus.NEW, 1);
        Task task4 = task("Задача #1", "Описание задачи #1", TaskStatus.NEW, 4);
        Epic epic2 = epic("Эпик #1", "Описание эпика #1", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask3 = subtask("Подзадача #1 в эпике #1", "Описание подзадачи #1 в эпике #1", TaskStatus.NEW, 3, 2);
        Subtask subtask5 = subtask("Подзадача #1 в эпике #1", "Описание подзадачи #1 в эпике #1", TaskStatus.NEW, 5, 2);

        assertEquals(List.of(task1, task4), fileBackedTasksManager.getTasks(), "Check tasks list");
        assertEquals(List.of(epic2), fileBackedTasksManager.getEpics(), "Check epics list");
        assertEquals(List.of(subtask3, subtask5), fileBackedTasksManager.getSubtasks(), "Check subtasks list");
    }

    @Test
    void save_shouldSaveDataToFile() throws IOException {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("resources/file3.csv"));

        Task task1 = task("Задача #1", "Описание задачи #1", TaskStatus.NEW, 1);
        Task task4 = task("Задача #1", "Описание задачи #1", TaskStatus.NEW, 4);
        Epic epic2 = epic("Эпик #1", "Описание эпика #1", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask3 = subtask("Подзадача #1 в эпике #1", "Описание подзадачи #1 в эпике #1", TaskStatus.NEW, 3, 2);
        Subtask subtask5 = subtask("Подзадача #1 в эпике #1", "Описание подзадачи #1 в эпике #1", TaskStatus.NEW, 5, 2);

        fileBackedTasksManager.updateTask(task1);
        fileBackedTasksManager.updateEpic(epic2);
        fileBackedTasksManager.updateSubtask(subtask3);
        fileBackedTasksManager.updateTask(task4);
        fileBackedTasksManager.updateSubtask(subtask5);

        fileBackedTasksManager.getTaskById(4);
        fileBackedTasksManager.getEpicById(2);
        fileBackedTasksManager.getSubtaskById(5);

        String content = String.valueOf(Files.readAllLines(Path.of("resources/file.csv"), StandardCharsets.UTF_8));

        BufferedReader bufferedReader = new BufferedReader(new FileReader("resources/file4.csv"));

        assertEquals(bufferedReader.readLine(), content);
    }
}
