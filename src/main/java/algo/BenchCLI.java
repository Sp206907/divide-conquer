package algo;

import java.security.SecureRandom;
import java.util.Arrays;

public final class BenchCLI {
    public static void main(String[] args) {
        if (args.length == 0 || Arrays.asList(args).contains("--help")) {
            System.out.println("Usage: --algo [mergesort|quicksort|select|closestpair] --n 1000 [--trials 3] [--k 0]");
            return;
        }
        String algo = "mergesort";
        int n = 1000;
        int trials = 3;
        int k = 0;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--algo": algo = args[++i]; break;
                case "--n": n = Integer.parseInt(args[++i]); break;
                case "--trials": trials = Integer.parseInt(args[++i]); break;
                case "--k": k = Integer.parseInt(args[++i]); break;
            }
        }
        CsvPrinter csv = new CsvPrinter(System.out);
        csv.header("algo","n","trial","time_ns","comparisons","allocations","max_depth");
        SecureRandom rnd = new SecureRandom();
        for (int t = 1; t <= trials; t++) {
            Metrics m = new Metrics();
            Metrics.Snapshot s;
            switch (algo.toLowerCase()) {
                case "mergesort": {
                    int[] a = rnd.ints(n).toArray();
                    MergeSort.sort(a, m);
                    s = m.stopAndSnapshot();
                    break;
                }
                case "quicksort": {
                    int[] a = rnd.ints(n).toArray();
                    QuickSort.sort(a, m);
                    s = m.stopAndSnapshot();
                    break;
                }
                case "select": {
                    int[] a = rnd.ints(n).toArray();
                    int kk = Math.min(Math.max(0, k), Math.max(0, n - 1));
                    Select.kthSmallest(a, kk, m);
                    s = m.stopAndSnapshot();
                    break;
                }
                case "closestpair": {
                    ClosestPair.Point[] pts = new ClosestPair.Point[n];
                    for (int i = 0; i < n; i++) pts[i] = new ClosestPair.Point(rnd.nextDouble(), rnd.nextDouble());
                    ClosestPair.closestDistance(pts, m);
                    s = m.stopAndSnapshot();
                    break;
                }
                default:
                    System.err.println("Unknown algo: " + algo);
                    return;
            }
            csv.row(algo, n, t, s.elapsedNanos, s.comparisons, s.allocations, s.maxRecursionDepth);
        }
    }
}


