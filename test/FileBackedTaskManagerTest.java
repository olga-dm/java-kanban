import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager fileBackedTaskManager;
    private File file;

    @BeforeEach
    public void setUp() throws IOException {
        file = File.createTempFile("test", ".csv");
        fileBackedTaskManager = new FileBackedTaskManager(file);
    }

    @Test
    public void shouldLoadTasksFromFile() throws IOException {
        String fileContent = "id,type,name,status,description,epic\n" +
                "1,TASK,Task 1,NEW,Task 1 Description,\n" +
                "2,EPIC,Epic 1,NEW,Epic 1 Description,\n" +
                "3,SUBTASK,Subtask 1,NEW,Subtask 1 Description,2\n";
        Files.writeString(file.toPath(), fileContent);
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        List<Task> tasks = loadedManager.getAllTasks();
        List<Epic> epics = loadedManager.getAllEpics();
        List<Subtask> subtasks = loadedManager.getAllSubtasks();
        assertEquals(1, tasks.size());
        assertEquals(1, epics.size());
        assertEquals(1, subtasks.size());
        Task task = tasks.getFirst();
        Epic epic = epics.getFirst();
        Subtask subtask = subtasks.getFirst();
        assertEquals("Task 1", task.getName());
        assertEquals(TaskStatus.NEW, task.getStatus());
        assertEquals("Epic 1", epic.getName());
        assertEquals(TaskStatus.NEW, epic.getStatus());
        assertEquals("Subtask 1", subtask.getName());
        assertEquals(TaskStatus.NEW, subtask.getStatus());
        assertEquals(2, subtask.getEpicID());
    }

    @Test
    public void shouldTasksSaveToFile() throws Exception {
        Task task = new Task(1, "Task1", "Description1");
        Epic epic = new Epic(2, "Epic1", "DescriptionEpic1");
        Subtask subtask = new Subtask(3, "Subtask 1", "Subtask 1 Description", 2);
        fileBackedTaskManager.add(task);
        fileBackedTaskManager.add(epic);
        fileBackedTaskManager.add(subtask);
        fileBackedTaskManager.saveToFile();
        String savedData = Files.readString(file.toPath());
        String expectedData = "id,type,name,description,status\n" +
                task.getId() + ",TASK,Task1,Description1,NEW\n" +
                epic.getId() + ",EPIC,Epic1,DescriptionEpic1,NEW\n" +
                subtask.getId() + ",SUBTASK,Subtask 1,Subtask 1 Description,NEW,2\n";
        assertEquals(expectedData, savedData);
    }
}
