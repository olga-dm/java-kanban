import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task implements Comparable<Task> {
    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus status;

    protected Duration duration;
    protected LocalDateTime startTime;
    private LocalDateTime endTime;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setDuration(Duration duration) {
        if (duration.isPositive()) {
            this.duration = duration;
        }
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return null;
        }

        return startTime.plus(duration);
    }

    public void setDuration(int intDuration) {
        Duration duration = Duration.ofMinutes(intDuration);
        if (duration.isPositive()) {
            this.duration = duration;
        }
    }

    public Duration getDuration() {
        if (duration == null)
            return Duration.ZERO;
        return duration;
    }

    public Task(int id, String name, String description, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.duration = duration;
        this.startTime = startTime != null ? startTime : LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return task.id == this.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("id: %d, name: %s, status: %s \n", id, name, status);
    }

    @Override
    public int compareTo(Task t) {
        return startTime.isBefore(t.getStartTime())
                ? -1
                : startTime.isAfter(t.getStartTime())
                ? 1
                : 0;
    }

    public boolean isIntersect(Task other) {
        if (other.getStartTime() == null || other.getEndTime() == null || this.getStartTime() == null || this.getEndTime() == null)
            return false;

        return !this.getStartTime().isAfter(other.getEndTime()) && !this.getEndTime().isBefore(other.getStartTime());
    }
}
