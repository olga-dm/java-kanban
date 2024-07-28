public class Subtask extends Task {
    private final int epicID;

    public Subtask(String name, String description, int epicID) {
        super(name, description);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    @Override
    public String toString() {
        return String.format("id: %d, name: %s, epic: %d, status: %s\n", id, name, epicID, status);
    }
}
