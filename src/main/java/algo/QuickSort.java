package algo;

import java.security.SecureRandom;
import java.util.Random;

public final class QuickSort {
    private static final int INSERTION_SORT_CUTOFF = 24;
    private static final SecureRandom RNG = new SecureRandom();

    public static void sort(int[] a, Metrics metrics) {
        if (a == null || a.length <= 1) return;
        if (metrics != null) metrics.startTimer();
        quicksort(a, 0, a.length - 1, metrics, RNG);
    }

    public static void sort(int[] a, Metrics metrics, Random rng) {
        if (a == null || a.length <= 1) return;
        if (metrics != null) metrics.startTimer();
        quicksort(a, 0, a.length - 1, metrics, rng == null ? RNG : rng);
    }

    private static void quicksort(int[] a, int lo, int hi, Metrics metrics, Random rng) {
        while (lo < hi) {
            int n = hi - lo + 1;
            if (n <= INSERTION_SORT_CUTOFF) {
                insertionSort(a, lo, hi, metrics);
                return;
            }
            if (metrics != null) metrics.enterRecursion();
            int p = partitionRandom(a, lo, hi, metrics, rng);
            int leftSize = p - lo;
            int rightSize = hi - p;
            if (leftSize < rightSize) {
                if (lo < p - 1) quicksort(a, lo, p - 1, metrics, rng);
                lo = p + 1; // tail recurse into larger side iteratively
                if (metrics != null) metrics.exitRecursion();
            } else {
                if (p + 1 < hi) quicksort(a, p + 1, hi, metrics, rng);
                hi = p - 1;
                if (metrics != null) metrics.exitRecursion();
            }
        }
    }

    private static int partitionRandom(int[] a, int lo, int hi, Metrics metrics, Random rng) {
        int pivotIndex = lo + rng.nextInt(hi - lo + 1);
        swap(a, pivotIndex, hi);
        int pivot = a[hi];
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (metrics != null) metrics.incComparisons(1);
            if (a[j] <= pivot) {
                i++;
                swap(a, i, j);
            }
        }
        swap(a, i + 1, hi);
        return i + 1;
    }

    private static void insertionSort(int[] a, int lo, int hi, Metrics metrics) {
        for (int i = lo + 1; i <= hi; i++) {
            int v = a[i];
            int j = i - 1;
            while (j >= lo) {
                if (metrics != null) metrics.incComparisons(1);
                if (a[j] <= v) break;
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = v;
        }
    }

    private static void swap(int[] a, int i, int j) {
        if (i == j) return;
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}


