import static java.lang.Math.sqrt;
/**
 * Author's Name: ThaiBinh Nguyen
 *
 * Purpose of this class is to turn raw coordinate data into a maximally connected graph.
 */
public class InputToGraph {
    private double[][] rawData;
    private double[][] adjacencyMatrix;

    public InputToGraph() {

    }

    public void createGraph(double[][] inputData) {
        rawData = inputData;
        adjacencyMatrix = new double[inputData.length][inputData.length];
        double currentX, currentY, otherX, otherY;

        for (int i=0; i<rawData.length; i++) {
            currentX = rawData[i][0];
            currentY = rawData[i][1];

            for (int j=0; j<rawData.length; j++) {      // Finds distance between this vertex and ALL OTHERS
                otherX = rawData[j][0];
                otherY = rawData[j][1];
                adjacencyMatrix[i][j] = findDistance(currentX, currentY, otherX, otherY);
            }
        }
    }

    // Helper function to find distance
    private double findDistance(double x1, double y1, double x2, double y2) {
        return sqrt((x1-x2) * (x1-x2) + (y1-y2) * (y1-y2));
    }

    public void printGraph() {
        for (int i=0; i< adjacencyMatrix.length; i++) {
            for (int j=0; j<adjacencyMatrix.length; j++) {
                System.out.print(adjacencyMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public double[][] getGraph() {
        return adjacencyMatrix;
    }
}
