import java.util.Comparator;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * Your implementation of various sorting algorithms.
 *
 * @author peiyu wang
 * @version 1.0
 */
public class Sorting {

    /**
     * Implement cocktail sort.
     *
     * It should be:
     *  in-place
     *  stable
     *
     * Have a worst case running time of:
     *  O(n^2)
     *
     * And a best case running time of:
     *  O(n)
     *
     * Any duplicates in the array should be in the same relative position after
     * sorting as they were before sorting. (stable).
     *
     * See the PDF for more info on this sort.
     *
     * @throws IllegalArgumentException if the array or comparator is null
     * @param <T> data type to sort
     * @param arr the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     */
    public static <T> void cocktailSort(T[] arr, Comparator<T> comparator) {
        if (arr == null) {
            throw new IllegalArgumentException("Error,arr is null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Error,comparator is null");
        }
        boolean swapped = true;
        int i = 0;
        int j = arr.length - 1;
        while (i < j && swapped) {
            swapped = false;
            for (int k = i; k < j; k++) {
                if (comparator.compare(arr[k], arr[k + 1]) > 0) {
                    T temp = arr[k];
                    arr[k] = arr[k + 1];
                    arr[k + 1] = temp;
                    swapped = true;
                }
            }
            j--;
            if (swapped) {
                swapped = false;
                for (int k = j; k > i; k--) {
                    if (comparator.compare(arr[k], arr[k - 1]) < 0) {
                        T temp = arr[k];
                        arr[k] = arr[k - 1];
                        arr[k - 1] = temp;
                        swapped = true;
                    }
                }
            }
            i++;
        }

    }

    /**
     * Implement insertion sort.
     *
     * It should be:
     *  in-place
     *  stable
     *
     * Have a worst case running time of:
     *  O(n^2)
     *
     * And a best case running time of:
     *  O(n)
     *
     * Any duplicates in the array should be in the same relative position after
     * sorting as they were before sorting. (stable).
     *
     * See the PDF for more info on this sort.
     *
     * @throws IllegalArgumentException if the array or comparator is null
     * @param <T> data type to sort
     * @param arr the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     */
    public static <T> void insertionSort(T[] arr, Comparator<T> comparator) {
        if (arr == null) {
            throw new IllegalArgumentException("Error,arr is null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Error,comparator is null");
        }
        T temp;
        for (int i = 1; i < arr.length; i++) {
            for (int j = i; j > 0; j--) {
                if (comparator.compare(arr[j], arr[j - 1]) < 0) {
                    temp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = temp;
                } else {
                    j = -1;
                }
            }
        }
    }

    /**
     * Implement selection sort.
     *
     * It should be:
     *  in-place
     *
     * Have a worst case running time of:
     *  O(n^2)
     *
     * And a best case running time of:
     *  O(n^2)
     *
     * Note that there may be duplicates in the array, but they may not
     * necessarily stay in the same relative order.
     *
     * @throws IllegalArgumentException if the array or comparator is null
     * @param <T> data type to sort
     * @param arr the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     */
    public static <T> void selectionSort(T[] arr, Comparator<T> comparator) {
        if (arr == null) {
            throw new IllegalArgumentException("Error,arr is null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Error,comparator is null");
        }
        int minindex;
        for (int i = 0; i < arr.length; i++) {
            minindex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (comparator.compare(arr[j], arr[minindex]) < 0) {
                    minindex = j;
                }
            }
            T temp = arr[minindex];
            arr[minindex] = arr[i];
            arr[i] = temp;
        }
    }

    /**
     * Implement quick sort.
     *
     * Use the provided random object to select your pivots.
     * For example if you need a pivot between a (inclusive)
     * and b (exclusive) where b > a, use the following code:
     *
     * int pivotIndex = r.nextInt(b - a) + a;
     *
     * It should be:
     *  in-place
     *
     * Have a worst case running time of:
     *  O(n^2)
     *
     * And a best case running time of:
     *  O(n log n)
     *
     * Note that there may be duplicates in the array.
     *
     * Make sure you code the algorithm as you have been taught it in class.
     * There are several versions of this algorithm and you may not get full
     * credit if you do not use the one we have taught you!
     *
     * @throws IllegalArgumentException if the array or comparator or rand is
     * null
     * @param <T> data type to sort
     * @param arr the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @param rand the Random object used to select pivots
     */
    public static <T> void quickSort(T[] arr, Comparator<T> comparator,
                                     Random rand) {
        if (arr == null) {
            throw new IllegalArgumentException("Error,arr is null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Error,comparator is null");
        }
        if (rand == null) {
            throw new IllegalArgumentException("Error,rand is null");
        }
        realqucksort(arr, 0, arr.length - 1, comparator, rand);

    }
    /**
         * Implement quick sort.
         * the real quick sort method.
         * @param <T> data type to sort
         * @param arr the array that must be sorted after the method runs
         * @param right eight number.
         * @param left left number
         * @param comparator the Comparator used to compare the data in arr
         * @param rand the Random object used to select pivots
         */

    private static <T> void realqucksort(T[] arr, int left, int right,
        Comparator<T> comparator, Random rand) {

        if (left >= right) {
            return;
        }
        int leftindex = left + 1;
        int rightindex = right;

        int pivotIndex = rand.nextInt(right - left) + left;

        T pivot = arr[pivotIndex];
        arr[pivotIndex] = arr[left];
        arr[left] = pivot;

        while (leftindex <= rightindex) {
            while (leftindex <= rightindex
                    && comparator.compare(arr[leftindex], pivot) <= 0) {
                leftindex++;
            }
            while (leftindex <= rightindex
                    && comparator.compare(arr[rightindex], pivot) >= 0) {
                rightindex--;
            }
            if (leftindex <= rightindex) {
                T temp = arr[leftindex];
                arr[leftindex] = arr[rightindex];
                arr[rightindex] = temp;
                leftindex++;
                rightindex--;
            }
        }
        T temp = arr[rightindex];
        arr[rightindex] = arr[left];
        arr[left] = temp;
        // if (left<rightindex) {
        realqucksort(arr, left, rightindex - 1, comparator, rand);
        // }


        //if (right>leftindex) {
        realqucksort(arr, rightindex + 1, right, comparator, rand);
        // }




    }

    /**
     * Implement merge sort.
     *
     * It should be:
     *  stable
     *
     * Have a worst case running time of:
     *  O(n log n)
     *
     * And a best case running time of:
     *  O(n log n)
     *
     * You can create more arrays to run mergesort, but at the end,
     * everything should be merged back into the original T[]
     * which was passed in.
     *
     * Any duplicates in the array should be in the same relative position after
     * sorting as they were before sorting.
     *
     * @throws IllegalArgumentException if the array or comparator is null
     * @param <T> data type to sort
     * @param arr the array to be sorted
     * @param comparator the Comparator used to compare the data in arr
     */
    public static <T> void mergeSort(T[] arr, Comparator<T> comparator) {
        if (arr == null) {
            throw new IllegalArgumentException("Error,arr is null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Error,comparator is null");
        }
        if (arr.length > 1) {

            int middleindex = (int) (arr.length / 2);
            int leftsize = (int) (arr.length / 2);
            int rightsize = arr.length - leftsize;
            T[] leftarr = (T[]) new Object[leftsize];
            T[] rightarr = (T[]) new Object[rightsize];
            for (int i = 0; i < leftsize; i++) {
                leftarr[i] = arr[i];
            }
            for (int i = leftsize; i < arr.length; i++) {
                rightarr[i - leftsize] = arr[i];
            }

            mergeSort(leftarr, comparator);

            mergeSort(rightarr, comparator);

            int leftindex = 0;
            int rightindex = 0;
            int currentindex = 0;
            while (leftindex < middleindex
                    && rightindex < arr.length - middleindex) {
                if (comparator.compare(leftarr[leftindex],
                                       rightarr[rightindex]) <= 0) {
                    arr[currentindex] = leftarr[leftindex];
                    leftindex++;
                } else {
                    arr[currentindex] = rightarr[rightindex];
                    rightindex++;
                }
                currentindex++;
            }

            while (leftindex < middleindex) {
                arr[currentindex] = leftarr[leftindex];
                leftindex++;
                currentindex++;
            }
            while (rightindex < arr.length - middleindex) {
                arr[currentindex] = rightarr[rightindex];
                rightindex++;
                currentindex++;
            }
        }
    }

    /**
     * Implement LSD (least significant digit) radix sort.
     *
     * Remember you CANNOT convert the ints to strings at any point in your
     * code!
     *
     * It should be:
     *  stable
     *
     * Have a worst case running time of:
     *  O(kn)
     *
     * And a best case running time of:
     *  O(kn)
     *
     * Any duplicates in the array should be in the same relative position after
     * sorting as they were before sorting. (stable)
     *
     * Do NOT use {@code Math.pow()} in your sort. Instead, if you need to, use
     * the provided {@code pow()} method below.
     *
     * You may use {@code java.util.ArrayList} or {@code java.util.LinkedList}
     * if you wish, but it may only be used inside radix sort and any radix sort
     * helpers. Do NOT use these classes with other sorts.
     *
     * @throws IllegalArgumentException if the array is null
     * @param arr the array to be sorted
     * @return the sorted array
     */
    public static int[] lsdRadixSort(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Error,arr is null");
        }
        int maxnumber = arr[0];
        int maxlength = 1;
        for (int i = 0; i < arr.length; i++) {
            if (Math.abs(arr[i]) > maxnumber) {
                maxnumber = Math.abs(arr[i]);
            }
        }
        while ((maxnumber) >= 10) {
            maxlength++;
            maxnumber = maxnumber / 10;
        }

        List<Integer>[] buckets = new ArrayList[19];
        for (int i = 0; i < 19; i++) {
            buckets[i] = new ArrayList<Integer>();
        }
        int divnumber = 1;

        for (int i = 0; i < maxlength; i++) {
            for (Integer num: arr) {

                buckets[((num / divnumber) % 10) + 9].add(num);

            }

            int index = 0;
            for (int k = 0; k < buckets.length; k++) {
                for (Integer xx: buckets[k]) {
                    arr[index++] = xx;
                }
                buckets[k].clear();
            }
            divnumber = divnumber * 10;
        }


        return arr;
    }

    /**
     * Calculate the result of a number raised to a power. Use this method in
     * your radix sorts instead of {@code Math.pow()}.
     *
     * DO NOT MODIFY THIS METHOD.
     *
     * @throws IllegalArgumentException if both {@code base} and {@code exp} are
     * 0
     * @throws IllegalArgumentException if {@code exp} is negative
     * @param base base of the number
     * @param exp power to raise the base to. Must be 0 or greater.
     * @return result of the base raised to that power
     */
    private static int pow(int base, int exp) {
        if (exp < 0) {
            throw new IllegalArgumentException("Exponent cannot be negative.");
        } else if (base == 0 && exp == 0) {
            throw new IllegalArgumentException(
                "Both base and exponent cannot be 0.");
        } else if (exp == 0) {
            return 1;
        } else if (exp == 1) {
            return base;
        }
        int halfPow = pow(base, exp / 2);
        if (exp % 2 == 0) {
            return halfPow * halfPow;
        } else {
            return halfPow * pow(base, (exp / 2) + 1);
        }
    }
}