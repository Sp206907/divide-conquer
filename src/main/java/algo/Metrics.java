package algo;

import java.util.concurrent.atomic.AtomicLong;

public final class Metrics {
    public static final class Snapshot {
        public final long comparisons;
        public final long allocations;
        public final long maxRecursionDepth;
        public final long elapsedNanos;

        Snapshot(long comparisons, long allocations, long maxRecursionDepth, long elapsedNanos) {
            this.comparisons = comparisons;
            this.allocations = allocations;
            this.maxRecursionDepth = maxRecursionDepth;
            this.elapsedNanos = elapsedNanos;
        }
    }

    private final ThreadLocal<Integer> currentDepth = ThreadLocal.withInitial(() -> 0);
    private final AtomicLong maxDepth = new AtomicLong(0);
    private final AtomicLong comparisons = new AtomicLong(0);
    private final AtomicLong allocations = new AtomicLong(0);
    private long startTime;

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public Snapshot stopAndSnapshot() {
        long elapsed = System.nanoTime() - startTime;
        return new Snapshot(comparisons.get(), allocations.get(), maxDepth.get(), elapsed);
    }

    public void incComparisons(long c) {
        comparisons.addAndGet(c);
    }

    public void incAllocations(long a) {
        allocations.addAndGet(a);
    }

    public void enterRecursion() {
        int depth = currentDepth.get() + 1;
        currentDepth.set(depth);
        maxDepth.accumulateAndGet(depth, Math::max);
    }

    public void exitRecursion() {
        int depth = currentDepth.get() - 1;
        currentDepth.set(Math.max(depth, 0));
    }
}


