/**
 * Your implementation of an array-backed stack.
 *
 * @author Peiyu wang
 * @version 1.0
 */
public class ArrayStack<T> implements StackInterface<T> {

    // Do not add new instance variables.
    private T[] backingArray;
    private int size;

    /**
     * Constructs a new ArrayStack.
     */
    public ArrayStack() {
        backingArray = (T[]) new Object[INITIAL_CAPACITY];
        size = 0;
    }

    @
    Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Pop from the stack.
     *
     * Do not shrink the backing array.
     *
     * @see StackInterface#pop()
     */
    @
    Override
    public T pop() {
        if (size == 0) {
            throw new java.util.NoSuchElementException("Error, stake is empty");
        }
        size--;
        T temp = backingArray[size];
        backingArray[size] = null;
        return temp;
    }

    /**
     * Push the given data onto the stack.
     *
     * If sufficient space is not available in the backing array, you should
     * regrow it to 1.5 times the current backing array length, rounding down
     * if necessary.
     *
     * @see StackInterface#push()
     */
    @
    Override
    public void push(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }

        if (size >= backingArray.length) {
            T[] temp = (T[]) new Object[(int) (1.5 * backingArray.length)];
            for (int i = 0; i < backingArray.length; i++) {
                temp[i] = backingArray[i];
            }
            backingArray = temp;
        }
        backingArray[size] = data;

        size++;
    }

    @
    Override
    public int size() {
        return size;
    }

    /**
     * Returns the backing array of this stack.
     * Normally, you would not do this, but we need it for grading your work.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return the backing array
     */
    public Object[] getBackingArray() {
        // DO NOT MODIFY!
        return backingArray;
    }
}