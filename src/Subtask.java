import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicID;

    public Subtask(int id, String name, String description, int epicID, Duration duration, LocalDateTime startTime) {
        super(id, name, description,duration, startTime);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    @Override
    public String toString() {
        return String.format("id: %d, name: %s, epic: %d, status: %s\n", id, name, epicID, status);
    }
}
