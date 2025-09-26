# Divide-and-Conquer Algorithms: Implementation and Analysis

A project on classic Divide-and-Conquer algorithms with safe recursion templates, a metrics system, and a report.

## Architecture and Metrics

* Metrics: time (System.nanoTime), recursion depth, comparisons, allocations.
* `algo.Metrics` class: thread-local recursion depth counter (enterRecursion/exitRecursion), atomic counters for comparisons/allocations, snapshot for CSV.
* Minimizing allocations: `MergeSort` uses one buffer; `ClosestPair` uses a copy of the point array + a helper array for the strip.
* Stack depth control:

    * `QuickSort`: recursion only on the smaller part; the larger part is processed iteratively (tail recursion elimination). Typical depth is O(log n).
    * `Select`: MoM5 and tail recursion for the smaller part.

## Implemented Algorithms

1. **MergeSort** (Master Case 2)

    * Recurrence: T(n) = 2T(n/2) + Θ(n). According to the Master Theorem, Case 2: Θ(n log n).
    * Practical aspects: depth ≈ ⌈log2 n⌉; linear merging, the buffer is reused; cut-off to insertion sort for small n.

2. **QuickSort** (randomized, bounded stack)

    * Idea: random pivot, recursion only on the smaller part; larger part in a loop. Expected depth is O(log n).
    * Estimate: expected time Θ(n log n); worst case Θ(n^2) is unlikely with a random pivot.

3. **Deterministic Select** (Median-of-Medians, MoM5)

    * Grouping by 5, taking medians, then the median of medians as the pivot; in-place partitioning.
    * Recurrence (intuitively Akra–Bazzi): T(n) ≤ T(⌈n/5⌉) + T(7n/10) + O(n) ⇒ Θ(n).

4. **Closest Pair of Points** (2D)

    * Sorting by x, dividing into two halves, using a strip for y (checking ≤ 7–8 neighbors).
    * Recurrence: T(n) = 2T(n/2) + O(n) ⇒ Θ(n log n).

## Measurement and Graphing Plans

* CLI `algo.BenchCLI` outputs CSV: algo,n,trial,time_ns,comparisons,allocations,max_depth.
* Sizes of n: 1e2, 5e2, 1e3, 5e3, 1e4, 5e4, 1e5 (based on time/machine capabilities).
* Build graphs (using external tools): time vs n, depth vs n, comparisons vs n.
* Discussion factors: JIT warm-up, cache, data distribution, GC.

## Quick Start

* Build: `mvn -q -DskipTests package`
* Tests: `mvn -q test`
* CLI examples:

    * `java -cp target/divide-and-conquer-1.0.0.jar algo.BenchCLI --algo mergesort --n 100000 --trials 5`
    * `java -cp target/divide-and-conquer-1.0.0.jar algo.BenchCLI --algo quicksort --n 100000 --trials 5`
    * `java -cp target/divide-and-conquer-1.0.0.jar algo.BenchCLI --algo select --n 500000 --k 12345 --trials 3`
    * `java -cp target/divide-and-conquer-1.0.0.jar algo.BenchCLI --algo closestpair --n 20000 --trials 3`

## Testing

* Sorting: correctness on random and "bad" arrays (reverse, duplicates), QS depth: ≤ ~2·⌊log2 n⌋ + O(1) typically.
* Select: comparison with `Arrays.sort(a)[k]` on 100 tests.
* Closest Pair: validation against O(n^2) for small n (≤ 2000).

## Git Workflow Guide (for your repo)

* Branches: `main`, `feature/metrics`, `feature/mergesort`, `feature/quicksort`, `feature/select`, `feature/closest`, `feature/cli`.
* Commit history (example): init → feat(metrics) → feat(mergesort) → feat(quicksort) → refactor(util) → feat(select) → feat(closest) → feat(cli) → docs(report) → release: v1.0.


## Краткие выводы (заполнить после прогонов)
- Теория vs замеры: где совпадает, где расходится; причины (кэш, GC, распределения).
