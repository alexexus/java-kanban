import ru.yandex.practicum.tracker.service.InMemoryHistoryManager;
import ru.yandex.practicum.tracker.service.InMemoryTaskManager;

public class Main {

    public static void main(String[] args) {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    }
}
