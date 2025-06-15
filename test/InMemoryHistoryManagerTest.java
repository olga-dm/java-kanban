import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    InMemoryTaskManager manager;

    @BeforeEach
    public void init() throws Exception {
        manager = (InMemoryTaskManager) Managers.getDefault();
        Task taskOne = manager.createTask("One", "First Task", Duration.ofMinutes(124), LocalDateTime.of(2025, 5, 21, 1, 30));
        Task taskTwo = manager.createTask("Two", "Second Task", Duration.ofMinutes(200), LocalDateTime.of(2025, 5, 6, 12, 30));
        Epic epic = manager.createEpic("Epic", "First Epic Task");
        Subtask subtaskOne = manager.createSubtask("Subtask", "First Subtask", epic.id, Duration.ofMinutes(345), LocalDateTime.of(2025, 5, 2, 15, 30));
        Subtask subtaskTwo = manager.createSubtask("Subtask", "Second Subtask", epic.id, Duration.ofMinutes(456), LocalDateTime.of(2025, 3, 21, 13, 45));
        Epic epicTwo = manager.createEpic("Epic", "Second Epic Task");
        Subtask subtaskThree = manager.createSubtask("Subtask", "Third Subtask", epicTwo.id, Duration.ofMinutes(60), LocalDateTime.of(2025, 5, 22, 10, 31));

        epic.addSubtask(subtaskOne);
        epic.addSubtask(subtaskTwo);
        epicTwo.addSubtask(subtaskThree);

        manager.add(taskOne);
        manager.add(taskTwo);
        manager.add(epic);
        manager.add(epicTwo);
        manager.add(subtaskOne);
        manager.add(subtaskTwo);
        manager.add(subtaskThree);
    }

    @Test
    public void shouldReturnTrueIfSavedTaskState() {
        Task task1 = manager.getTask(1);
        Task task1Update = new Task(1, "Task1Update", "Description1Update", Duration.ofHours(1), LocalDateTime.of(2025, 1, 17, 14, 26));
        manager.update(task1Update);
        manager.getTask(task1Update.getId());
        Task savedTask = manager.historyList().getFirst();
        assertEquals(task1.getId(), savedTask.getId());
        assertEquals(task1.getName(), savedTask.getName());
        assertEquals(task1.getDescription(), savedTask.getDescription());
        assertEquals(task1.getStatus(), savedTask.getStatus());
    }

    @Test
    public void shouldReturnTrueIfHistoryHaveCorrectOrder() {
        Task task1 = manager.createTask("MemoryOne", "First Task", Duration.ofMinutes(100), LocalDateTime.of(2025, 5, 21, 17, 0));
        Task task2 = manager.createTask("MemoryTwo", "Second Task", Duration.ofMinutes(23), LocalDateTime.of(2025, 5, 19, 18, 10));

        manager.add(task1);
        manager.add(task2);

        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getTask(task1.getId());

        List<Task> history = manager.historyList();

        assertEquals(2, history.size(), "История должна содержать 2 задачи без дубликатов.");
        assertEquals(task2, history.get(0), "Первая задача должна быть task2.");
        assertEquals(task1, history.get(1), "Вторая задача должна быть task1.");
    }
}