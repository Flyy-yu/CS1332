/**
 * Class representing an edge between two vertices.
 * 
 * DO NOT EDIT THIS CLASS!!!
 * 
 * @author CS 1332 TAs
 * @version 1.0
 */
public final class Edge<T> implements Comparable<Edge<? super T>> {

    private final Vertex<T> u;
    private final Vertex<T> v;
    private final int weight;
    private final boolean directed;

    /**
     * Creates an Edge between two given vertices with a given weight.  If the
     * edge is specified to be a directed edge, then u is the starting vertex
     * and v is the ending vertex.
     *
     * @param u one of the vertices at the end of the Edge, is the start
     *        vertex if the Edge is specified to be directed
     * @param v one of the vertices at the end of the Edge, is the end
     *        vertex if the Edge is specified to be directed
     * @param weight the weight of the Edge
     * @param directed whether or not the edge is directed
     */
    public Edge(Vertex<T> u, Vertex<T> v, int weight, boolean directed) {
        this.u = u;
        this.v = v;
        this.weight = weight;
        this.directed = directed;
    }

    @Override
    public int hashCode() {
        return (u == null ? 0 : u.hashCode())
                ^ (v == null ? 0 : v.hashCode())
                ^ weight
                ^ (directed ? 1 : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Edge) {
            Edge<?> e = (Edge<?>) o;
            if (directed ^ e.directed) {
                return false;
            }
            if (directed) {
                return weight == e.weight
                        && u.equals(e.u) && v.equals(e.v);
            }
            return weight == e.weight
                    && ((u.equals(e.u) && v.equals(e.v))  
                            || (u.equals(e.v) && v.equals(e.u)));
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Edge<? super T> e) {
        return weight - e.getWeight();
    }

    /**
     * Gets the weight of this Edge.
     * 
     * @return the weight of this Edge
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Gets the u Vertex of this Edge, which is the starting Vertex if the 
     * Edge is directed.
     * 
     * @return the u Vertex of this Edge
     */
    public Vertex<T> getU() {
        return u;
    }

    /**
     * Gets the v Vertex of this Edge, which is the ending Vertex if the 
     * Edge is directed.
     * 
     * @return the v Vertex of this Edge
     */
    public Vertex<T> getV() {
        return v;
    }
    
    /**
     * Gets whether or not this Edge is directed.
     * 
     * @return true if this Edge is directed, false otherwise
     */
    public boolean isDirected() {
        return directed;
    }
    
    @Override
    public String toString() {
        if (directed) {
            return "Edge from " + u + " to " + v + " with weight " + weight;
        }
        return "Edge between " + u + " and " + v + " with weight " + weight;
    }
}
