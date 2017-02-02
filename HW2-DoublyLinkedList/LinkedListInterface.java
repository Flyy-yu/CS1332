/**
 * This interface describes the public methods needed for DoublyLinkedList,
 * which should be doubly-linked and should have head and tail pointers.
 *
 * The expected Big-O for each method has been given to you.
 *
 * DO NOT ALTER THIS FILE!!
 *
 * @author CS 1332 TAs
 * @version 1.0
 */
public interface LinkedListInterface<T> {

    /**
     * Adds the element to the index specified.
     *
     * Adding to indices 0 and {@code size} should be O(1), all other cases are
     * O(n).
     *
     * @param index The requested index for the new element.
     * @param data The data for the new element.
     * @throws java.lang.IndexOutOfBoundsException if index is negative or
     * index > size.
     * @throws java.lang.IllegalArgumentException if data is null.
     */
    public void addAtIndex(int index, T data);

    /**
     * Adds the element to the front of the list. Make sure to update head.
     *
     * Must be O(1) for all cases.
     *
     * @param data The data for the new element.
     * @throws java.lang.IllegalArgumentException if data is null.
     */
    public void addToFront(T data);

    /**
     * Adds the element to the back of the list. Make sure to update tail.
     *
     * Must be O(1) for all cases.
     *
     * @param data The data for the new element.
     * @throws java.lang.IllegalArgumentException if data is null.
     */
    public void addToBack(T data);

    /**
     * Removes and returns the element from the index specified.
     *
     * Removing from indices 0 and size - 1 should be O(1), all other cases are
     * O(n).
     *
     * @param index The requested index to be removed.
     * @return The object formerly located at index.
     * @throws java.lang.IndexOutOfBoundsException if index is negative or
     * index >= size.
     */
    public T removeAtIndex(int index);

    /**
     * Removes and returns the element at the front of the list. If the list is
     * empty, return {@code null}.
     *
     * Must be O(1) for all cases.
     *
     * @return The object formerly located at the front.
     */
    public T removeFromFront();

    /**
     * Removes and returns the element at the back of the list. If the list is
     * empty, return {@code null}.
     *
     * Must be O(1) for all cases.
     *
     * @return The object formerly located at the back.
     */
    public T removeFromBack();

    /**
     * Removes every copy of the given data from the list.
     *
     * Must be O(n) for all cases.
     *
     * @param data The data to be removed from the list.
     * @return true if something was removed from the list; false otherwise.
     * @throws java.lang.IllegalArgumentException if data is null.
     */
    public boolean removeAllOccurrences(T data);

    /**
     * Returns the element at the specified index.
     *
     * Getting indices 0 and size - 1 should be O(1), all other cases are O(n).
     *
     * @param index The index of the requested element.
     * @return The object stored at index.
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or
     * index >= size.
     */
    public T get(int index);

    /**
     * Returns an array representation of the linked list.
     *
     * Must be O(n) for all cases.
     *
     * @return An array of length {@code size} holding all of the objects in
     * this list in the same order.
     */
    public Object[] toArray();

    /**
     * Returns a boolean value indicating if the list is empty.
     *
     * Must be O(1) for all cases.
     *
     * @return true if empty; false otherwise.
     */
    public boolean isEmpty();

    /**
     * Returns the number of elements in the list.
     *
     * Must be O(1) for all cases.
     *
     * @return The size of the list.
     */
    public int size();

    /**
     * Clears the list of all data.
     *
     * Must be O(1) for all cases.
     */
    public void clear();

    /**
     * Returns the head node of the linked list.
     * Normally, you would not do this, but we need it for grading your work.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return Node at the head of the linked list.
     */
    public LinkedListNode<T> getHead();

    /**
     * Returns the tail node of the linked list.
     * Normally, you would not do this, but we need it for grading your work.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return Node at the tail of the linked list.
     */
    public LinkedListNode<T> getTail();
}
