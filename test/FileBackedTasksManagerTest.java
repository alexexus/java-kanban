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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {

    @Override
    protected TaskManager createTaskManager() {
        return new FileBackedTasksManager(new File("resources/file2.csv"));
    }

    @Test
    void loadFromFile_shouldCreateManagerFromFile() {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("resources/file2.csv"));
        Task task1 = task("Задача #1", "Описание задачи #1", TaskStatus.NEW, 1);
        Task task4 = task("Задача #1", "Описание задачи #1", TaskStatus.NEW, 4);
        Epic epic2 = epic("Эпик #1", "Описание эпика #1", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask3 = subtask("Подзадача #1 в эпике #1", "Описание подзадачи #1 в эпике #1", TaskStatus.NEW, 3, 2);
        Subtask subtask5 = subtask("Подзадача #1 в эпике #1", "Описание подзадачи #1 в эпике #1", TaskStatus.NEW, 5, 2);

        assertEquals(List.of(task1, task4), fileBackedTasksManager.getTasks(), "Проверка списка Tasks");
        assertEquals(List.of(epic2), fileBackedTasksManager.getEpics(), "Проверка списка Epics");
        assertEquals(List.of(subtask3, subtask5), fileBackedTasksManager.getSubtasks(), "Проверка списка Subtasks");
    }

    @Test
    void save_shouldSaveDataToFile() {
        try {
            PrintWriter printWriter = new PrintWriter("resources/file.csv");
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("resources/file.csv"))) {
            assertNull(bufferedReader.readLine(), "Проверяем пустой ли файл");
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("resources/file2.csv"));
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("resources/file.csv"))) {
            assertNotNull(bufferedReader.readLine(), "Проверяем произошла ли запись в файл");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
