import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopMenu extends JMenuBar implements ActionListener {
    private JMenu help;
    private JMenuItem guide, about;
    private JFrame masterFrame;

    public TopMenu(JFrame aframe) {
        super();        // Calls the parent (JMenuBar) constructor

        help = new JMenu("Help");
        guide = new JMenuItem("Guide");
        about = new JMenuItem("About");
        guide.addActionListener(this);
        about.addActionListener(this);
        help.add(guide);
        help.add(about);
        this.add(help);

        masterFrame = aframe;       // The JFrame that will be containing this was passed in as an argument
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if (source == guide) {
            JOptionPane.showMessageDialog(masterFrame,      // JOptionPane is a simple popup box
                    "Choose a file that contains several X Y coordinates, X first followed by Y separated by a\n" +
                            "space or comma. Then set the configuration and press the 'Run Search' button to find the\n" +
                            "APPROXIMATE shortest path that traverses it all (Travelling Salesman Problem).\n\n" +
                            "Look at the example52.txt from the GitHub repository for an example of such a text file.",
                    "Guide",
                    JOptionPane.PLAIN_MESSAGE);
        } else if (source == about) {
            JOptionPane.showMessageDialog(masterFrame,
                    "<html><body><div width='200px' align='center'>Author: ThaiBinh Nguyen<br/>" +
                            "Created: 2018</div></body></html>",        // Used HTML injection to center this message
                    "About",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }
}
