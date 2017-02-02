import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for the CS1332 Heaps/Priority Queues assignment.
 *
 * @version 3.1
 * @author Timothy J. Aveni
 */

public class test {

    private static final int TIMEOUT = 2000; 
    private static final boolean TRY_REALLY_HARD_TO_GUESS_YOUR_INTENT = true;
    
    HeapInterface<Integer> heap;
    
    final PrintStream originalSystemOut = System.out;
    CleanOutputStream cleanOutputStream;
    
    private class CleanOutputStream extends OutputStream {

        private boolean clean = true;
        
        @Override
        public void write(int b) throws IOException {
            clean = false;
            originalSystemOut.write(b);
        }
        
        public boolean isClean() {
            return clean;
        }
    }
    
    private void assertException(String message, Class<? extends Exception> exceptionClass, Runnable code) {
        assertException(message, new Class[]{exceptionClass}, code);
    }
    
    private void assertException(String message, Class<? extends Exception>[] exceptionClasses, Runnable code) {
        try {
            code.run();
            Assert.fail(message);
        } catch (Exception e) {
            assertNotNull(
                            "Exception messages must not be empty",
                    e.getMessage());
            assertNotEquals(
                    "Exception messages must not be empty",
                    "",
                    e.getMessage());
            
            boolean foundException = false;
            for (Class<? extends Exception> exceptionClass: exceptionClasses) {
                if (exceptionClass.equals(e.getClass())) {
                    foundException = true;
                }
            }
            
            if (!foundException) {
                Assert.fail(message);
            }
        }
    }
    
    private <T> int countNonNulls(T[] array) {
        int count = 0;
        for (T item: array) {
            if (item != null) {
                count++;
            }
        }
        
        return count;
    }
    
    private <T extends Comparable<? super T>> void assertHeapArrayEquals(String message, T[] expected, HeapInterface<T> heap) {
        assertArrayEquals(message, expected, heap.getBackingArray());
        
        assertEquals(
                "The size() method returned an incorrect value (for these tests, implement size() and isEmpty() before other methods)",
                countNonNulls(expected),
                heap.size());
        assertEquals("The isEmpty method returned a value inconsistent with size() (for these tests, implement size() and isEmpty() before any other method)",
                heap.size() == 0, 
                heap.isEmpty());
    }
    
    @Before
    public void init() {
        heap = new MinHeap<Integer>();
        cleanOutputStream = new CleanOutputStream();
        System.setOut(new PrintStream(cleanOutputStream));
    }
    
    @After
    public void checkOutput() {
        assertTrue(
                "You used print statements somewhere in your code. That's forbidden!", 
                cleanOutputStream.isClean());
    }
    
    @Test(timeout = TIMEOUT)
    public void constructor() {
        assertArrayEquals(
                "The MinHeap's backing array didn't start out as an empty array of length 10.",
                new Integer[]{null, null, null, null, null, null, null, null, null, null},
                heap.getBackingArray());
    }
    
    @Test(timeout = TIMEOUT)
    public void addException() {
        assertException(
                "Adding null to a MinHeap should throw an IllegalArgumentException", 
                IllegalArgumentException.class,
                () -> heap.add(null));
    }

    @Test(timeout = TIMEOUT)
    public void addAscending() {
        heap.add(1);
        assertHeapArrayEquals(
                "Adding one element to the heap failed",
                new Integer[]{null, 1, null, null, null, null, null, null, null, null},
                heap);
        
        heap.add(2);
        assertHeapArrayEquals(
                "Adding to the second layer of the heap failed",
                new Integer[]{null, 1, 2, null, null, null, null, null, null, null},
                heap);
        
        heap.add(3);
        assertHeapArrayEquals(
                "Adding to the second layer of the heap failed",
                new Integer[]{null, 1, 2, 3, null, null, null, null, null, null},
                heap);
        
        heap.add(4);
        assertHeapArrayEquals(
                "Adding to the third layer of the heap failed",
                new Integer[]{null, 1, 2, 3, 4, null, null, null, null, null},
                heap);
        
        heap.add(5);
        assertHeapArrayEquals(
                "Adding to the third layer of the heap failed",
                new Integer[]{null, 1, 2, 3, 4, 5, null, null, null, null},
                heap);
        
        heap.add(6);
        assertHeapArrayEquals(
                "Adding to the third layer of the heap failed",
                new Integer[]{null, 1, 2, 3, 4, 5, 6, null, null, null},
                heap);
        
        heap.add(7);
        assertHeapArrayEquals(
                "Adding to the third layer of the heap failed",
                new Integer[]{null, 1, 2, 3, 4, 5, 6, 7, null, null},
                heap);
    }
    
    @Test(timeout = TIMEOUT)
    public void addDescending() {
        heap.add(7);
        assertHeapArrayEquals(
                "Adding one element to the heap failed",
                new Integer[]{null, 7, null, null, null, null, null, null, null, null},
                heap);
        
        heap.add(6);
        assertHeapArrayEquals(
                "Adding to the second layer of the heap, involving a swap, failed",
                new Integer[]{null, 6, 7, null, null, null, null, null, null, null},
                heap);
        
        heap.add(5);
        assertHeapArrayEquals(
                "Adding to the second layer of the heap, involving a swap, failed",
                new Integer[]{null, 5, 7, 6, null, null, null, null, null, null},
                heap);
        
        heap.add(4);
        assertHeapArrayEquals(
                "Adding to the third layer of the heap, involving two swaps, failed",
                new Integer[]{null, 4, 5, 6, 7, null, null, null, null, null},
                heap);
        
        heap.add(3);
        assertHeapArrayEquals(
                "Adding to the third layer of the heap, involving two swaps, failed",
                new Integer[]{null, 3, 4, 6, 7, 5, null, null, null, null},
                heap);
        
        heap.add(2);
        assertHeapArrayEquals(
                "Adding to the third layer of the heap, involving two swaps, failed",
                new Integer[]{null, 2, 4, 3, 7, 5, 6, null, null, null},
                heap);
        
        heap.add(1);
        assertHeapArrayEquals(
                "Adding to the third layer of the heap, involving two swaps, failed",
                new Integer[]{null, 1, 4, 2, 7, 5, 6, 3, null, null},
                heap);
    }
    
    @Test(timeout = TIMEOUT)
    public void addWithMidStops() {
        heap.add(2);
        heap.add(7);
        heap.add(4);
        heap.add(6);
        
        // == null, 2, 6, 4, 7, null, null, null, null, null
        
        heap.add(5);
        assertHeapArrayEquals(
                "Adding to the third layer of the heap, involving one swap, failed",
                new Integer[]{null, 2, 5, 4, 7, 6, null, null, null, null},
                heap);
        
        heap.add(3);
        assertHeapArrayEquals(
                "Adding to the third layer of the heap, involving one swap, failed",
                new Integer[]{null, 2, 5, 3, 7, 6, 4, null, null, null},
                heap);
        
        heap.add(1);
        assertHeapArrayEquals(
                "Adding to the third layer of the heap, involving two swaps, failed",
                new Integer[]{null, 1, 5, 2, 7, 6, 4, 3, null, null},
                heap);
    }
    
    @Test(timeout = TIMEOUT)
    public void addResize() {
        heap.add(5);
        heap.add(10);
        heap.add(7);
        heap.add(9);
        heap.add(8);
        heap.add(6);
        heap.add(4);
        heap.add(3);
        heap.add(2);
        heap.add(1);
        
        assertHeapArrayEquals(
                "The heap failed to resize correctly when adding a tenth element.",
                new Integer[]{null, 1, 2, 5, 4, 3, 7, 6, 10, 8, 9, null, null, null, null},
                heap);
        
        heap.add(11);
        heap.add(12);
        heap.add(13);
        heap.add(14);
        heap.add(15);
        
        assertHeapArrayEquals(
                "The heap failed to resize correctly when adding a fifteenth element. (you rounded down, right?)",
                new Integer[]{null, 1, 2, 5, 4, 3, 7, 6, 10, 8, 9, 11, 12, 13, 14, 15, null, null, null, null, null, null},
                heap);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeException() {
        assertException(
                "Removing from an empty heap should throw a java.util.NoSuchElementException.",
                java.util.NoSuchElementException.class,
                () -> heap.remove());
    }
    
    @Test(timeout = TIMEOUT)
    public void removeSingle() {
        heap.add(1);
        
        assertEquals(
        		"Removing an item from a heap with one element did not return the correct element",
        		heap.remove(),
        		new Integer(1));
        
        assertHeapArrayEquals(
        		"Removing an item from a heap with one element did not empty the heap",
        		new Integer[10],
        		heap);
    }
    
    @Test(timeout = TIMEOUT)
    public void remove() {
    	/* Possible cases, for each swap in a particular remove:
    	 * 		2 children:
    	 * 			both children are less than the parent:
    	 * 				a - swap with the left child (less than the right)
    	 * 				b - swap with the right child (less than the left)
    	 * 			only one child is less than the parent:
    	 * 				c - swap with the left child
    	 * 				d - swap with the right child
    	 *         neither child is less than the parent:
    	 *              e - don't swap			
    	 * (will never have neither child less than the parent)
    	 * 		1 child (on the left):
    	 * 				f - swap with the left child
    	 * 				g - don't swap with the left child
    	 * 		0 children:
    	 * 				h - don't swap
    	 * 		i - no swaps
    	 */
    	
    	heap.add(1);
        heap.add(2);
        heap.add(5);
        heap.add(4);
        heap.add(3);
        heap.add(6);
        heap.add(7);
        
        // a, b, h
        assertEquals(heap.remove(), new Integer(1));
        assertHeapArrayEquals(
                "Removing an item from a three-level heap failed; cases a, b, and h",
                new Integer[]{null, 2, 3, 5, 4, 7, 6, null, null, null},
                heap);
        
        // a, c, h
        assertEquals(heap.remove(), new Integer(2));
        assertHeapArrayEquals(
                "Removing an item from a three-level heap failed; cases a, c, and h",
                new Integer[]{null, 3, 4, 5, 6, 7, null, null, null, null},
                heap);
        
        // a, f, h
        assertEquals(heap.remove(), new Integer(3));
        assertHeapArrayEquals(
                "Removing an item from a three-level heap failed; cases a, f, and h",
                new Integer[]{null, 4, 6, 5, 7, null, null, null, null, null},
                heap);
        
        // b, h
        assertEquals(heap.remove(), new Integer(4));
        assertHeapArrayEquals(
                "Removing an item from a three-level (becoming a two-level) heap failed; cases b and h",
                new Integer[]{null, 5, 6, 7, null, null, null, null, null, null},
                heap);
        
        // f
        assertEquals(heap.remove(), new Integer(5));
        assertHeapArrayEquals(
                "Removing an item from a two-level heap failed; case f",
                new Integer[]{null, 6, 7, null, null, null, null, null, null, null},
                heap);
        
        // h
        assertEquals(heap.remove(), new Integer(6));
        assertHeapArrayEquals(
                "Removing an item from a two-level (becoming a one-level) heap failed; case h",
                new Integer[]{null, 7, null, null, null, null, null, null, null, null},
                heap);
        
        heap.add(9);
        heap.add(8);
        
        // g
        assertEquals(heap.remove(), new Integer(7));
        assertHeapArrayEquals(
                "Removing an item from a two-level heap failed; case g",
                new Integer[]{null, 8, 9, null, null, null, null, null, null, null},
                heap);
        
        // h
        assertEquals(heap.remove(), new Integer(8));
        assertHeapArrayEquals(
                "Removing an item from a two-level (becoming a one-level) heap failed; case h",
                new Integer[]{null, 9, null, null, null, null, null, null, null, null},
                heap);
        // i
        assertEquals(heap.remove(), new Integer(9));
        assertHeapArrayEquals(
                "Removing the final element in a heap failed; case i",
                new Integer[]{null, null, null, null, null, null, null, null, null, null},
                heap);
        
        heap.add(1);
        heap.add(4);
        heap.add(2);
        heap.add(5);
        heap.add(6);
        heap.add(3);
        
        // d
        assertEquals(heap.remove(), new Integer(1));
        assertHeapArrayEquals(
                "Removing an item from a three-level heap failed; case d",
                new Integer[]{null, 2, 4, 3, 5, 6, null, null, null, null},
                heap);
        
        while (!heap.isEmpty()) {
            heap.remove();
        }
        
        heap.add(1);
        heap.add(2);
        heap.add(3);
        heap.add(5);
        heap.add(6);
        heap.add(4);
        
        // e
        assertEquals(heap.remove(), new Integer(1));
        assertHeapArrayEquals(
                "Removing an item from a three-level heap failed; case e",
                new Integer[]{null, 2, 4, 3, 5, 6, null, null, null, null},
                heap);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeResize() {
        heap.add(1);
        heap.add(2);
        heap.add(3);
        heap.add(4);
        heap.add(5);
        heap.add(6);
        heap.add(7);
        heap.add(8);
        heap.add(9);
        heap.add(10);
        
        assertEquals(heap.remove(), new Integer(1));
        assertHeapArrayEquals(
                "Removing the final element of a resized heap seems to cause a problem",
                new Integer[]{null, 2, 4, 3, 8, 5, 6, 7, 10, 9, null, null, null, null, null},
                heap);
    }
    
    @Test(timeout = TIMEOUT)
    public void clear() {
        heap.add(1);
        heap.add(2);
        heap.add(3);
        heap.add(4);
        heap.add(5);
        heap.add(6);
        heap.add(7);
        
        heap.clear();
        
        assertHeapArrayEquals(
                "The clear() method failed", 
                new Integer[]{null, null, null, null, null, null, null, null, null, null},
                heap);
    }
    
    @Test(timeout = TIMEOUT)
    public void clearAfterResize() {
        heap.add(1);
        heap.add(2);
        heap.add(3);
        heap.add(4);
        heap.add(5);
        heap.add(6);
        heap.add(7);
        heap.add(8);
        heap.add(9);
        heap.add(10);
        
        heap.clear();
        
        assertHeapArrayEquals(
                "The clear() method failed after resize (did you make sure the array was reset to length STARTING_SIZE?)",
                new Integer[]{null, null, null, null, null, null, null, null, null, null}, 
                heap);
    }
    
    private class CompareCheckingInteger implements Comparable<CompareCheckingInteger> {
    	int data;
    	int comparisons = 0;
    	
    	public CompareCheckingInteger(int data) {
    		this.data = data;
    	}

		@Override
		public int compareTo(CompareCheckingInteger o) {
			comparisons++;
			o.comparisons++;
			return this.data - o.data;
		}
		
		@Override
		public String toString() {
			return "" + this.data;
		}
    }
    
    /**
     * This test checks that, in your add() method,
     * 	you STOP traversing/comparing up the tree once you've
     * 	placed the element in its final location.
     */
    @Test(timeout = TIMEOUT)
    public void stopPropagating() {
    	MinHeap<CompareCheckingInteger> compareCheckingHeap = new MinHeap<CompareCheckingInteger>(); 
    	
    	compareCheckingHeap.add(new CompareCheckingInteger(1));
    	CompareCheckingInteger two = new CompareCheckingInteger(2);
    	compareCheckingHeap.add(two);
    	compareCheckingHeap.add(new CompareCheckingInteger(3));
    	compareCheckingHeap.add(new CompareCheckingInteger(4));
    	
    	for (int i = 6; i <= 11; i++) {
    		compareCheckingHeap.add(new CompareCheckingInteger(i));
    	}
    	
    	int preComparisons = two.comparisons;
    	
    	compareCheckingHeap.add(new CompareCheckingInteger(5));
    	
    	// If you kept comparing up the tree, two will have been compared two additional times, instead of one.
    	assertEquals(
    	        "The add method resulted in too many comparisons: did you remember to stop comparing"
    	        + " up the tree when you found the final location for the added element?",
    	        1,
    	        two.comparisons - preComparisons);
    }
    
    @Test(timeout = TIMEOUT)
    public void queue() {
        MinPriorityQueue<Integer> queue = new MinPriorityQueue<Integer>();
        
        queue.enqueue(5);
        queue.enqueue(10);
        queue.enqueue(7);
        queue.enqueue(9);
        queue.enqueue(8);
        queue.enqueue(6);
        queue.enqueue(4);
        queue.enqueue(3);
        queue.enqueue(2);
        queue.enqueue(1);
        queue.enqueue(11);
        queue.enqueue(12);
        queue.enqueue(13);
        queue.enqueue(14);
        queue.enqueue(15);

        assertHeapArrayEquals(
                "The queue did not behave identically to the heap (assuming the heap tests passed).",
                new Integer[]{null, 1, 2, 5, 4, 3, 7, 6, 10, 8, 9, 11, 12, 13, 14, 15, null, null, null, null, null, null},
                queue.getBackingHeap());
        assertEquals(queue.size(), queue.getBackingHeap().size());
        assertEquals(queue.isEmpty(), queue.getBackingHeap().isEmpty());
        
        queue.clear();
        assertEquals(queue.size(), queue.getBackingHeap().size());
        assertEquals(queue.isEmpty(), queue.getBackingHeap().isEmpty());
        
        assertHeapArrayEquals(
                "The queue did not clear correctly.",
                new Integer[]{null, null, null, null, null, null, null, null, null, null},
                queue.getBackingHeap());
        
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(5);
        queue.enqueue(4);
        queue.enqueue(3);
        queue.enqueue(6);
        queue.enqueue(7);

        assertEquals(queue.dequeue(), new Integer(1));
        assertHeapArrayEquals(
                "Removing an item from a three-level queue failed; cases a, b, and h",
                new Integer[]{null, 2, 3, 5, 4, 7, 6, null, null, null},
                queue.getBackingHeap());

        assertEquals(queue.dequeue(), new Integer(2));
        assertHeapArrayEquals(
                "Removing an item from a three-level queue failed; cases a, c, and h",
                new Integer[]{null, 3, 4, 5, 6, 7, null, null, null, null},
                queue.getBackingHeap());

        assertEquals(queue.dequeue(), new Integer(3));
        assertHeapArrayEquals(
                "Removing an item from a three-level queue failed; cases a, f, and h",
                new Integer[]{null, 4, 6, 5, 7, null, null, null, null, null},
                queue.getBackingHeap());

        assertEquals(queue.dequeue(), new Integer(4));
        assertHeapArrayEquals(
                "Removing an item from a three-level (becoming a two-level) queue failed; cases b and h",
                new Integer[]{null, 5, 6, 7, null, null, null, null, null, null},
                queue.getBackingHeap());

        assertEquals(queue.dequeue(), new Integer(5));
        assertHeapArrayEquals(
                "Removing an item from a two-level queue failed; case f",
                new Integer[]{null, 6, 7, null, null, null, null, null, null, null},
                queue.getBackingHeap());

        assertEquals(queue.dequeue(), new Integer(6));
        assertHeapArrayEquals(
                "Removing an item from a two-level (becoming a one-level) queue failed; case h",
                new Integer[]{null, 7, null, null, null, null, null, null, null, null},
                queue.getBackingHeap());

        queue.enqueue(9);
        queue.enqueue(8);

        assertEquals(queue.dequeue(), new Integer(7));
        assertHeapArrayEquals(
                "Removing an item from a two-level queue failed; case g",
                new Integer[]{null, 8, 9, null, null, null, null, null, null, null},
                queue.getBackingHeap());

        assertEquals(queue.dequeue(), new Integer(8));
        assertHeapArrayEquals(
                "Removing an item from a two-level (becoming a one-level) queue failed; case h",
                new Integer[]{null, 9, null, null, null, null, null, null, null, null},
                queue.getBackingHeap());
        assertEquals(queue.dequeue(), new Integer(9));
        assertHeapArrayEquals(
                "Removing the final element in a queue failed; case i",
                new Integer[]{null, null, null, null, null, null, null, null, null, null},
                queue.getBackingHeap());

        queue.enqueue(1);
        queue.enqueue(4);
        queue.enqueue(2);
        queue.enqueue(5);
        queue.enqueue(6);
        queue.enqueue(3);

        assertEquals(queue.dequeue(), new Integer(1));
        assertHeapArrayEquals(
                "Removing an item from a three-level queue failed; case d",
                new Integer[]{null, 2, 4, 3, 5, 6, null, null, null, null},
                queue.getBackingHeap());

        while (!queue.isEmpty()) {
            queue.dequeue();
        }

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(5);
        queue.enqueue(6);
        queue.enqueue(4);

        assertEquals(queue.dequeue(), new Integer(1));
        assertHeapArrayEquals(
                "Removing an item from a three-level queue failed; case e",
                new Integer[]{null, 2, 4, 3, 5, 6, null, null, null, null},
                queue.getBackingHeap());
        
        queue.clear();
        
        try {
            queue.enqueue(null);
            Assert.fail("Enqueueing a null element should throw an IllegalArgumentException");
        } catch(Exception e) {
            assertNotNull(
                    "Exception messages must not be empty", 
                    e.getMessage());
            assertNotEquals(
                    "Exception messages must not be empty", 
                    "",
                    e.getMessage());
            assertEquals(
                    "Enqueueing a null element should throw an IllegalArgumentException",
                    e.getClass(), 
                    IllegalArgumentException.class);
            
            if (TRY_REALLY_HARD_TO_GUESS_YOUR_INTENT && e.getMessage().matches(".*heap.*") && !e.getMessage().matches(".*backing heap*.")) {
                Assert.fail("The exception message for enqueueing a null element says \"heap\" in it."
                        + " Did you copy-paste your exception code from the heap and forget to change it?"
                        + " If your exception message is fine, you can disable this test by setting TRY_REALLY_HARD_TO_GUESS_YOUR_INTENT at the top of the test file to false.");
            }
        }
        
        try {
            queue.dequeue();
            Assert.fail("Dequeueing from an empty queue should throw a java.util.NoSuchElementException");
        } catch(Exception e) {
            assertNotNull(
                    "Exception messages must not be empty",
                    e.getMessage());
            assertNotEquals(
                    "Exception messages must not be empty",
                    "",
                    e.getMessage());
            assertEquals(
                    "Dequeueing from an empty queue should throw a java.util.NoSuchElementException",
                    e.getClass(), 
                    java.util.NoSuchElementException.class);
            
            if (TRY_REALLY_HARD_TO_GUESS_YOUR_INTENT && e.getMessage().matches(".*heap.*") && !e.getMessage().matches(".*backing heap*.")) {
                Assert.fail("The exception message for dequeueing from an empty queue says \"heap\" in it."
                        + " Did you copy-paste your exception code from the heap and forget to change it?"
                        + " If your exception message is fine, you can disable this test by setting TRY_REALLY_HARD_TO_GUESS_YOUR_INTENT at the top of the test file to false.");
            }
        }
    }
    
}