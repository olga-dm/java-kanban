import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskTest {
    @Test
    void subTaskWithSameIdShouldBeEqual() {
        var subtask = new Subtask(1, "First", "First task", 2);
        var subtask2 = new Subtask(1, "Second", "Second task", 3);
        assertEquals(subtask, subtask2, "");
    }
}