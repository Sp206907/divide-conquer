package algo;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MergeSortTest {
    @Test
    void sortsRandomArrays() {
        Random rnd = new Random(1);
        for (int n : new int[]{0,1,2,5,16,1000}) {
            int[] a = rnd.ints(n, -1000, 1000).toArray();
            int[] b = Arrays.copyOf(a, a.length);
            Arrays.sort(b);
            MergeSort.sort(a, new Metrics());
            assertArrayEquals(b, a);
        }
    }
}


