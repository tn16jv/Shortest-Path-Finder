import java.util.Random;
import java.util.ArrayList;
import java.util.concurrent.*;
import static java.lang.Math.sqrt;
/**
 * Author's Name: ThaiBinh Nguyen
 *
 * Implementation of a genetic algorithm for approximating a solution to the Travelling Salesman Problem.
 * Random paths are generated and the best one (shortest length) is kept at the end of each search.
 */
public class GraphSearcher implements Runnable {
    Semaphore globalAccess;
    private double[][] graph;
    private double[][] coordinates;
    private int iteration;
    private int searchCount;
    private double localBest;
    private ArrayList<Double> global;
    private ArrayList<int[]> globalPath;

    public GraphSearcher(Semaphore newSema, ArrayList globalScore, ArrayList<int[]> bestPath, int iterations,
                         int searches, double[][] newArr, double[][] coords) {
        globalAccess = newSema;
        global = globalScore;
        globalPath = bestPath;
        iteration = iterations;
        searchCount = searches;
        graph = newArr;
        coordinates = coords;
        localBest = Double.MAX_VALUE;
    }

    // Represents the mutating genetic algorithm for solving a Travelling Salesman problem
    private void findPath(int iterations) throws InterruptedException {
        double length = Integer.MAX_VALUE;          // Set internal length arbitrarily high
        int[] path = randomPath(graph.length);      // Generate random path to begin
        for (int i = 0; i < searchCount; i++) {
            int[] mutatedPath;
            for (int j = 0; j < iterations; j++) {      // Mutation loops
                mutatedPath = path.clone();
                mutate(mutatedPath);                    // Path mutation
                if (pathLength(mutatedPath) < pathLength(path))     // Inherit mutation or discard based on improvement
                    path = mutatedPath.clone();
            }

            if (pathLength(path) < length) {
                length = pathLength(path);
                localBest = length;
                updateGlobal(path);
            }
            path = randomPath(graph.length);    // Generate new random path
        }
        System.out.println("Local Best length: " + length);
    }

    // Method to check the global best and its path. Will update if this current path is better
    private void updateGlobal(int[] path) throws InterruptedException{
        globalAccess.acquire();         // Enter critical region
        synchronized(global) {
            synchronized(globalPath) {
                if (localBest < global.get(0)) {
                    global.set(0, localBest);
                    globalPath.set(0, path);
                }
            }
        }
        globalAccess.release();         // Signal to other threads the release of semaphore
    }

    private double pathLength(int[] path) {
        double length = 0.0;
        for (int i=1; i<path.length; i++) {
            length += findDistance(coordinates[path[i-1]][0], coordinates[path[i-1]][1],
                                    coordinates[path[i]][0], coordinates[path[i]][1]);
        }
        return length;
    }

    // Helper function for pathLength
    private static double findDistance(double x1, double y1, double x2, double y2) {
        return sqrt((x1-x2) * (x1-x2) + (y1-y2) * (y1-y2));
    }

    private void mutate(int[] arr) {
        int random[] = new int[3];
        for (int i=0; i<3; i++)
            random[i] = (int) (Math.random() * (arr.length));

        while (random[0]==random[1]) {              // Keep picking a random second vertex if it is equal to first
            random[1] = (int) (Math.random() * (arr.length));
        }
        while (random[0]==random[2] || random[1] == random[2]) { // Pick new third vertex if equal to either 1st or 2nd
            random[2] = (int) (Math.random() * (arr.length));
        }

        /*
        int tmp = arr[random[0]];                   // Keep first element
        for (int i=0; i<random.length-1; i++) {     // Shifts each vertex over one and ends before last one
            arr[random[i]] = arr[random[i+1]];
        }
        arr[random[random.length-1]] = tmp;        // Swap last vertex with first vertex
        */
        int[] old = random.clone();
        randomPermutate(random);
        for (int i=0; i<arr.length; i++) {
            if (arr[i] == old[0])
                arr[i] = random[0];
            else if (arr[i] == old[1])
                arr[i] = random[1];
            else if (arr[i] == old[2])
                arr[i] = random[2];
        }
    }

    private static void randomPermutate(int[] ar) { // Fisher–Yates shuffle
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    // Performs an array shuffling
    private static void shuffle(int[] arr) {
        for (int i =0; i<arr.length; i++) {
            Random rand = new Random(System.currentTimeMillis()*(1 + i));
            int r = (int) (rand.nextInt(i+1));
            int tmp = arr[r];
            arr[r] = arr[i];
            arr[i] = tmp;
        }
    }

    private static int[] randomPath(int n) {
        int[] newPath = new int[n];
        for (int i=0; i<n; i++) {
            newPath[i] = i;
        }
        shuffle(newPath);
        return newPath;
    }

    @Override
    public void run() {
        synchronized(this) {
            try {
                findPath(iteration);                        // Calls the algorithm
            } catch (InterruptedException e) {             // Need to catch the thread's interruption
                Thread.currentThread().interrupt();
                return;
            }
            //notify();
        }
    }
}
