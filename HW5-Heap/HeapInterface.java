/**
 * The interface describing the methods you will implement for your heap.
 *
 * @author CS 1332 TAs
 * @version 1.0
 */
public interface HeapInterface<T extends Comparable<? super T>> {

    public static final int STARTING_SIZE = 10;

    /**
     * Adds an item to the heap. If the backing array is full and you're trying
     * to add a new item, then increase its size by 1.5 times, rounding down
     * if necessary. No duplicates will be added.
     *
     * @throws IllegalArgumentException if the item is null
     * @param item the item to be added to the heap
     */
    public void add(T item);

    /**
     * Removes and returns the first item of the heap. Do not decrease the size
     * of the backing array.
     *
     * @throws java.util.NoSuchElementException if the heap is empty
     * @return the item removed
     */
    public T remove();

    /**
     * Returns if the heap is empty or not.
     * @return a boolean representing if the heap is empty
     */
    public boolean isEmpty();

    /**
     * Returns the size of the heap.
     * @return the size of the heap
     */
    public int size();

    /**
     * Clears the heap and returns array to starting size.
     */
    public void clear();

    /**
     * Used for grading purposes only.
     *
     * DO NOT USE OR EDIT THIS METHOD!
     *
     * @return the backing array
     */
    public Comparable[] getBackingArray();
}
