package algo;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class QuickSortDepthTest {
    @Test
    void recursionDepthTypicallyBounded() {
        int n = 20000;
        int[] a = new Random(42).ints(n).toArray();
        Metrics m = new Metrics();
        QuickSort.sort(a, m, new Random(42));
        Metrics.Snapshot s = m.stopAndSnapshot();
        int bound = 2 * (31 - Integer.numberOfLeadingZeros(Math.max(1, n)));
        assertTrue(s.maxRecursionDepth <= bound + 10, "depth=" + s.maxRecursionDepth + " bound=" + bound);
    }
}


