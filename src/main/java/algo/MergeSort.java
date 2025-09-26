package algo;

import java.util.Arrays;

public final class MergeSort {
    private static final int INSERTION_SORT_CUTOFF = 24;

    public static void sort(int[] a, Metrics metrics) {
        if (a == null || a.length <= 1) return;
        int[] buffer = Arrays.copyOf(a, a.length);
        if (metrics != null) metrics.incAllocations(a.length);
        if (metrics != null) metrics.startTimer();
        sortRecursive(a, buffer, 0, a.length, metrics);
    }

    public static Metrics.Snapshot sortWithSnapshot(int[] a) {
        Metrics m = new Metrics();
        sort(a, m);
        return m.stopAndSnapshot();
    }

    private static void sortRecursive(int[] a, int[] buf, int lo, int hi, Metrics metrics) {
        if (metrics != null) metrics.enterRecursion();
        int n = hi - lo;
        if (n <= 1) {
            if (metrics != null) metrics.exitRecursion();
            return;
        }
        if (n <= INSERTION_SORT_CUTOFF) {
            insertionSort(a, lo, hi, metrics);
            if (metrics != null) metrics.exitRecursion();
            return;
        }
        int mid = lo + (n >>> 1);
        sortRecursive(a, buf, lo, mid, metrics);
        sortRecursive(a, buf, mid, hi, metrics);
        merge(a, buf, lo, mid, hi, metrics);
        if (metrics != null) metrics.exitRecursion();
    }

    private static void insertionSort(int[] a, int lo, int hi, Metrics metrics) {
        for (int i = lo + 1; i < hi; i++) {
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

    private static void merge(int[] a, int[] buf, int lo, int mid, int hi, Metrics metrics) {
        System.arraycopy(a, lo, buf, lo, hi - lo);
        int i = lo;
        int j = mid;
        int k = lo;
        while (i < mid && j < hi) {
            if (metrics != null) metrics.incComparisons(1);
            if (buf[i] <= buf[j]) {
                a[k++] = buf[i++];
            } else {
                a[k++] = buf[j++];
            }
        }
        while (i < mid) a[k++] = buf[i++];
        while (j < hi) a[k++] = buf[j++];
    }
}


