import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.service.HistoryManager;
import ru.yandex.practicum.tracker.service.InMemoryHistoryManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryHistoryManagerTest {
    private final HistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    void getHistory_shouldReturnEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void add_shouldSaveTaskToHistory() {
        Task task1 = task(1);
        Task task2 = task(2);
        Task task3 = task(3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Integer> expected = List.of(task1.getId(), task2.getId(), task3.getId());
        List<Integer> actual = historyManager.getHistory();

        assertEquals(expected, actual);
    }

    @Test
    void add_shouldNotKeepDuplicates() {
        Task task1 = task(1);
        Task task2 = task(2);
        Task task3 = task(3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task1);
        historyManager.add(task2);

        List<Integer> expected = List.of(task3.getId(), task1.getId(), task2.getId());
        List<Integer> actual = historyManager.getHistory();

        assertEquals(expected, actual);
    }

    @Test
    void remove_shouldRemoveTaskFromHistory() {
        Task task1 = task(1);
        Task task2 = task(2);
        Task task3 = task(3);
        Task task4 = task(4);
        Task task5 = task(5);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        historyManager.remove(task1.getId());
        historyManager.remove(task3.getId());
        historyManager.remove(task5.getId());

        List<Integer> expected = List.of(task2.getId(), task4.getId());
        List<Integer> actual = historyManager.getHistory();

        assertEquals(expected, actual);
    }

    @Test
    void add_shouldMoveTaskToTheEnd_ifTaskAlreadyExistsInHistory() {
        Task task1 = task(1);
        Task task2 = task(2);
        Task task3 = task(3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task1);

        List<Integer> expected = List.of(task2.getId(), task3.getId(), task1.getId());
        List<Integer> actual = historyManager.getHistory();

        assertEquals(expected, actual);
    }

    private static Task task(int id) {
        Task task = new Task();
        task.setId(id);
        return task;
    }
}