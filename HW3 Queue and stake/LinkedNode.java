/**
 * Node class used in linked data structure implementations.
 *
 * DO NOT ALTER THIS FILE!!
 *
 * @author CS 1332 TAs
 * @version 1.0
 */
public class LinkedNode<T> {

    private T data;
    private LinkedNode<T> next;

    /**
     * Create a new LinkedNode with the given data object and next node.
     *
     * @param data data to store in the node
     * @param next the next node
     */
    public LinkedNode(T data, LinkedNode<T> next) {
        this.data = data;
        this.next = next;
    }

    /**
     * Create a new LinkedNode with the given data object and no next node.
     *
     * @param data data to store in this node
     */
    public LinkedNode(T data) {
        this(data, null);
    }

    /**
     * Get the data stored in the node.
     *
     * @return data in this node.
     */
    public T getData() {
        return data;
    }

    /**
     * Get the next node.
     *
     * @return next node.
     */
    public LinkedNode<T> getNext() {
        return next;
    }

    /**
     * Set the next node.
     *
     * @param next new next node.
     */
    public void setNext(LinkedNode<T> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node containing: " + data;
    }

}
