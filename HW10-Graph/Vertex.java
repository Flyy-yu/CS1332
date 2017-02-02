/**
 * Class representing a vertex.
 * 
 * DO NOT EDIT THIS CLASS!!!
 * 
 * @author CS 1332 TAs
 * @version 1.0
 */
public final class Vertex<T> {

    private final T data;

    /**
     * Creates a Vertex object holding the given data.
     *
     * @param data the object that is stored in this Vertex
     */
    public Vertex(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Vertex) {
            return data.equals(((Vertex<?>) o).data);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    /**
     * Gets the data in this Vertex.
     * 
     * @return the data in this Vertex
     */
    public T getData() {
        return data;
    }
    
    @Override
    public String toString() {
        return data.toString();
    }

}
