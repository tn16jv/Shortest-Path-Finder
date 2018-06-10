import javax.swing.*;
/**
 * Author's Name: ThaiBinh Nguyen
 *
 * This is the main driver class for the simulating the travelling salesman problem with select data.
 */
public class TravelSalesmanTest {
    public TravelSalesmanTest() {
        ReadInputFile reader = new ReadInputFile();
        InputToGraph graphMaker = new InputToGraph();
        SearcherCreator creator = new SearcherCreator();
        JFrame frame = new TravelFrame(reader, graphMaker, creator);
    }

    public static void main(String[] args) {
        new TravelSalesmanTest();
    }
}
