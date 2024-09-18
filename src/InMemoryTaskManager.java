import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryTaskManager implements TaskManager {
    private static final AtomicInteger atomicID = new AtomicInteger();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public List<Task> historyList() {
        return historyManager.getHistory();
    }

    @Override
    public Task createTask(String name, String description) {
        return new Task(InMemoryTaskManager.getId(), name, description);
    }

    @Override
    public Epic createEpic(String name, String description) {
        return new Epic(InMemoryTaskManager.getId(), name, description);
    }

    @Override
    public Subtask createSubtask(String name, String description, int epicId) {
        return new Subtask(InMemoryTaskManager.getId(), name, description, epicId);
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.clear();
        for (var epic : epics.values()) {
            epic.setStatus(TaskStatus.NEW);
        }
    }

    @Override
    public Task getTask(int id) {
        var task = tasks.get(id);
        historyManager.addToHistory(task);
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        var epic = epics.get(id);
        historyManager.addToHistory(epic);
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        var subtask = subtasks.get(id);
        historyManager.addToHistory(subtask);
        return subtask;
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        var result = new ArrayList<Subtask>();
        for (var subtask : subtasks.values()) {
            if (subtask.getEpicID() == epicId) {
                result.add(subtask);
            }
        }
        return result;
    }

    @Override
    public void add(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void add(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void add(Subtask subtask) throws Exception {
        if (!epics.containsKey(subtask.getEpicID())) {
            throw new Exception("Epic not found");
        }
        subtasks.put(subtask.getId(), subtask);
        var epicSubtasks = epics.get(subtask.getEpicID()).getSubtasks();
        if (!epicSubtasks.contains(subtask.getId())) {
            epicSubtasks.add(subtask.getId());
        }
    }

    @Override
    public void update(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Task not found");
            return;
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void update(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            System.out.println("Epic not found");
            return;
        }
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    @Override
    public void update(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            System.out.println("Subtask not found");
            return;
        }
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicID());
    }

    @Override
    public void removeTask(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Task not found");
            return;
        }
        tasks.remove(id);
    }

    @Override
    public void removeEpic(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Epic not found");
            return;
        }
        var epic = epics.get(id);
        var epicSubtasks = epic.getSubtasks();
        for (var subtask : epicSubtasks) {
            subtasks.remove(subtask);
        }
        epics.remove(id);
    }

    @Override
    public void removeSubtask(int id) {
        if (!subtasks.containsKey(id)) {
            System.out.println("Subtask not found");
            return;
        }
        var subtask = subtasks.get(id);

        subtasks.remove(id);
        updateEpicStatus(subtask.getEpicID());
    }

    private List<Subtask> getSubtasks(int epicId) {
        var result = new ArrayList<Subtask>();
        var epic = epics.get(epicId);
        for (int id : epic.getSubtasks()) {
            if (subtasks.containsKey(id)) {
                result.add(subtasks.get(id));
            }
        }
        return result;
    }

    private void updateEpicStatus(int id) {
        var epic = epics.get(id);
        if (epic.getSubtasks().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        var epicSubtasks = getSubtasks(id);
        var isNew = epicSubtasks.stream().allMatch(st -> st.getStatus() == TaskStatus.NEW);
        if (isNew) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        var isDone = epicSubtasks.stream().allMatch(st -> st.getStatus() == TaskStatus.DONE);
        if (isDone) {
            epic.setStatus(TaskStatus.DONE);
            return;
        }
        epic.setStatus(TaskStatus.IN_PROGRESS);
    }

    private static int getId() {
        return atomicID.incrementAndGet();
    }
}
