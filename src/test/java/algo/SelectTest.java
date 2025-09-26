package algo;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class SelectTest {
    @Test
    void compareWithSortedK() {
        Random rnd = new Random(3);
        for (int t = 0; t < 100; t++) {
            int n = 50 + rnd.nextInt(150);
            int[] a = rnd.ints(n, -1000, 1000).toArray();
            int[] b = Arrays.copyOf(a, a.length);
            Arrays.sort(b);
            int k = rnd.nextInt(n);
            int sel = Select.kthSmallest(a, k, new Metrics());
            assertEquals(b[k], sel);
        }
    }
}


