import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> subtasks = new ArrayList<>();

    public Epic(int id, String name, String description) {
        super(id, name, description);
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(int subtaskId) {
        subtasks.add(subtaskId);
    }

    @Override
    public String toString() {
        return String.format("id: %d, name: %s, subtasks: %s, status: %s \n", id, name, subtasks, status);
    }
}
