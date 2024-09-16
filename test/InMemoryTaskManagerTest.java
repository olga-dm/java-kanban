import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryTaskManagerTest {
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
        Task task1 = manager.createTask("New Task", "New Task");
        manager.add(task1);
        Task resultTusk = manager.getTask(task1.getId());
        assertEquals(task1, resultTusk);
    }

    @Test
    public void shouldReturnEpicIfRealContainsEpics() {
        Epic epic1 = manager.createEpic("Epic1", "Description1");
        manager.add(epic1);
        Epic resultEpic = manager.getEpic(epic1.getId());
        assertEquals(epic1, resultEpic);
    }

    @Test
    public void shouldReturnSubtaskIfRealContainsSubtasks() throws Exception {
        Subtask subTask1 = manager.createSubtask("Subtusk", "Description1", 3);
        manager.add(subTask1);
        Subtask resultTusk = manager.getSubtask(subTask1.getId());
        assertEquals(subTask1, resultTusk);
    }

    @Test
    void epicCantBeUsedAsSubtask() throws Exception {
        TaskManager manager = new InMemoryTaskManager();
        Epic epic = manager.createEpic("Epic", "First Epic Task");
        manager.add(epic);
        Subtask subtask = manager.createSubtask("Subtask", "Sub", 2);
        Exception exception = assertThrows(Exception.class, () -> {
            manager.add(subtask);
        });
    }

    @Test
    void subtaskCantBeUsedAsEpic() throws Exception {
        TaskManager manager = new InMemoryTaskManager();
        Subtask subtask = manager.createSubtask("Subtask", "Sub", 1);
        Exception exception = assertThrows(Exception.class, () -> {
            manager.add(subtask);
        });
    }
}