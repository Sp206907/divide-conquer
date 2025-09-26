package algo;

import java.util.Arrays;

public final class Select {
    public static int kthSmallest(int[] a, int k, Metrics metrics) {
        if (k < 0 || k >= a.length) throw new IllegalArgumentException("k out of range");
        if (metrics != null) metrics.startTimer();
        int res = select(a, 0, a.length - 1, k, metrics);
        return res;
    }

    private static int select(int[] a, int lo, int hi, int k, Metrics metrics) {
        while (true) {
            if (lo == hi) return a[lo];
            if (metrics != null) metrics.enterRecursion();
            int pivotIndex = medianOfMedians(a, lo, hi, metrics);
            int pivotNewIndex = partition(a, lo, hi, pivotIndex, metrics);
            if (pivotNewIndex == k) {
                if (metrics != null) metrics.exitRecursion();
                return a[pivotNewIndex];
            } else if (k < pivotNewIndex) {
                hi = pivotNewIndex - 1; // recurse into smaller by tail loop
                if (metrics != null) metrics.exitRecursion();
            } else {
                lo = pivotNewIndex + 1;
                if (metrics != null) metrics.exitRecursion();
            }
        }
    }

    private static int partition(int[] a, int lo, int hi, int pivotIndex, Metrics metrics) {
        int pivot = a[pivotIndex];
        swap(a, pivotIndex, hi);
        int store = lo;
        for (int i = lo; i < hi; i++) {
            if (metrics != null) metrics.incComparisons(1);
            if (a[i] < pivot) {
                swap(a, store++, i);
            }
        }
        swap(a, store, hi);
        return store;
    }

    private static int medianOfMedians(int[] a, int lo, int hi, Metrics metrics) {
        int n = hi - lo + 1;
        if (n <= 5) {
            Arrays.sort(a, lo, hi + 1);
            if (metrics != null) metrics.incComparisons(n * (long) Math.log(Math.max(1, n))); // rough accounting
            return lo + n / 2;
        }
        int numGroups = (n + 4) / 5;
        for (int i = 0; i < numGroups; i++) {
            int gLo = lo + i * 5;
            int gHi = Math.min(gLo + 4, hi);
            Arrays.sort(a, gLo, gHi + 1);
            if (metrics != null) metrics.incComparisons((gHi - gLo + 1) * 2L); // bounded small sort cost
            int medianIndex = gLo + (gHi - gLo) / 2;
            swap(a, lo + i, medianIndex);
        }
        int mid = lo + numGroups / 2;
        return selectIndexOfMedian(a, lo, lo + numGroups - 1, mid, metrics);
    }

    private static int selectIndexOfMedian(int[] a, int lo, int hi, int target, Metrics metrics) {
        // deterministic select on indices (median of medians) to position the median at 'target'
        while (true) {
            if (lo == hi) return lo;
            int pivot = medianOfMedians(a, lo, hi, metrics);
            int p = partition(a, lo, hi, pivot, metrics);
            if (p == target) return p;
            if (target < p) hi = p - 1; else lo = p + 1;
        }
    }

    private static void swap(int[] a, int i, int j) {
        if (i == j) return;
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}


