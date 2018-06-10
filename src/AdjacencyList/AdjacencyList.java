package AdjacencyList;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
/**
 * Author's Name: ThaiBinh Nguyen
 *
 * Implementation for an adjacency-list representation of graphs.
 */
public class AdjacencyList<E> implements GraphInterface<E> {
    private Map<E, List<Edge<E>>> adjacencyList;
    private Map<E, VertexInfo<E>> vertexInfo;

    public AdjacencyList() {
        this.adjacencyList = new HashMap<E, List<Edge<E>>>();
        this.vertexInfo = new HashMap<E, VertexInfo<E>>();
    }

    public void addVertex(E v) {
        if (v == null) {
            throw new IllegalArgumentException("null");
        }

        adjacencyList.put(v, new ArrayList<Edge<E>>());
        vertexInfo.put(v, new VertexInfo<E>(v));
    }

    public void addEdge(E from, E to, int weight) {
        List<Edge<E>> edgeList = adjacencyList.get(from);
        if (edgeList == null) {
            throw new IllegalArgumentException("source vertex missing");
        }

        Edge<E> newEdge = new Edge<E>(from, to, weight);
        edgeList.add(newEdge);
    }

    public Edge<E> getEdge(E from, E to) {
        List<Edge<E>> edgeList = adjacencyList.get(from);
        if (edgeList == null) {
            throw new IllegalArgumentException("source vertex missing");
        }

        for (Edge<E> e : edgeList) {
            if (e.to.equals(to)) {
                return e;
            }
        }
        return null;
    }

    public boolean hasEdge(E from, E to) {
        return getEdge(from, to) != null;
    }

    public List<E> getDFSPath(E v1, E v2) {
        clearVertexInfo();

        List<E> path = new ArrayList<E>();
        getDFSPath(v1, v2, path);

        if (path.isEmpty()) {
            return null;
        } else {
            return path;
        }
    }

    private List<E> getDFSPath(E v1, E v2, List<E> path) {
        path.add(v1);
        VertexInfo<E> vInfo = vertexInfo.get(v1);
        vInfo.visited = true;

        if (v1.equals(v2)) {
            return path;
        }
        List<Edge<E>> edges = this.adjacencyList.get(v1);
        for (Edge<E> e : edges) {
            VertexInfo<E> vInfo2 = vertexInfo.get(e.to);
            if (!vInfo2.visited) {
                getDFSPath(e.to, v2, path);
                if (path.get(path.size() - 1).equals(v2)) {
                    return path;
                }
            }
        }
        path.remove(v1);
        return path;
    }

    public boolean hasPath(E v1, E v2) {
        return getDFSPath(v1, v2) != null;
    }

    public String toString() {
        Set<E> keys = adjacencyList.keySet();
        String toReturn = "";

        for (E v : keys) {
            toReturn += v + ": ";

            List<Edge<E>> edgeList = adjacencyList.get(v);

            for (Edge<E> edge : edgeList) {
                toReturn += edge + " ";
            }
            toReturn += "\n";
        }
        return toReturn;
    }

    protected final void clearVertexInfo() {
        for (VertexInfo<E> info : this.vertexInfo.values()) {
            info.clear();
        }
    }

}
