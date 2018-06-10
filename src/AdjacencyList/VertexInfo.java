package AdjacencyList;
/**
 * Author's Name: ThaiBinh Nguyen
 *
 * Edge data for an edge (connecting between 2 vertices) on an adjacency list graph.
 */
public class VertexInfo<E> {
    /** The vertex itself. */
    public E v;

    /** A mark for whether this vertex has been visited.  Useful for path searching. */
    public boolean visited;

    /** Constructs information for the given vertex. */
    public VertexInfo(E v) {
        this.v = v;
        this.clear();
    }

    /** Resets the visited field. */
    public void clear() {
        this.visited = false;
    }
}
