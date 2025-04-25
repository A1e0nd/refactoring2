import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class GameLogicTest {

    @Test
    public void testGenerateUniqueNumbers() {
        GameLogic logic = new GameLogic();
        int[] numbers = logic.generateUniqueNumbers(16, 100);

        assertEquals(16, numbers.length);

        Set<Integer> set = new HashSet<>();
        for (int num : numbers) {
            assertTrue(num >= 0 && num < 100);
            set.add(num);
        }

        assertEquals("All numbers should be unique", 16, set.size());
    }

    @Test
    public void testValidNext() {
        GameLogic logic = new GameLogic();
        int[] correct = {5, 10, 15, 20};

        List<Integer> selected = new ArrayList<>();
        assertTrue(logic.isValidNext(correct, selected, 5));

        selected.add(5);
        assertTrue(logic.isValidNext(correct, selected, 10));

        selected.add(10);
        assertFalse(logic.isValidNext(correct, selected, 9)); // меньше предыдущего
        assertFalse(logic.isValidNext(correct, selected, 99)); // не по порядку

        selected.add(15);
        assertTrue(logic.isValidNext(correct, selected, 20));
    }
}
