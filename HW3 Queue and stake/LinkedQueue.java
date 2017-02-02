/**
 * Your implementation of a linked queue.
 *
 * @author peiyu wang
 * @version 1.0
 */
public class LinkedQueue<T> implements QueueInterface<T> {

    // Do not add new instance variables.
    private LinkedNode<T> head;
    private LinkedNode<T> tail;
    private int size;

    @
    Override
    public T dequeue() {
        if (size == 0) {
            throw new java.util.NoSuchElementException("Error, queue is empty");
        }
        LinkedNode<T> temp = head;
        if (head.getNext() != null) {
            head = head.getNext();

        } else {
            head = null;
            tail = null;
        }
        size--;
        return temp.getData();
    }

    @
    Override
    public void enqueue(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }
        LinkedNode<T> temp = new LinkedNode(data);
        if (tail == null) {
            head = temp;
            tail = temp;
        } else {
            tail.setNext(temp);
            tail = temp;
        }
        size++;
    }

    @
    Override
    public boolean isEmpty() {
        return size == 0;
    }

    @
    Override
    public int size() {
        return size;
    }

    /**
     * Returns the head of this queue.
     * Normally, you would not do this, but we need it for grading your work.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return the head node
     */
    public LinkedNode<T> getHead() {
        // DO NOT MODIFY!
        return head;
    }

    /**
     * Returns the tail of this queue.
     * Normally, you would not do this, but we need it for grading your work.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return the tail node
     */
    public LinkedNode<T> getTail() {
        // DO NOT MODIFY!
        return tail;
    }
}