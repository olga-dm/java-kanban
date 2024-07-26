public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        Task taskOne = new Task(1, "One", "First Task");
        Task taskTwo = new Task(2, "Two", "Second Task");
        Epic epic = new Epic(3, "Epic", "First Epic Task");
        Subtask subtaskOne = new Subtask(4, "Subtask", "First Subtask", epic.id);
        Subtask subtaskTwo = new Subtask(5, "Subtask", "Second Subtask", epic.id);
        Epic epicTwo = new Epic(6, "Epic", "Second Epic Task");
        Subtask subtaskThree = new Subtask(7, "Subtask", "Third Subtask", epicTwo.id);

        epic.addSubtask(subtaskOne.id);
        epic.addSubtask(subtaskTwo.id);
        epicTwo.addSubtask(subtaskThree.id);

        Manager manager = new Manager();
        manager.addTask(taskOne);
        manager.addTask(taskTwo);
        manager.addTask(epic);
        manager.addTask(epicTwo);
        manager.addTask(subtaskOne);
        manager.addTask(subtaskTwo);
        manager.addTask(subtaskThree);

        System.out.println(manager.getTasks());

        subtaskThree.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(subtaskThree);
        System.out.print(manager.getTask(epicTwo.id));
        System.out.print(manager.getTask(subtaskThree.id));

        subtaskThree.setStatus(TaskStatus.DONE);
        manager.updateTask(subtaskThree);
        System.out.print(manager.getTask(epicTwo.id));
        System.out.print(manager.getTask(subtaskThree.id));

        manager.removeTask(taskOne.getId());
        manager.removeTask(epicTwo.getId());
        System.out.println(manager.getTasks());
    }
}
