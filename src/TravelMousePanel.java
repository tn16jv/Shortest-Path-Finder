import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
/**
 * Name: ThaiBinh Nguyen
 *
 * Has all the functionality of TravelPanel, with the GUI elements, with the addition of a mouse motion listener.
 */
public class TravelMousePanel extends TravelPanel implements MouseMotionListener {
    private JLabel xyLabel;

    public TravelMousePanel(ReadInputFile newReader, InputToGraph graphMaker, SearcherCreator creator) {
        super(newReader, graphMaker, creator);
        addMouseMotionListener(this);

        xyLabel = new JLabel("X:  Y:");
        xyLabel.setBorder(BorderFactory.createEmptyBorder(10, 2000, 10, 2000));
        this.add(xyLabel);
    }

    public void mouseMoved(MouseEvent e) {
        saySomething("Moved", e);
    }

    public void mouseDragged(MouseEvent e) {
        saySomething("Dragged", e);
    }

    private void saySomething(String message, MouseEvent e) {
        if (e.getX() >= baseX-10 && e.getX() <= (baseX+graphWidth) &&  // If a solution exists and mouse is in the graph
                e.getY() >= baseY-10 && e.getY() <= (baseY+graphHeight) && solutionPresent) {
            xyLabel.setText(relativeMousePosition(e.getX(), e.getY()));
        } else {    // When there is no solution or the mouse is outside of the graph
            xyLabel.setText("X:  Y:");
        }
    }

    /*
    Gives the x,y position that the mouse would be on if it was on the graph with all the coordinates. Accounts for
    where the graph is drawn, along with inverting the compression ratio that was used to draw the graph.
     */
    private String relativeMousePosition(int x, int y) {
        double relativeX = round((x - baseX) * (1/compressionX) + leftX, 5);
        double relativeY = round((y - baseY) * (1/compressionY) + topY, 5);
        return "X: " + relativeX + "  Y: " + relativeY;
    }

    // Rounds the given double num to the amount of decimal places specified. It is truncated though.
    private double round(double num, int places) {
        double scale = Math.pow(10, places);
        return Math.round(num * scale) / scale;
    }
}
