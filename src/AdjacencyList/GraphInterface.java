package AdjacencyList;
import java.util.*;
/**
 * Author's Name: ThaiBinh Nguyen
 *
 * Interface with functions for a general graph. May be implemented with an adjacency list, adjacency matrix, or any
 * other implementations of graphs.
 */
public interface GraphInterface<E> {
    public void addVertex(E v);

    public void addEdge(E v1, E v2, int weight);

    public boolean hasEdge(E v1, E v2);

    public Edge<E> getEdge(E v1, E v2);

    public boolean hasPath(E v1, E v2);

    public List<E> getDFSPath(E v1, E v2);

    public String toString();
}
