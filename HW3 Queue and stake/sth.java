/**
 * I don't mind lines >80 characters long, as you can tell.
 *
 * @author ahosein3
 * @version 0.1.0
 */

import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class sth {
    private ArrayQueue<String> aq;
    private ArrayStack<String> as;
    private LinkedQueue<String> lq;
    private LinkedStack<String> ls;

    public static final int TIMEOUT = 200;


    @Before
    public void setUp() {
        aq = new ArrayQueue<>();
        as = new ArrayStack<>();
        lq = new LinkedQueue<>();
        ls = new LinkedStack<>();
    }

    @Test(timeout = TIMEOUT)
    public void testIsEmptyAll() {
        assertTrue("ArrayQueue not empty upon init.", aq.isEmpty());
        assertTrue("ArrayStack not empty upon init.", as.isEmpty());
        assertTrue("LinkedQueue not empty upon init.", lq.isEmpty());
        assertTrue("LinkedStack not empty upon init.", ls.isEmpty());
    }

    @Test(timeout = TIMEOUT)
    public void testArrayStack() {
        as.push("poop1");
        assertFalse("ArrayStack is empty after pushing.", as.isEmpty());
        assertEquals("Size did not update correctly after pushing to ArrayStack.", 1, as.size());
        as.push("poop2");
        assertEquals("Size did not update correctly after pushing to ArrayStack.", 2, as.size());

        String[] expectedArray = new String[]{"poop1", "poop2", null, null, null, null, null, null, null, null};
        assertArrayEquals("The backing array of ArrayStack did not match what was expected.", expectedArray, as.getBackingArray());

        assertEquals("Pop did not return the expected value for ArrayStack.", "poop2", as.pop());
        assertEquals("Pop did not return the expected value for ArrayStack.", "poop1", as.pop());
        assertTrue("ArrayStack not empty after popping all elements.", as.isEmpty());

        try {
            as.pop();
            assertTrue("Popping off of an empty ArrayStack did not throw a NoSuchElementException.", false);
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }

        try {
            as.push(null);
            assertTrue("Pushing 'null' to an ArrayStack did not throw a IllegalArgumentException.", false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        int expectedSize  = (int) (((int) (as.getBackingArray().length * 1.5)) * 1.5);
        expectedArray = new String[expectedSize];
        for (int i = 0; i < expectedSize - 1; i++) {
            expectedArray[i] = "poop";
            as.push("poop");
        }

        assertEquals("The length of the backing array of ArrayStack is not as expected after two resizes.", expectedSize, as.getBackingArray().length);
        assertEquals("Size did not update correctly after forcing ArrayStack to resize.", (expectedSize - 1), as.size());
        assertArrayEquals("The backing array of ArrayStack did not match what was expected.", expectedArray, as.getBackingArray());

        as.push("poop");
        assertEquals("It looks like you're preemptively resizing the backing array.", expectedSize, as.getBackingArray().length);

        for (int i = 0; i < expectedSize; i++) {
            as.pop();
        }
        assertEquals("Did you shrink the array after popping? That's not supposed to happen.", expectedSize, as.getBackingArray().length);
        assertTrue("ArrayStack not empty after popping all elements.", as.isEmpty());
    }

    @Test(timeout = TIMEOUT)
    public void testLinkedStack() {
        ls.push("poop1");
        assertFalse("LinkedStack is empty after pushing.", ls.isEmpty());
        assertEquals("Size did not update correctly after pushing to LinkedStack.", 1, ls.size());
        ls.push("poop2");
        assertEquals("Size did not update correctly after pushing to LinkedStack.", 2, ls.size());
        assertEquals("Pop did not return the expected value for LinkedStack.", "poop2", ls.pop());
        assertEquals("Pop did not return the expected value for LinkedStack.", "poop1", ls.pop());
        assertTrue("LinkedStack not empty after popping all elements.", ls.isEmpty());

        try {
            ls.pop();
            assertTrue("Popping off of an empty LinkedStack did not throw a NoSuchElementException.", false);
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }

        try {
            ls.push(null);
            assertTrue("Pushing 'null' to an LinkedStack did not throw a IllegalArgumentException.", false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

    }

    @Test(timeout = TIMEOUT)
    public void testLinkedQueue() {
        lq.enqueue("poop1");
        assertFalse("LinkedStack is empty after enqueueing.", lq.isEmpty());
        assertEquals("Size did not update correctly after enqueueing to LinkedStack.", 1, lq.size());
        lq.enqueue("poop2");
        assertEquals("Size did not update correctly after enqueueing to LinkedStack.", 2, lq.size());
        assertEquals("Dequeue did not return the expected value for LinkedStack.", "poop1", lq.dequeue());
        assertEquals("Dequeue did not return the expected value for LinkedStack.", "poop2", lq.dequeue());
        assertTrue("LinkedStack not empty after Dequeuing all elements.", lq.isEmpty());

        try {
            lq.dequeue();
            assertTrue("Dequeuing off of an empty LinkedStack did not throw a NoSuchElementException.", false);
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }

        try {
            lq.enqueue(null);
            assertTrue("Enqueuing 'null' to an LinkedStack did not throw a IllegalArgumentException.", false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

    }

    @Test(timeout = TIMEOUT)
    public void testArrayQueue() {
        aq.enqueue("poop1");
        assertFalse("ArrayStack is empty after enqueueing.", aq.isEmpty());
        assertEquals("Size did not update correctly after enqueueing to ArrayStack.", 1, aq.size());
        aq.enqueue("poop2");
        assertEquals("Size did not update correctly after enqueueing to ArrayStack.", 2, aq.size());
        assertEquals("Dequeue did not return the expected value for ArrayStack.", "poop1", aq.dequeue());
        assertEquals("Dequeue did not return the expected value for ArrayStack.", "poop2", aq.dequeue());
        assertTrue("ArrayStack not empty after Dequeuing all elements.", aq.isEmpty());

        try {
            aq.dequeue();
            assertTrue("Dequeuing off of an empty ArrayStack did not throw a NoSuchElementException.", false);
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }

        try {
            aq.enqueue(null);
            assertTrue("Enqueuing 'null' to an ArrayStack did not throw a IllegalArgumentException.", false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        int initialCapacity = aq.getBackingArray().length;
        int newCapacity = (int) (initialCapacity * 1.5);
        String[] expectedArray = new String[newCapacity];

        for (int i = 0; i < initialCapacity; i++) {
            aq.enqueue("poop" + i);
            expectedArray[i] = "poop" + (i + 2);
        }

        expectedArray[10] = "poop12";

        assertEquals("Size did not update correctly after enqueueing to ArrayStack.", initialCapacity, aq.size());
        assertEquals("Dequeue did not return the expected value for ArrayStack.", "poop0", aq.dequeue());
        assertEquals("Dequeue did not return the expected value for ArrayStack.", "poop1", aq.dequeue());
        assertEquals("Size did not update correctly after dequeueing to ArrayStack.", initialCapacity - 2, aq.size());

        aq.enqueue("poop10");
        assertEquals("Size did not update correctly after enqueueing to ArrayStack.", initialCapacity - 1, aq.size());
        assertEquals("Your backing array resized instead of wrapping around.", initialCapacity, aq.getBackingArray().length);

        aq.enqueue("poop11");
        aq.enqueue("poop12");

        assertArrayEquals("Your array is not in order after resizing.", expectedArray, aq.getBackingArray());
        assertEquals("ArrayStack size did not update correctly after resizing.", initialCapacity + 1, aq.size());

        aq.enqueue("poop13");
        aq.enqueue("poop14");

        assertEquals("ArrayStack size did not update correctly after enqueueing after resizing.", initialCapacity + 3, aq.size());

        aq.enqueue("poop15");
        aq.enqueue("poop16");

        assertEquals("ArrayStack size did not update correctly after enqueueing after resizing.", initialCapacity + 5, aq.size());
        assertEquals("Your backing array in ArrayQueue resized unnecessarily.", newCapacity, aq.getBackingArray().length);

        assertEquals("You did not dequeue correctely after resizing.", "poop2", aq.dequeue());
    }
}