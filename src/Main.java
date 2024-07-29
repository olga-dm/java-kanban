public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        Task taskOne = Manager.createTask( "One", "First Task");
        Task taskTwo = Manager.createTask( "Two", "Second Task");
        Epic epic = Manager.createEpic("Epic", "First Epic Task");
        Subtask subtaskOne = Manager.createSubtask("Subtask", "First Subtask", epic.id);
        Subtask subtaskTwo = Manager.createSubtask("Subtask", "Second Subtask", epic.id);
        Epic epicTwo = Manager.createEpic("Epic", "Second Epic Task");
        Subtask subtaskThree = Manager.createSubtask("Subtask", "Third Subtask", epicTwo.id);

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
