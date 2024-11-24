import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            String content = Files.readString(file.toPath());
            String[] lines = content.split(System.lineSeparator());

            // Пропускаем заголовок
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.isEmpty()) {
                    continue; // Игнорируем пустые строки
                }

                String[] parts = line.split(",");
                if (parts.length < 5) {
                    throw new ManagerSaveException("Неверное количество полей в строке: " + line);
                }

                int id = Integer.parseInt(parts[0]);
                String taskType = parts[1];

                switch (taskType) {
                    case "TASK":
                        Task task = new Task(id, parts[2], parts[3]);
                        manager.add(task);
                        break;
                    case "EPIC":
                        Epic epic = new Epic(id, parts[2], parts[3]);
                        manager.add(epic);
                        break;
                    case "SUBTASK":
                        if (parts.length != 6) {
                            throw new ManagerSaveException("Неверное количество полей для подзадачи: " + line);
                        }
                        Subtask subtask = new Subtask(id, parts[2], parts[3], Integer.parseInt(parts[5]));
                        manager.add(subtask);
                        break;
                    default:
                        throw new ManagerSaveException("Неизвестный тип задачи: " + taskType);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось загрузить задачи из файла: " + file.getPath(), e);
        } catch (NumberFormatException e) {
            throw new ManagerSaveException("Ошибка формата данных в файле: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ManagerSaveException("Неизвестная ошибка при загрузке задач: " + e.getMessage(), e);
        }
        return manager;
    }

    @Override
    public void add(Task task) {
        super.add(task);
        saveToFile();
    }

    @Override
    public void add(Epic epic) {
        super.add(epic);
        saveToFile();
    }

    @Override
    public void add(Subtask subtask) throws Exception {
        super.add(subtask);
        saveToFile();
    }

    @Override
    public void update(Task task) {
        super.update(task);
        saveToFile();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        saveToFile();
    }

    @Override
    public void update(Subtask subtask) {
        super.update(subtask);
        saveToFile();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        saveToFile();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        saveToFile();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        saveToFile();
    }

    private void saveToFile() {
        try {
            StringBuilder content = new StringBuilder();

            // Записываем заголовки
            content.append("id,type,name,description,status");
            content.append(System.lineSeparator());

            // Записываем задачи
            for (Task task : getAllTasks()) {
                content.append(task.getId()).append(",")
                        .append("TASK,")
                        .append(task.getName()).append(",")
                        .append(task.getDescription()).append(",")
                        .append(task.getStatus());
                content.append(System.lineSeparator());
            }

            // Записываем эпики
            for (Epic epic : getAllEpics()) {
                content.append(epic.getId()).append(",")
                        .append("EPIC,")
                        .append(epic.getName()).append(",")
                        .append(epic.getDescription()).append(",")
                        .append(epic.getStatus());
                content.append(System.lineSeparator());
            }

            // Записываем подзадачи
            for (Subtask subtask : getAllSubtasks()) {
                content.append(subtask.getId()).append(",")
                        .append("SUBTASK,")
                        .append(subtask.getName()).append(",")
                        .append(subtask.getDescription()).append(",")
                        .append(subtask.getStatus()).append(",")
                        .append(subtask.getEpicID());
                content.append(System.lineSeparator());
            }

            // Записываем содержимое в файл
            Files.writeString(file.toPath(), content.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении задач в файл: " + file.getPath(), e);
        } catch (Exception e) {
            throw new ManagerSaveException("Неизвестная ошибка при сохранении задач: " + e.getMessage(), e);
        }
    }
}
