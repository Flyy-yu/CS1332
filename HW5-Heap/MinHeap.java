/**
 * Your implementation of a min heap.
 *
 * @author Peiyu Wang
 * @version 1.0
 */
public class MinHeap<T extends Comparable<? super T>>
    implements HeapInterface<T> {

    private T[] backingArray;
    private int size;
    // Do not add any more instance variables. Do not change the declaration
    // of the instance variables above.

    /**
     * Creates a Heap with an initial size of {@code STARTING_SIZE} for the
     * backing array.
     *
     * Use the constant field in the interface. Do not use magic numbers!
     */

    public MinHeap() {
        backingArray = (T[]) new Comparable[STARTING_SIZE];
    }


    @
    Override
    public void add(T item) {
        int childindex = 0;
        int parindex = 0;
        T tempp;
        if (item == null) {
            throw new IllegalArgumentException("Error,item is null");
        }

        if (size >= backingArray.length - 1) {
            T[] temp = (T[]) new Comparable[(int) (1.5 * backingArray.length)];
            for (int i = 1; i < backingArray.length; i++) {
                temp[i] = backingArray[i];
            }
            backingArray = temp;
        }
        size++;
        backingArray[size] = item;
        childindex = size;
        parindex = (int) childindex / 2;
        while (childindex > 1 
            && backingArray[parindex].compareTo(backingArray[childindex]) > 0) {
            tempp = backingArray[childindex];
            backingArray[childindex] = backingArray[parindex];
            backingArray[parindex] = tempp;
            childindex = parindex;
            parindex = (int) childindex / 2;

        }

    }

    @
    Override
    public T remove() {

        if (size == 0) {
            throw new java.util.NoSuchElementException("Error,heap is empty");
        }

        int smalloneindex;
        T temp = backingArray[1];
        T swaptemp;
        backingArray[1] = backingArray[size];
        backingArray[size] = null;
        size--;
        int index = 1;
        while ((index * 2) <= size) {

            if (backingArray[index * 2 + 1] != null) {
                if (backingArray[index * 2].compareTo(backingArray[index
                    * 2 + 1]) < 0) {
                    smalloneindex = index * 2;
                } else {
                    smalloneindex = index * 2 + 1;
                }
            } else {
                smalloneindex = index * 2;
            }

            if (backingArray[index].compareTo(backingArray[smalloneindex])
                    > 0) {
                swaptemp = backingArray[index];
                backingArray[index] = backingArray[smalloneindex];
                backingArray[smalloneindex] = swaptemp;
                index = smalloneindex;
            }

            index = smalloneindex;



        }

        return temp;
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

    @
    Override
    public void clear() {
        backingArray = (T[]) new Comparable[STARTING_SIZE];
        size = 0;
    }

    @
    Override
    public Comparable[] getBackingArray() {
        // DO NOT CHANGE THIS METHOD!
        return backingArray;
    }

}