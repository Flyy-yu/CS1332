/**
 * Node class used for implementing your DoublyLinkedList.
 *
 * DO NOT ALTER THIS FILE!!
 *
 * @author CS 1332 TAs
 * @version 1.0
 */
public class LinkedListNode<T> {
    private T data;
    private LinkedListNode<T> previous;
    private LinkedListNode<T> next;

    /**
     * Creates a new LinkedListNode with the given T object and node references.
     *
     * @param data The data stored in the new node.
     * @param previous The previous node in the list.
     * @param next The next node in the list.
     */
    public LinkedListNode(T data, LinkedListNode<T> previous,
            LinkedListNode<T> next) {
        this.data = data;
        this.previous = previous;
        this.next = next;
    }

    /**
     * Creates a new LinkedListNode with only the given T object.
     *
     * @param data The data stored in the new node.
     */
    public LinkedListNode(T data) {
        this(data, null, null);
    }

    /**
     * Gets the data stored in the node.
     *
     * @return The data in this node.
     */
    public T getData() {
        return data;
    }

    /**
     * Gets the next node.
     *
     * @return The next node.
     */
    public LinkedListNode<T> getNext() {
        return next;
    }

    /**
     * Set the next node.
     *
     * @param next The new next node.
     */
    public void setNext(LinkedListNode<T> next) {
        this.next = next;
    }

    /**
     * Gets the previous node.
     *
     * @return The previous node.
     */
    public LinkedListNode<T> getPrevious() {
        return previous;
    }

    /**
     * Set the previous node.
     *
     * @param previous The new previous node.
     */
    public void setPrevious(LinkedListNode<T> previous) {
        this.previous = previous;
    }

    @Override
    public String toString() {
        return "Node containing: " + data;
    }
}
