import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryTaskManager implements TaskManager {
    private static final AtomicInteger atomicID = new AtomicInteger();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private final TreeSet<Task> prioritizedTasks = new TreeSet<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public List<Task> historyList() {
        return historyManager.getHistory();
    }

    @Override
    public Task createTask(Task task) {
        task.setId(getId());
        tasks.put(task.getId(), task);
        addPrioritizedTask(task); // Добавляем в отсортированный набор
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(getId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(getId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicID());
        if (epic != null) {
            epic.getSubtasks().add(subtask.getId());
            updateEpicStatus(subtask.getEpicID());
            calculateDurationAndStartEndTime(epic);
        }
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        return subtask;
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
        tasks.forEach((a, task) -> removePrioritizedTask(task));
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        subtasks.forEach((a, task) -> removePrioritizedTask(task));
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.forEach((a, task) -> removePrioritizedTask(task));
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
        Epic epic = epics.get(epicId);
        if (epic != null) {
            return epic.getSubtasks().stream().map(subtasks::get).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public void add(Task task) {
        tasks.put(task.getId(), task);
        addPrioritizedTask(task);
    }

    @Override
    public void add(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    @Override
    public void add(Subtask subtask) throws Exception {
        if (!epics.containsKey(subtask.getEpicID())) {
            throw new Exception("Epic not found");
        }
        subtasks.put(subtask.getId(), subtask);
        var epicSubtasks = epics.get(subtask.getEpicID()).getSubtasks();
        if (!epicSubtasks.contains(subtask)) {
            epicSubtasks.add(subtask.getId());
        }
        updateEpicStatus(subtask.getEpicID());
        addPrioritizedTask(subtask);
    }

    @Override
    public void update(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Task not found");
            return;
        }
        tasks.put(task.getId(), task);

        removePrioritizedTask(task);
        addPrioritizedTask(task);
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

        removePrioritizedTask(subtask);
        addPrioritizedTask(subtask);
    }

    @Override
    public void removeTask(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Task not found");
            return;
        }
        var task = tasks.remove(id);
        removePrioritizedTask(task);
    }

    @Override
    public void removeEpic(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Epic not found");
            return;
        }
        var epic = epics.get(id);
        var epicSubtasks = epic.getSubtasks();
        for (var subtaskId : epicSubtasks) {
            var subtask = getSubtask(subtaskId);
            removePrioritizedTask(subtask);
            subtasks.remove(subtaskId, subtask);
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
        removePrioritizedTask(subtask);
        subtasks.remove(id);
        updateEpicStatus(subtask.getEpicID());
    }

    private void updateEpicStatus(int id) {
        var epic = epics.get(id);
        if (epic.getSubtasks().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            epic.setDuration(0);
            return;
        }

        var epicSubtasks = epic.getSubtasks();
        calculateDurationAndStartEndTime(epic);

        var isNew = epicSubtasks.stream().allMatch(st -> subtasks.get(st).getStatus() == TaskStatus.NEW);
        if (isNew) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        var isDone = epicSubtasks.stream().allMatch(st -> subtasks.get(st).getStatus() == TaskStatus.DONE);
        if (isDone) {
            epic.setStatus(TaskStatus.DONE);
            return;
        }
        epic.setStatus(TaskStatus.IN_PROGRESS);
    }

    private static int getId() {
        return atomicID.incrementAndGet();
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private void removePrioritizedTask(Task task) {
        if (task.getStartTime() != null)
            prioritizedTasks.remove(task);
    }

    private void addPrioritizedTask(Task task) {
        if (task.getStartTime() != null && prioritizedTasks.stream().noneMatch(task::isIntersect))
            prioritizedTasks.add(task);
    }

    private Duration calculateDurationAndStartEndTime(Epic epic) {
        epic.duration = Duration.ZERO;
        epic.startTime = LocalDateTime.MAX;
        epic.endTime = LocalDateTime.MIN;
        var epicSubtasks = epic.getSubtasks().stream().map(subtasks::get).toList();
        epic.duration = epicSubtasks.stream()
                .map(Task::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
        epic.startTime = epicSubtasks.stream()
                .map(Task::getStartTime)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(null);
        epic.endTime = epicSubtasks.stream()
                .map(Task::getEndTime)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(null);
        return epic.duration;
    }
}
