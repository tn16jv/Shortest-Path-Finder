import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * Author's Name: ThaiBinh Nguyen
 *
 * This class reads in coordinate data from a user specified .txt file, to be used to create a graph. However, this
 * class will only read the data into a multidimensional array.
 */
public class ReadInputFile {
    private String filePath;
    private Scanner in;
    private double [][] rawCoordinates;

    public ReadInputFile() {

    }

    /*
    This reads a file with rows having one X coordinate and one Y coordinate separated by spaces or commas.
     */
    public void readFile(String filePath) throws FileNotFoundException{
        File file = new File(filePath);
        ArrayList<Double> xCoords = new ArrayList<Double>();
        ArrayList<Double> yCoords = new ArrayList<Double>();

        in = new Scanner(file);
        in.useDelimiter("(\\p{javaWhitespace}|,)+");    //Regular expression(regex) for whitespace or comma
        while (in.hasNext()) {
            xCoords.add(in.nextDouble());
            yCoords.add(in.nextDouble());
        }
        // If there are less than 3 coordinates, there is no optimal path. Also problem if not same amount of X and Y
        if (xCoords.size() < 3 || yCoords.size() < 3 || xCoords.size()!=yCoords.size())
            throw new NegativeArraySizeException();

        rawCoordinates = new double[xCoords.size()][2];
        for (int i=0; i<xCoords.size(); i++) {
            rawCoordinates[i][0] = xCoords.get(i);
            rawCoordinates[i][1] = yCoords.get(i);
        }
    }

    /*
    This reads a file with the number of vertices as the first line, and has the vertex number on each row. Each row
    should have an X and Y coordinate.
     */
    public void readFile2(String filePath) {
        File file = new File(filePath);
        try {
            in = new Scanner(file);
            int vertexCount = in.nextInt();
            rawCoordinates = new double[vertexCount][2];    // Initialize coordinates (2 x VertexCount)

            for (int i=0; i<vertexCount; i++) {         // Simple Scanner read in. First value is x, second is y
                in.next();
                rawCoordinates[i][0] = in.nextDouble();
                rawCoordinates[i][1] = in.nextDouble();
            }
        } catch (FileNotFoundException e) {
            return;
        }
    }

    public double[][] getCoordinates() {
        return rawCoordinates;
    }

    // Should only be called after rawCoordinates has been initialized by either readFile methods
    private void coordinatesToFile(String name) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(name)); // BufferedReader uses FileWriter
            int i;
            for (i=0; i<rawCoordinates.length; i++) {
                writer.append(rawCoordinates[i][0] + " " + rawCoordinates[i][1]);
                if (i < rawCoordinates.length-1)    // Only adds a new line if it's not the last row.
                    writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            return;
        }
    }

    public void displayAll() {
        for (int i=0; i<rawCoordinates.length; i++) {
            for (int j=0; j<rawCoordinates[i].length; j++) {
                System.out.print(rawCoordinates[i][j] + " ");
            }
            System.out.println();
        }
    }
}
