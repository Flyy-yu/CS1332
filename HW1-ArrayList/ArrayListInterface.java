/**
 * This interface describes the public methods needed for ArrayList, a generic
 * array-backed list data structure.
 *
 * We've given you the expected Big-O for each method this time around.
 *
 * DO NOT ALTER THIS FILE!!
 *
 * @author CS 1332 TAs
 * @version 1
 */
public interface ArrayListInterface<T> {

    /**
     * The initial capacity of the array list.
     */
    public static final int INITIAL_CAPACITY = 10;

    /**
     * Adds the element to the index specified.
     *
     * Remember that this add may require elements to be shifted.
     * Adding to index {@code size} should be O(1), all other adds are O(n).
     *
     * @param index The index where you want the new element.
     * @param data Any object of type T.
     * @throws java.lang.IndexOutOfBoundsException if index is negative
     * or index > size.
     * @throws java.lang.IllegalArgumentException if data is null.
     */
    public void addAtIndex(int index, T data);

    /**
     * Add the given data to the front of your array list.
     *
     * Remember that this add may require elements to be shifted.
     * Must be O(n).
     *
     * @param data The data to add to the list.
     * @throws java.lang.IllegalArgumentException if data is null.
     */
    public void addToFront(T data);

    /**
     * Add the given data to the back of your array list.
     *
     * Must be O(1).
     *
     * @param data The data to add to the list.
     * @throws java.lang.IllegalArgumentException if data is null.
     */
    public void addToBack(T data);

    /**
     * Returns the element at the given index.
     *
     * Must be O(1).
     *
     * @param index The index of the element
     * @return The data stored at that index.
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or
     * index >= size.
     */
    public T get(int index);

    /**
     * Removes and returns the element at index.
     *
     * Remember that this remove may require elements to be shifted.
     * This method should be O(1) for index {@code size}, and O(n) in all other
     * cases.
     *
     * @param index The index of the element
     * @return The object that was formerly at that index.
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or
     * index >= size.
     */
    public T removeAtIndex(int index);

    /**
     * Remove the first element in the list and return it.
     *
     * If the list is empty, return {@code null}.
     * Remember that this remove may require elements to be shifted.
     * Must be O(n).
     *
     * @return The data from the front of the list or null.
     */
    public T removeFromFront();

    /**
     * Remove the last element in the list and return it.
     *
     * If the list is empty, return {@code null}.
     * Must be O(1).
     *
     * @return The data from the back of the list or null.
     */
    public T removeFromBack();

    /**
     * Return a boolean value representing whether or not the list is empty.
     *
     * Must be O(1).
     *
     * @return true if empty; false otherwise
     */
    public boolean isEmpty();

    /**
     * Return the size of the list as an integer.
     *
     * Must be O(1).
     *
     * @return The size of the list.
     */
    public int size();

    /**
     * Clear the list. Reset the backing array to a new array of the initial
     * capacity.
     *
     * Must be O(1).
     */
    public void clear();

    /**
     * Return the backing array for this list.
     *
     * Must be O(1).
     * For grading purposes only. DO NOT USE THIS METHOD IN YOUR CODE!
     *
     * @return the backing array for this list
     */
    public Object[] getBackingArray();
}
