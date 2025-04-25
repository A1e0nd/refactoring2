import java.util.*;

public class GameLogic {
    private final Random random = new Random();

    public int[] generateUniqueNumbers(int count, int bound) {
        Set<Integer> set = new LinkedHashSet<>();
        while (set.size() < count) {
            set.add(random.nextInt(bound));
        }
        int[] result = set.stream().mapToInt(Integer::intValue).toArray();
        Arrays.sort(result);
        return result;
    }

    public boolean isValidNext(int[] correctSequence, List<Integer> selected, int next) {
        int index = selected.size();
        if (index >= correctSequence.length) return false;
        return next == correctSequence[index] &&
                (selected.isEmpty() || selected.get(index - 1) <= next);
    }
}
