import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> tasks = new HashMap<>();

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task) {

        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Такого идентификатора нет");
            return;
        }
        if (task.getClass() == Subtask.class) {
            var epic = tasks.get(((Subtask) task).getEpicID());
            var subtasks = getSubtasks(epic.getId());
            var isNew = subtasks.stream().allMatch(st -> st.getStatus() == TaskStatus.NEW);
            var isDone = subtasks.stream().allMatch(st -> st.getStatus() == TaskStatus.DONE);
            if (isNew) {
                epic.setStatus(TaskStatus.NEW);
            } else if (isDone) {
                epic.setStatus(TaskStatus.DONE);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }

        if (task.getClass() == Epic.class) {
            var epic = tasks.get(task.getId());
            var subtasks = getSubtasks(epic.getId());
            if (subtasks.isEmpty()) {
                epic.setStatus(TaskStatus.NEW);
            }
        }

    }

    public void removeTask(int id) {
        var task = tasks.get(id);
        if (task.getClass() == Epic.class) {
            var subtasks = ((Epic) task).getSubtasks();
            for (var subtask : subtasks) {
                tasks.remove(subtask);
            }
        }
        tasks.remove(id);
    }

    public ArrayList<Subtask> getSubtasks(int epicID) {
        var epic = (Epic) tasks.get(epicID);
        var result = new ArrayList<Subtask>();
        for (int id : epic.getSubtasks()) {
            if (tasks.containsKey(id)) {
                result.add((Subtask) tasks.get(id));
            }
        }
        return result;
    }
}
