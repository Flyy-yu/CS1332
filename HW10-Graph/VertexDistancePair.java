/**
 * Class to store a vertex in a graph and an integer associated with it
 * representing the distance to this vertex from the previous vertex in an
 * adjacency list.
 *
 * DO NOT EDIT THIS CLASS!!!
 *
 * @author CS 1332 TAs
 * @version 1.0
 */
public final class VertexDistancePair<T>
        implements Comparable<VertexDistancePair<? super T>> {

    private final Vertex<T> vertex;
    private final int distance;

    /**
     * Creates a VertexDistancePair.
     *
     * @param vertex the Vertex to be stored.
     * @param distance the integer representing the distance to this Vertex
     *        from the previous Vertex.
     */
    public VertexDistancePair(Vertex<T> vertex, int distance) {
        this.vertex = vertex;
        this.distance = distance;
    }

    /**
     * Gets the Vertex stored in this VertexDistancePair.
     *
     * @return the Vertex stored in this VertexDistancePair.
     */
    public Vertex<T> getVertex() {
        return vertex;
    }

    /**
     * Gets the distance stored in this VertexDistancePair.
     *
     * @return the distance stored in this VertexDistancePair.
     */
    public int getDistance() {
        return distance;
    }

    @Override
    public int compareTo(VertexDistancePair<? super T> pair) {
        return this.getDistance() - pair.getDistance();
    }

    @Override
    public String toString() {
        return "Pair with vertex " + vertex + " and distance " + distance;
    }
}
