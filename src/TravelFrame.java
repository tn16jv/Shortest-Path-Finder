import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Author's Name: ThaiBinh Nguyen
 *
 * This window frame will hold the contents necessary for a user to provide data for a travelling salesman problem.
 */
public class TravelFrame extends JFrame {
    public TravelFrame(ReadInputFile newReader, InputToGraph graphMaker, SearcherCreator creator) {
        setTitle("Travelling Salesman Problem Solver");
        setSize(1200, 1000);
        setMinimumSize(new Dimension(900, 600));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
            }
        });
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();

        this.setJMenuBar(new TopMenu(this));

        TravelMousePanel panel = new TravelMousePanel(newReader, graphMaker, creator, this);
        contentPane.add(panel);
        this.setVisible(true);
    }
}
