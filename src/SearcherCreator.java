import java.util.concurrent.*;
import java.util.ArrayList;
/**
 * Author's Name: ThaiBinh Nguyen
 *
 * This class creates the GraphSearcher threads and controls the multithreading for them. Additionally, it maintains
 * the global best score for all of the GraphSearcher threads running.
 */
public class SearcherCreator {
    private Double score;
    private int[] path;

    public SearcherCreator() {
        score = Double.MAX_VALUE;
    }

    public void newSearch(int threads, int searches, int iterations, double[][] newGraph, double[][] coords) {
        GraphSearcher[] searchers = new GraphSearcher[threads];
        Thread[] t = new Thread[threads];
        Semaphore accessScore = new Semaphore(threads - 1); // Semaphores to control access

        ArrayList<Double> bestScore = new ArrayList<Double>();      // I used ArrayList, because Double() didn't work
        bestScore.add(Double.MAX_VALUE);                            // Add arbitrarily large value
        ArrayList<int[]> bestPath = new ArrayList<int[]>();         // again ArrayList, because collections synchronize
        int[] fillerArray = {1,2,3,4,5};
        bestPath.add(fillerArray);

        for (int i=0; i<threads; i++) {
            searchers[i] = new GraphSearcher(accessScore, bestScore, bestPath, iterations, searches, newGraph, coords);
            t[i] = new Thread(searchers[i]);
            t[i].start();
        }

        synchronized (t) {
            try {
                for (int i=0; i<threads; i++) {
                    t[i].join();
                }
            } catch (InterruptedException e) {
                return;
            }
            System.out.println("This run's global score: " + bestScore.get(0));
            if (score > bestScore.get(0))
                score = bestScore.get(0);
            path = bestPath.get(0);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    public String getScore() {
        String formattedScore = String.format("%.5f", score);   // Rounds to 5 decimal places
        return formattedScore;
    }

    public double getRealScore() {
        return score;
    }

    public String getPath2() {
        String newStr = "";
        for (int i=0; i<path.length; i++) {
            newStr += (Integer.toString(path[i]));
            newStr += (", ");
        }
        System.out.println(newStr);
        return newStr;
    }

    public int[] getPath() {
        return path;
    }
}
