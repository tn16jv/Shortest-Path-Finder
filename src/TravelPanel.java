import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import java.io.File;
import java.util.NoSuchElementException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
/**
 * Author's Name: ThaiBinh Nguyen
 *
 * Holds all the menu items for a user to specify file path, number of threads, number of searches, and number of
 * iterations. Additionally, this displays the current best score.
 */
class TravelPanel extends JPanel implements ActionListener {
    private JButton coordFile, runSearch;
    private JTextField fileField, bestField, threadsField, searchesField, iterationsField;
    private JLabel fileLabel, bestLabel, threadsLabel, searchesLabel, iterationsLabel;
    private JCheckBox drawVertexBox;
    private ReadInputFile newReader;
    private InputToGraph newGraph;
    private SearcherCreator newCreator;
    private JFrame master;

    protected boolean solutionPresent;
    protected double baseX = 100.0, baseY=100.0, rectangleWidth = 800, rectangleLength = 800, compressionX, compressionY;
    protected double leftX, topY, rightX, bottomY;
    private double[][] coords;
    private int[] path;
    private boolean drawVertices = true;

    public TravelPanel() {} // Default constructor

    public TravelPanel(ReadInputFile refReader, InputToGraph graphMaker, SearcherCreator creator, JFrame aFrame) {
        newReader = refReader;
        newGraph = graphMaker;
        newCreator = creator;
        master = aFrame;
        solutionPresent = false;

        coordFile = new JButton("Choose File");
        coordFile.addActionListener(this);
        runSearch = new JButton("Run Search");
        runSearch.addActionListener(this);

        fileLabel = new JLabel("File Path");
        bestLabel = new JLabel("Current Best");
        threadsLabel = new JLabel("Number of Threads");
        searchesLabel = new JLabel("Number of Searches");
        iterationsLabel = new JLabel("Number of Iterations");

        fileField = new JTextField(20);
        bestField = new JTextField(8);
        bestField.setEditable(false);
        threadsField = new JTextField(8);
        searchesField = new JTextField(8);
        iterationsField = new JTextField(8);
        drawVertexBox = new JCheckBox("Mark vertices(destinations) on the map: ",true);
        drawVertexBox.setHorizontalTextPosition(SwingConstants.LEFT);
        drawVertexBox.addActionListener(this);

        add(coordFile);
        add(fileLabel);
        add(fileField);
        add(runSearch);
        add(bestLabel);
        add(bestField);
        add(threadsLabel);
        add(threadsField);
        add(searchesLabel);
        add(searchesField);
        add(iterationsLabel);
        add(iterationsField);
        add(drawVertexBox);
    }

    // Changes the graph size and where it is drawn based on the master JFrame's current size
    private void resize() {
        rectangleWidth = master.getWidth()*0.75;        // Width and length of the graph is determined by the JFrame
        rectangleLength = master.getHeight()*0.75;
        baseX = master.getWidth() * 0.25 / 2;       // The base X and Y the graph is drawn on is determined by JFrame
        baseY = master.getHeight() * 0.25 / 2;
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
        compressionX = (rectangleWidth - 50) / Math.abs(rightX - leftX);       // Take away 50 to shrink down a little
        compressionY = (rectangleLength - 50) / Math.abs(topY - bottomY);      // 0.2 is the most aesthetic though

        compressionX = (compressionX < compressionY) ? compressionX : compressionY; // These 2 lines not needed
        compressionY = (compressionY < compressionX) ? compressionY : compressionX; // but produces nicer looking graph
    }

    private void drawVertices(Graphics2D g2) {
        if (drawVertices) {
            // First of the vertices visited gets filled in
            g2.fill(new Ellipse2D.Double(Math.abs(coords[path[0]][0] - leftX) * compressionX + baseX - 9,
                    Math.abs(coords[path[0]][1] - topY) * compressionY + baseY - 9, 18, 18));
            for (int i=1; i<path.length; i++)
                    g2.draw(new Ellipse2D.Double(Math.abs(coords[path[i-1]][0] - leftX) * compressionX + baseX - 5,
                            Math.abs(coords[path[i-1]][1] - topY) * compressionY + baseY - 5, 10, 10));
            // Last of the vertices visited gets filled in
            if (drawVertices)
                g2.fill(new Ellipse2D.Double(Math.abs(coords[path[path.length-1]][0] - leftX) * compressionX + baseX - 9,
                        Math.abs(coords[path[path.length-1]][1] - topY) * compressionY + baseY - 9, 18, 18));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        resize();
        g2.draw(new Rectangle2D.Double(baseX, baseY, rectangleWidth, rectangleLength));
        if (solutionPresent) {
            findCompression();
            baseX+=10; baseY+=10;   // Makes sure that the vertices aren't drawn on the border
            drawVertices(g2);
            for (int i=1; i<path.length; i++) {
                // Algorithm: take away starting point, apply compression, then add the lowest from the respective axis
                double x1 = Math.abs(coords[path[i-1]][0] - leftX) * compressionX + baseX;
                double y1 = Math.abs(coords[path[i-1]][1] - topY) * compressionY + baseY;
                double x2 = Math.abs(coords[path[i]][0] - leftX) * compressionX + baseX;
                double y2 = Math.abs(coords[path[i]][1] - topY) * compressionY + baseY;
                g2.draw(new Line2D.Double(x1, y1, x2, y2));
            }
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
                bestField.setText(newCreator.getScore());
                solutionPresent = true;
                coords = newReader.getCoordinates();
                path = newCreator.getPath();
                this.repaint();
            } catch (NumberFormatException | NoSuchElementException e) {
                fileField.setText("Invalid file format");
                return;
            } catch (NullPointerException e) {
                fileField.setText("INVALID");
                return;
            }
        } else if (source == coordFile) {       // Code for a Java file chooser
            solutionPresent = false;
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int returnValue = jfc.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {   // Once user selects and approves the file
                File selectedFile = jfc.getSelectedFile();
                fileField.setText(selectedFile.getAbsolutePath());
            }
        } else if (source == drawVertexBox) {
            drawVertices = drawVertexBox.isSelected();
            this.repaint();
        }
    }
}
