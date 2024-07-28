import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void removeAllSubtasks() {
        subtasks.clear();
        for (var epic : epics.values()) {
            epic.setStatus(TaskStatus.NEW);
        }
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }
    public Epic getEpic(int id) {
        return epics.get(id);
    }
    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    /*генерация id происходит при создании объекта в классе Task
    Тем самым решается проблема, если пользозователь создаст несколько менеджеров
    Решение в подсказке к ПР с счетчиком в TaskManager не является потокобезопасным  */
    public void add(Task task) {
        tasks.put(task.getId(), task);
    }

    public void add(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void add(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public void update(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Task not found");
            return;
        }
        tasks.put(task.getId(), task);
    }

    public void update(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            System.out.println("Epic not found");
            return;
        }
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    public void update(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            System.out.println("Subtask not found");
            return;
        }
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicID());
    }

    public void removeTask(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Task not found");
            return;
        }
        tasks.remove(id);
    }

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

    public void removeSubtask(int id) {
        if (!subtasks.containsKey(id)) {
            System.out.println("Subtask not found");
            return;
        }
        var subtask = subtasks.get(id);

        subtasks.remove(id);
        updateEpicStatus(subtask.getEpicID());
    }

    private ArrayList<Subtask> getSubtasks(int epicId) {
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

}
