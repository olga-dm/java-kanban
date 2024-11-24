import java.io.File;

public class Managers {
    private static final String FILE_NAME = "manager.db";

    public static TaskManager getDefault() {
        return new FileBackedTaskManager(new File(FILE_NAME));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
