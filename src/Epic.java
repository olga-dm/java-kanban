import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private LocalDateTime endTime;
    //private final ArrayList<Integer> subtasks = new ArrayList<>();
    private final List<Subtask> subtasks;

    public Epic(int id, String name, String description) {
        super(id, name, description, Duration.ZERO, null);
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

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public Duration calculateDurationAndStartEndTime(List<Subtask> subtasks) {
        this.duration = Duration.ZERO;
        this.startTime = LocalDateTime.MAX;
        this.endTime = LocalDateTime.MIN;

        this.duration = subtasks.stream()
                .map(Task::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
        this.startTime = subtasks.stream()
                .map(Task::getStartTime)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(null);
        this.endTime = subtasks.stream()
                .map(Task::getEndTime)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(null);
        return this.duration;
    }

    @Override
    public String toString() {
        return String.format("id: %d, name: %s, subtasks: %s, status: %s \n", id, name, subtasks, status);
    }
}
