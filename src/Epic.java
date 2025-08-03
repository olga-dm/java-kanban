import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtasks;

    public Epic(int id, String name, String description) {
        super(id, name, description, Duration.ZERO, null);
        this.subtasks = new ArrayList<>();
        this.endTime = null;
    }

    public Epic(String name, String description) {
        super(name, description, Duration.ZERO, null);
        this.subtasks = new ArrayList<>();
        this.endTime = null;
    }

    public void setEndTime(LocalDateTime dt) {
        this.endTime = dt;
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public List<Integer> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask.getId());
    }

    @Override
    public String toString() {
        return String.format("id: %d, name: %s, subtasks: %s, status: %s \n", id, name, subtasks, status);
    }
}
