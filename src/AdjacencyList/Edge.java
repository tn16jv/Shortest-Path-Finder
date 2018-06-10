package AdjacencyList;

/**
 * Author's Name: ThaiBinh Nguyen
 *
 * Edge data for an edge (connecting between 2 vertices) on an adjacency list graph.
 */
public class Edge<E> {
    public E from, to;
    public int weight;

    public Edge(E from, E to, int weight) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("null");
        }
        this.from = from;
        this.to = to;
        this.weight = weight;
    }


    public String toString() {
        return "<" + from + ", " + to + ", " + weight + ">";
    }
}
