import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager fileBackedTaskManager;
    private File file;
    protected TaskManager taskManager;

    @BeforeEach
    public void setUp() throws IOException {
        file = File.createTempFile("tasks", ".csv");
        fileBackedTaskManager = new FileBackedTaskManager(file);
    }

    @Test
    public void shouldTasksSaveToFile() throws Exception {
        Task task = new Task(1, "Task1", "Description1", Duration.ofHours(8), LocalDateTime.of(2025, 5, 17, 14, 26));
        Epic epic = new Epic(2, "Epic1", "DescriptionEpic1");
        Subtask subtask = new Subtask(3, "Subtask 1", "Subtask 1 Description", 2, Duration.ofHours(10), LocalDateTime.of(2025, 5, 8, 11, 35));
        fileBackedTaskManager.add(task);
        fileBackedTaskManager.add(epic);
        fileBackedTaskManager.add(subtask);
        String savedData = Files.readString(file.toPath());
        String expectedData = "id,type,name,description,status\n" +
                task.getId() + ",TASK,Task1,Description1,NEW,480,2025-05-17T14:26\n" +
                epic.getId() + ",EPIC,Epic1,DescriptionEpic1,NEW,600,2025-05-08T11:35\n" +
                subtask.getId() + ",SUBTASK,Subtask 1,Subtask 1 Description,NEW,2,600,2025-05-08T11:35\n";
        assertEquals(expectedData, savedData);
    }


}
