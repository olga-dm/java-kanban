import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Поехали!");
        HistoryManager historyManager = Managers.getDefaultHistory();
        FileBackedTaskManager manager = new FileBackedTaskManager(new File("tasks.csv"));

        Task taskOne = manager.createTask("One", "First Task", Duration.ofMinutes(15), LocalDateTime.of(2022, 12, 30, 0, 30));
        Task taskTwo = manager.createTask("Two", "Second Task", Duration.ofMinutes(220), LocalDateTime.of(2024, 11, 21, 1, 30));
        Epic epic = manager.createEpic("Epic", "First Epic Task");
        Subtask subtaskOne = manager.createSubtask("Subtask", "First Subtask", epic.id, Duration.ofMinutes(21), LocalDateTime.of(2025, 10, 21, 15, 30));
        Subtask subtaskTwo = manager.createSubtask("Subtask", "Second Subtask", epic.id, Duration.ofMinutes(60), LocalDateTime.of(2025, 1, 12, 10, 30));
        Epic epicTwo = manager.createEpic("Epic", "Second Epic Task");
        Subtask subtaskThree = manager.createSubtask("Subtask", "Third Subtask", epicTwo.id, Duration.ofMinutes(60), LocalDateTime.of(2025, 1, 12, 10, 30));

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

        var a = manager.getPrioritizedTasks().stream().map(Task::getStartTime).toArray();
        printAllTasks(manager, historyManager);
    }

    private static void printAllTasks(TaskManager manager, HistoryManager historyManager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Subtask subtask : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + subtask);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : historyManager.getHistory()) {
            System.out.println(task);
        }
    }
}
