import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryTaskManagerTest {
    InMemoryTaskManager manager;

    @BeforeEach
    public void init() throws Exception {
        manager = (InMemoryTaskManager) Managers.getDefault();
        Task taskOne = new Task("One", "First Task", Duration.ofMinutes(100), LocalDateTime.of(2025, 5, 13, 15, 30));
        manager.createTask(taskOne);
        Task taskTwo = new Task("Two", "Second Task", Duration.ofHours(24), LocalDateTime.of(2025, 4, 21, 16, 45));
        manager.createTask(taskTwo);
        Epic epic = new Epic("Epic", "First Epic Task");
        manager.createEpic(epic);
        Subtask subtaskOne = new Subtask("Subtask", "First Subtask", epic.id, Duration.ofMinutes(300), LocalDateTime.of(2024, 12, 21, 19, 46));
        manager.createSubtask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Subtask", "Second Subtask", epic.id, Duration.ofHours(4), LocalDateTime.of(2024, 5, 12, 1, 30));
        manager.createSubtask(subtaskTwo);
        Epic epicTwo = new Epic("Epic", "Second Epic Task");
        manager.createEpic(epicTwo);
        Subtask subtaskThree = new Subtask("Subtask", "Third Subtask", epicTwo.id, Duration.ofHours(10), LocalDateTime.of(2025, 3, 12, 1, 30));
        manager.createSubtask(subtaskThree);

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
    public void shouldReturnTwoIfRealContainsTasks() {
        int count = manager.getAllTasks().size();
        assertEquals(2, count);
    }

    @Test
    public void shouldReturnTwoIfRealContainsEpics() {
        int count = manager.getAllEpics().size();
        assertEquals(2, count);
    }

    @Test
    public void shouldReturnFiveIfRealContainsSubtasks() {
        int count = manager.getAllSubtasks().size();
        assertEquals(3, count);
    }

    @Test
    public void shouldReturnTaskIfRealContainsTasks() {
        Task task1 = new Task("New Task", "New Task", Duration.ofHours(4), LocalDateTime.of(2024, 5, 12, 1, 30));
        manager.createTask(task1);
        Task resultTusk = manager.getTask(task1.getId());
        assertEquals(task1, resultTusk);
    }

    @Test
    public void shouldReturnEpicIfRealContainsEpics() {
        Epic epic1 = new Epic("Epic1", "Description1");
        manager.createEpic(epic1);
        Epic resultEpic = manager.getEpic(epic1.getId());
        assertEquals(epic1, resultEpic);
    }

    @Test
    public void shouldReturnSubtaskIfRealContainsSubtasks() throws Exception {
        Epic epic1 = new Epic("Epic1", "Description1");
        manager.createEpic(epic1);
        Subtask subTask1 = new Subtask("Subtusk", "Description1", epic1.getId(), Duration.ofHours(4), LocalDateTime.of(2024, 5, 12, 1, 30));
        manager.createSubtask(subTask1);
        Subtask resultTusk = manager.getSubtask(subTask1.getId());
        assertEquals(subTask1, resultTusk);
    }

    @Test
    void epicCantBeUsedAsSubtask() throws Exception {
        TaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic("Epic", "First Epic Task");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask", "Sub", 2, Duration.ofMinutes(124), LocalDateTime.of(2025, 5, 21, 1, 30));
        manager.createSubtask(subtask);
        Exception exception = assertThrows(Exception.class, () -> {
            manager.add(subtask);
        });
    }

    @Test
    void subtaskCantBeUsedAsEpic() throws Exception {
        TaskManager manager = new InMemoryTaskManager();
        Subtask subtask = new Subtask("Subtask", "Sub", 1, Duration.ofMinutes(100), LocalDateTime.of(2025, 5, 21, 1, 30));
        manager.createSubtask(subtask);
        Exception exception = assertThrows(Exception.class, () -> {
            manager.add(subtask);
        });
    }
}