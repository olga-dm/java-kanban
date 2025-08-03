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
        Task taskOne = new Task("One", "First Task", Duration.ofMinutes(15), LocalDateTime.of(2022, 12, 30, 0, 30));
        manager.createTask(taskOne);
        Task taskTwo = new Task("Two", "Second Task", Duration.ofMinutes(220), LocalDateTime.of(2024, 11, 21, 1, 30));
        manager.createTask(taskTwo);
        Epic epic = new Epic("Epic", "First Epic Task");
        manager.createEpic(epic);
        Subtask subtaskOne = new Subtask("Subtask", "First Subtask", epic.id, Duration.ofMinutes(21), LocalDateTime.of(2025, 10, 21, 15, 30));
        manager.createSubtask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Subtask", "Second Subtask", epic.id, Duration.ofMinutes(60), LocalDateTime.of(2025, 1, 12, 10, 30));
        manager.createSubtask(subtaskTwo);
        Epic epicTwo = new Epic("Epic", "Second Epic Task");
        manager.createEpic(epicTwo);
        Subtask subtaskThree = new Subtask("Subtask", "Third Subtask", epicTwo.id, Duration.ofMinutes(60), LocalDateTime.of(2025, 1, 12, 10, 30));
        manager.createSubtask(subtaskThree);
    }

    @Test
    public void shouldReturnTrueIfSavedTaskState() {
        Task task1 = manager.getTask(1);
        Task task1Update = new Task(task1.getId(), "Task1Update", "Description1Update", Duration.ofHours(1), LocalDateTime.of(2025, 11, 17, 14, 26));
        manager.update(task1Update);
        manager.getTask(task1Update.getId());
        Task savedTask = manager.historyList().getLast();
        assertEquals(task1.getId(), savedTask.getId());
        assertEquals(task1.getName(), savedTask.getName());
        assertEquals(task1.getDescription(), savedTask.getDescription());
        assertEquals(task1.getStatus(), savedTask.getStatus());
    }

    @Test
    public void shouldReturnTrueIfHistoryHaveCorrectOrder() {
        Task task1 = new Task("MemoryOne", "First Task", Duration.ofMinutes(100), LocalDateTime.of(2025, 5, 21, 17, 0));
        manager.createTask(task1);
        Task task2 = new Task("MemoryTwo", "Second Task", Duration.ofMinutes(23), LocalDateTime.of(2025, 5, 19, 18, 10));
        manager.createTask(task2);


        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getTask(task1.getId());

        List<Task> history = manager.historyList();

        assertEquals(2, history.size(), "История должна содержать 2 задачи без дубликатов.");
        assertEquals(task2, history.get(0), "Первая задача должна быть task2.");
        assertEquals(task1, history.get(1), "Вторая задача должна быть task1.");
    }
}