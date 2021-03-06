import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import javax.swing.JFileChooser;
/**
 * Author's Name: ThaiBinh Nguyen
 *
 * Holds all the menu items for a user to specify file path, number of threads, number of searches, and number of
 * iterations. Additionally, this displays the current best score.
 */
class TravelPanel extends JPanel implements ActionListener {
    private JButton coordFile, runSearch;
    private JTextField fileField, bestField, currentBestField, threadsField, searchesField, iterationsField;
    private JLabel fileLabel, bestLabel, currentBestLabel, threadsLabel, searchesLabel, iterationsLabel;
    private JCheckBox drawVertexBox;
    private ReadInputFile newReader;
    private InputToGraph newGraph;
    private SearcherCreator newCreator;

    protected boolean solutionPresent;
    protected int markerSize;
    protected double baseX, baseY, graphWidth, graphHeight, compressionX, compressionY;
    protected double leftX, topY, rightX, bottomY;
    private double[][] coords;
    private int[] path;
    private boolean drawVertices = true;

    public TravelPanel() {} // Default constructor

    public TravelPanel(ReadInputFile refReader, InputToGraph graphMaker, SearcherCreator creator) {
        newReader = refReader;
        newGraph = graphMaker;
        newCreator = creator;
        solutionPresent = false;
        markerSize = 10;

        coordFile = new JButton("Choose File");
        coordFile.addActionListener(this);
        runSearch = new JButton("Run Search");
        runSearch.addActionListener(this);

        fileLabel = new JLabel("File Path");
        threadsLabel = new JLabel("Number of Threads");
        searchesLabel = new JLabel("Number of Searches");
        iterationsLabel = new JLabel("Number of Iterations");
        bestLabel = new JLabel("Overall Best");
        currentBestLabel = new JLabel("Current Best");

        fileField = new JTextField(20);
        threadsField = new JTextField(8);
        searchesField = new JTextField(8);
        iterationsField = new JTextField(8);
        bestField = new JTextField(8);
        bestField.setEditable(false);
        currentBestField = new JTextField(8);
        currentBestField.setEditable(false);
        drawVertexBox = new JCheckBox("Mark vertices (destinations) on the map: ",true);
        drawVertexBox.setHorizontalTextPosition(SwingConstants.LEFT);
        drawVertexBox.addActionListener(this);

        add(coordFile);
        add(fileLabel);
        add(fileField);
        add(runSearch);
        add(threadsLabel);
        add(threadsField);
        add(searchesLabel);
        add(searchesField);
        add(iterationsLabel);
        add(iterationsField);
        add(bestLabel);
        add(bestField);
        add(currentBestLabel);
        add(currentBestField);
        add(drawVertexBox);
    }

    // Changes the graph size and where it is drawn based on the JPanel's size, which is determined by the master JFrame
    private void resize() {
        graphWidth = this.getWidth() - 20 * 2;        // Width and length of the graph is determined by the JPanel
        graphHeight = this.getHeight() - (100 + 20);
        baseX = 20;         // The starting X should always be the same
        baseY = 100;        // The starting Y is always just under all the GUI elements
    }

    // Finds the ratio for X and Y in order to keep every drawn path in the rectangle box
    protected void findCompression() {
        leftX = Integer.MAX_VALUE;
        topY = Integer.MAX_VALUE;
        rightX = Integer.MIN_VALUE;
        bottomY = Integer.MIN_VALUE;
        for (int i=0; i<coords.length; i++) {
            if (leftX > coords[i][0])   // lefX becomes the lowest X each iteration
                leftX = coords[i][0];
            if (topY > coords[i][1])    // topY becomes the lowest Y each iteration
                topY = coords[i][1];
            if (rightX < coords[i][0])  // rightX becomes the highest X each iteration
                rightX = coords[i][0];
            if (bottomY < coords[i][1]) // bottomY becomes the highest Y each iteration
                bottomY = coords[i][1];
        }
        compressionX = (graphWidth - 20) / Math.abs(rightX - leftX);       // Take away 20 to shrink down a little
        compressionY = (graphHeight - 20) / Math.abs(topY - bottomY);      // do not want to be drawn on the border

        compressionX = (compressionX < compressionY) ? compressionX : compressionY; // Sets the compression ratios to
        compressionY = (compressionY < compressionX) ? compressionY : compressionX; // the lowest one out of X and Y
    }

    private void drawVertices(Graphics2D g2) {
        if (drawVertices) {
            // First of the vertices visited gets filled in
            g2.setColor(Color.GREEN);
            g2.fill(new Ellipse2D.Double(Math.abs(coords[path[0]][0] - leftX) * compressionX + baseX - markerSize,
                    Math.abs(coords[path[0]][1] - topY) * compressionY + baseY - markerSize,
                    markerSize*2, markerSize*2));
            // Last of the vertices visited gets filled in
            g2.setColor(Color.RED);
            g2.fill(new Ellipse2D.Double(Math.abs(coords[path[path.length-1]][0] - leftX) * compressionX + baseX - markerSize,
                    Math.abs(coords[path[path.length-1]][1] - topY) * compressionY + baseY - markerSize,
                    markerSize*2, markerSize*2));
            // The rest of the vertices (if any) are filled in
            g2.setColor(Color.BLACK);
            for (int i=1; i<path.length-1; i++)
                    g2.draw(new Ellipse2D.Double(Math.abs(coords[path[i]][0] - leftX) * compressionX + baseX - markerSize/2,
                            Math.abs(coords[path[i]][1] - topY) * compressionY + baseY - markerSize/2,
                            markerSize, markerSize));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        resize();
        g2.draw(new Rectangle2D.Double(baseX, baseY, graphWidth, graphHeight));
        if (solutionPresent) {
            findCompression();
            baseX+=markerSize; baseY+=markerSize;   // Makes sure that the vertices markers aren't drawn on the border
            drawVertices(g2);
            for (int i=1; i<path.length; i++) {
                // Algorithm: take away starting point, apply compression, then add the lowest from the respective axis
                double x1 = Math.abs(coords[path[i-1]][0] - leftX) * compressionX + baseX;
                double y1 = Math.abs(coords[path[i-1]][1] - topY) * compressionY + baseY;
                double x2 = Math.abs(coords[path[i]][0] - leftX) * compressionX + baseX;
                double y2 = Math.abs(coords[path[i]][1] - topY) * compressionY + baseY;
                g2.draw(new Line2D.Double(x1, y1, x2, y2));
            }
            baseX-=markerSize; baseY-=markerSize;   // Resets the baseX and baseY after vertices drawn
        }
    }

    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if (source == runSearch) {
            try {
                String filePath = fileField.getText();
                newReader.readFile(filePath);
                newGraph.createGraph(newReader.getCoordinates());   // Read in coordinates from the file

                int threadCount = Integer.parseInt(threadsField.getText());
                int searchCount = Integer.parseInt(searchesField.getText());
                int iterationsCount = Integer.parseInt(iterationsField.getText());  // Getting info from the JTextFields

                newCreator.newSearch(threadCount, searchCount, iterationsCount,
                                newGraph.getGraph(), newReader.getCoordinates());   // Create the multithreaded searches
                bestField.setText(newCreator.getBestScore());
                currentBestField.setText(newCreator.getCurrentScore());
                solutionPresent = true;
                coords = newReader.getCoordinates();
                path = newCreator.getPath();
                this.repaint();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid input (must be numbers)", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FileNotFoundException | NoSuchElementException e) {
                JOptionPane.showMessageDialog(null,
                        "File not found or file format is invalid. Consult Help/Guide", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NegativeArraySizeException e) {
                JOptionPane.showMessageDialog(null,
                        "List of coordinates is too small (less than 3)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (source == coordFile) {       // Code for a Java file chooser
            JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));     // Current working directory
            int returnValue = jfc.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {   // Once user selects and approves the file
                File selectedFile = jfc.getSelectedFile();
                fileField.setText(selectedFile.getAbsolutePath());
            }
        } else if (source == drawVertexBox) {   // Code for checkbox specifying whether to mark the vertices
            drawVertices = drawVertexBox.isSelected();
            this.repaint();
        }
    }
}
