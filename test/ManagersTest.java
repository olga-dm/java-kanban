import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {

    @Test
    public void shouldReturnTrueIfTaskManagerNotNull() {
        InMemoryTaskManager manager = (InMemoryTaskManager) Managers.getDefault();
        assertNotNull(manager);
    }

    @Test
    public void shouldReturnTrueIfHistoryManagerNotNull() {
        InMemoryHistoryManager manager = (InMemoryHistoryManager) Managers.getDefaultHistory();
        assertNotNull(manager);
    }
}