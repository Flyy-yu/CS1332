/**
 * Your implementation of an ArrayList.
 *
 * @author Peiyu Wang
 * @version 1
 */
public class ArrayList<T> implements ArrayListInterface<T> {

    // Do not add new instance variables.
    private T[] backingArray;
    private int size;

    /**
     * Constructs a new ArrayList.
     *
     * You may add statements to this method.
     */
    public ArrayList() {
        backingArray = (T[]) new Object[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public void addAtIndex(int index, T data) {
        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }

        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Error, index is negative" 
                + "or larger than Array size");
        }

        if (size >= backingArray.length) {
            T[] temp = (T[]) new Object[2 * backingArray.length];
            for (int i = 0; i < backingArray.length; i++) {
                temp[i] = backingArray[i];
            }
            backingArray = temp;
        }

        for (int i = size; i > index; i--) {
            backingArray[i] = backingArray[i - 1];
        }
        backingArray[index] = data;
        size = size + 1;




    }

    @Override
    public void addToFront(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }
        if (size >= backingArray.length) {
            T[] temp = (T[]) new Object[2 * backingArray.length];
            for (int i = 0; i < backingArray.length; i++) {
                temp[i] = backingArray[i];
            }
            backingArray = temp;
        }


        for (int i = size; i > 0; i--) {
            backingArray[i] = backingArray[i - 1];
        }
        size = size + 1;
        backingArray[0] = data;




    }

    @Override
    public void addToBack(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }
        if (size >= backingArray.length) {
            T[] temp = (T[]) new Object[2 * backingArray.length];
            for (int i = 0; i < backingArray.length; i++) {
                temp[i] = backingArray[i];
            }
            backingArray = temp;
        }
        backingArray[size] = data;
        size = size + 1;


    }

    @Override
    public T removeAtIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Error, index is"
                + " negative or larger than Array size");
        }
        if (size == 0) {
            return null;
        }

        T temp = backingArray[index];

        for (int i = index; i < size - 1; i++) {
            backingArray[i] = backingArray[i + 1];
        }
        backingArray[size - 1] = null;
        size--;
        return temp;
    }

    @Override
    public T removeFromFront() {
        if (size == 0) {
            return null;
        }
        T temp = backingArray[0];

        for (int i = 0; i < size - 1; i++) {
            backingArray[i] = backingArray[i + 1];
        }
        backingArray[size - 1] = null;
        size--;
        return temp;
    }

    @Override
    public T removeFromBack() {
        if (size == 0) {
            return null;
        }
        T temp = backingArray[size - 1];


        backingArray[size - 1] = null;
        size--;

        return temp;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Error, index is negative "
                + "or larger than Array size");
        }

        return backingArray[index];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        backingArray = (T[]) new Object[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public Object[] getBackingArray() {
        // DO NOT MODIFY.
        return backingArray;
    }
}