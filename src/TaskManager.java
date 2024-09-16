import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubtasks();

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

    Task createTask(String name, String description);

    Epic createEpic(String name, String description);

    Subtask createSubtask(String name, String description, int epicId);
}
