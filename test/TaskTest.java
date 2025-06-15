import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
    @Test
    void taskWithSameIdShouldBeEqual() {
        var task1 = new Task(1, "First", "First task", Duration.ofHours(3), LocalDateTime.of(2025, 3, 22, 10, 56));
        var task2 = new Task(1, "Second", "Second task", Duration.ofHours(2), LocalDateTime.of(2025, 4, 15, 11, 26));
        assertEquals(task1, task2, "");
    }
}