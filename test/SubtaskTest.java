import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskTest {
    @Test
    void subTaskWithSameIdShouldBeEqual() {
        var subtask = new Subtask(1, "First", "First task", 2, Duration.ofHours(6), LocalDateTime.of(2024, 10, 17, 14, 26));
        var subtask2 = new Subtask(1, "Second", "Second task", 3, Duration.ofHours(2), LocalDateTime.of(2024, 11, 12, 10, 22));
        assertEquals(subtask, subtask2, "");
    }
}