import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    void add(Task task);

    void add(Epic epic) throws Exception;

    void add(Subtask subtask) throws Exception;

    void update(Task task);

    void update(Epic epic);

    void update(Subtask subtask);

    void removeTask(int id);

    void removeEpic(int id);

    void removeSubtask(int id);

    List<Subtask> getEpicSubtasks(int epicId);

    Task createTask(Task task);

    Epic createEpic(Epic epic);

    Subtask createSubtask(Subtask subtask);

    TreeSet<Task> getPrioritizedTasks();
}
