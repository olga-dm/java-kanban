public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        Task taskOne = new Task( "One", "First Task");
        Task taskTwo = new Task( "Two", "Second Task");
        Epic epic = new Epic("Epic", "First Epic Task");
        Subtask subtaskOne = new Subtask("Subtask", "First Subtask", epic.id);
        Subtask subtaskTwo = new Subtask("Subtask", "Second Subtask", epic.id);
        Epic epicTwo = new Epic("Epic", "Second Epic Task");
        Subtask subtaskThree = new Subtask("Subtask", "Third Subtask", epicTwo.id);

        epic.addSubtask(subtaskOne.id);
        epic.addSubtask(subtaskTwo.id);
        epicTwo.addSubtask(subtaskThree.id);

        Manager manager = new Manager();
        manager.add(taskOne);
        manager.add(taskTwo);
        manager.add(epic);
        manager.add(epicTwo);
        manager.add(subtaskOne);
        manager.add(subtaskTwo);
        manager.add(subtaskThree);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        subtaskThree.setStatus(TaskStatus.IN_PROGRESS);
        manager.update(subtaskThree);
        System.out.print(manager.getEpic(epicTwo.id));
        System.out.print(manager.getSubtask(subtaskThree.id));

        subtaskThree.setStatus(TaskStatus.DONE);
        manager.update(subtaskThree);
        System.out.print(manager.getEpic(epicTwo.id));
        System.out.print(manager.getSubtask(subtaskThree.id));

        manager.removeTask(taskOne.getId());
        System.out.println(manager.getAllTasks());
        manager.removeEpic(epicTwo.getId());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

    }
}
