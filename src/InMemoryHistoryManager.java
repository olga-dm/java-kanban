import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();
    private static final int MAX_HISTORY_STORAGE = 10;

    @Override
    public void addToHistory(Task task) {
        if (history.size() == MAX_HISTORY_STORAGE) {
            history.removeFirst();
        }
        history.addLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
