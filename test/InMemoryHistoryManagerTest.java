import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    InMemoryTaskManager manager;

    @BeforeEach
    public void init() throws Exception {
        manager = (InMemoryTaskManager) Managers.getDefault();
        Task taskOne = manager.createTask("One", "First Task");
        Task taskTwo = manager.createTask("Two", "Second Task");
        Epic epic = manager.createEpic("Epic", "First Epic Task");
        Subtask subtaskOne = manager.createSubtask("Subtask", "First Subtask", epic.id);
        Subtask subtaskTwo = manager.createSubtask("Subtask", "Second Subtask", epic.id);
        Epic epicTwo = manager.createEpic("Epic", "Second Epic Task");
        Subtask subtaskThree = manager.createSubtask("Subtask", "Third Subtask", epicTwo.id);

        epic.addSubtask(subtaskOne.id);
        epic.addSubtask(subtaskTwo.id);
        epicTwo.addSubtask(subtaskThree.id);

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
        Task task1Update = new Task(1, "Task1Update", "Description1Update");
        manager.update(task1Update);
        manager.getTask(task1Update.getId());
        Task savedTask = manager.historyList().getFirst();
        assertEquals(task1.getId(), savedTask.getId());
        assertEquals(task1.getName(), savedTask.getName());
        assertEquals(task1.getDescription(), savedTask.getDescription());
        assertEquals(task1.getStatus(), savedTask.getStatus());
    }

}