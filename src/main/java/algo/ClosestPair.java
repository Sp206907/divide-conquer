package algo;

import java.util.Arrays;

public final class ClosestPair {
    public static final class Point {
        public final double x;
        public final double y;
        public Point(double x, double y) { this.x = x; this.y = y; }
    }

    public static double closestDistance(Point[] points, Metrics metrics) {
        if (points == null || points.length < 2) return Double.POSITIVE_INFINITY;
        Point[] pts = Arrays.copyOf(points, points.length);
        if (metrics != null) metrics.incAllocations(points.length);
        Arrays.sort(pts, (a, b) -> Double.compare(a.x, b.x));
        if (metrics != null) metrics.startTimer();
        Point[] aux = new Point[pts.length];
        if (metrics != null) metrics.incAllocations(points.length);
        return divide(pts, aux, 0, pts.length, metrics);
    }

    private static double divide(Point[] px, Point[] aux, int lo, int hi, Metrics metrics) {
        if (metrics != null) metrics.enterRecursion();
        int n = hi - lo;
        if (n <= 3) {
            double d = brute(px, lo, hi, metrics);
            if (metrics != null) metrics.exitRecursion();
            return d;
        }
        int mid = lo + (n >>> 1);
        double dl = divide(px, aux, lo, mid, metrics);
        double dr = divide(px, aux, mid, hi, metrics);
        double d = Math.min(dl, dr);

        int m = 0;
        for (int i = lo; i < hi; i++) {
            if (Math.abs(px[i].x - px[mid].x) < d) aux[m++] = px[i];
        }
        Arrays.sort(aux, 0, m, (a, b) -> Double.compare(a.y, b.y));
        double best = d;
        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m && (aux[j].y - aux[i].y) < best && j <= i + 7; j++) {
                double dist = dist(aux[i], aux[j], metrics);
                if (dist < best) best = dist;
            }
        }
        if (metrics != null) metrics.exitRecursion();
        return best;
    }

    private static double brute(Point[] a, int lo, int hi, Metrics metrics) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = lo; i < hi; i++) {
            for (int j = i + 1; j < hi; j++) {
                double d = dist(a[i], a[j], metrics);
                if (d < best) best = d;
            }
        }
        return best;
    }

    private static double dist(Point p, Point q, Metrics metrics) {
        if (metrics != null) metrics.incComparisons(1);
        double dx = p.x - q.x;
        double dy = p.y - q.y;
        return Math.hypot(dx, dy);
    }
}


