import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    @Test
    public void epicsWithEqualIdShouldBeEqual() {
        Epic epic1 = new Epic(10, "Epic", "First Epic Task");
        Epic epic2 = new Epic(10, "Epic1", "Second Epic Task");
        assertEquals(epic1, epic2, "");
    }
}