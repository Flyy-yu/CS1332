/**
 * The interface describing the methods you will implement for your
 * priority queue.
 *
 * @author CS 1332 TAs
 * @version 1.0
 */
public interface PriorityQueueInterface<T extends Comparable<? super T>> {

    /**
     * Adds an item to the priority queue.
     *
     * @param item the item to be added
     * @throws IllegalArgumentException if the item is null
     */
    public void enqueue(T item);
        
    /**
     * Removes and returns the first item in the priority queue.
     *
     * @throws java.util.NoSuchElementException if the priority queue is empty
     * @return the item to be dequeued
     */
    public T dequeue();

    /**
     * Returns if the priority queue is empty.
     * @return a boolean representing if the priority queue is empty
     */
    public boolean isEmpty();

    /**
     * Returns the size of the priority queue.
     * @return the size of the priority queue
     */
    public int size();

    /**
     * Clears the priority queue.
     */
    public void clear();

    /**
     * Used for grading purposes only.
     *
     * DO NOT USE OR EDIT THIS METHOD!
     *
     * @return the backing heap
     */
    public HeapInterface<T> getBackingHeap();
}
