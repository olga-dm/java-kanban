import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
    @Test
    void taskWithSameIdShouldBeEqual() {
        var task1 = new Task(1, "First", "First task");
        var task2 = new Task(1, "Second", "Second task");
        assertEquals(task1, task2, "");
    }
}