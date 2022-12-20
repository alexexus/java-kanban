import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.Subtask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.model.TaskStatus;
import ru.yandex.practicum.tracker.service.FileBackedTasksManager;
import ru.yandex.practicum.tracker.service.ManagerSaveException;

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
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {

    @Test
    void loadFromFile_shouldCreateManagerFromFile() {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("resources/file2.csv"));

        Task task1 = task("Задача #1", "Описание задачи #1", TaskStatus.NEW, 1);
        Task task4 = task("Задача #1", "Описание задачи #1", TaskStatus.NEW, 4);
        Epic epic2 = epic("Эпик #1", "Описание эпика #1", TaskStatus.NEW, 2, new ArrayList<>());
        Subtask subtask3 = subtask("Подзадача #1 в эпике #1", "Описание подзадачи #1 в эпике #1", TaskStatus.NEW, 3, 2);
        Subtask subtask5 = subtask("Подзадача #1 в эпике #1", "Описание подзадачи #1 в эпике #1", TaskStatus.NEW, 5, 2);

        fileBackedTasksManager.updateTask(task1);
        fileBackedTasksManager.updateTask(task4);
        fileBackedTasksManager.updateEpic(epic2);
        fileBackedTasksManager.updateSubtask(subtask3);
        fileBackedTasksManager.updateSubtask(subtask5);

        assertEquals(List.of(task1, task4), fileBackedTasksManager.getTasks(), "Check tasks list");
        assertEquals(List.of(epic2), fileBackedTasksManager.getEpics(), "Check epics list");
        assertEquals(List.of(subtask3, subtask5), fileBackedTasksManager.getSubtasks(), "Check subtasks list");
    }

    /*
    Если этот тест запускать отдельно ото всех, то работает нормально, но если запустить вместе с остальными,
    то проверку проходит только с 4 попытки, я дебажил и понял что дело в истории просмотров, она почему-то берется
    непонятно откуда если запускать все тесты сразу, хотя loadFromFile() из 3 файла запускается только здесь.
    На момент прохождения этого теста вместе со всеми, тут в истории уже есть откуда-то 5 задач, как будто взявшиеся из
    обычного менеджера, то ли это из-за наследования классов, то ли я не знаю почему...
     */
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

        String content = String.valueOf(Files.readAllLines(Path.of("resources/file3.csv"), StandardCharsets.UTF_8));

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("resources/file4.csv"))) {
            assertEquals(bufferedReader.readLine(), content);
        }
    }

    @Test
    void loadFromFile_shouldThrowManagerSaveException() {
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> FileBackedTasksManager.loadFromFile(new File("resources/file7.csv")));
        assertEquals("File not found", exception.getMessage());
    }
}
