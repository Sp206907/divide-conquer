package algo;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ClosestPairTest {
    @Test
    void bruteForceCheckSmallN() {
        Random rnd = new Random(4);
        for (int n : new int[]{2,3,5,20,200}) {
            ClosestPair.Point[] pts = new ClosestPair.Point[n];
            for (int i = 0; i < n; i++) pts[i] = new ClosestPair.Point(rnd.nextDouble()*1000, rnd.nextDouble()*1000);
            double fast = ClosestPair.closestDistance(pts, new Metrics());
            double brute = brute(pts);
            assertEquals(brute, fast, 1e-9);
        }
    }

    private static double brute(ClosestPair.Point[] p) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = 0; i < p.length; i++) {
            for (int j = i + 1; j < p.length; j++) {
                double dx = p[i].x - p[j].x;
                double dy = p[i].y - p[j].y;
                double d = Math.hypot(dx, dy);
                if (d < best) best = d;
            }
        }
        return best;
    }
}


