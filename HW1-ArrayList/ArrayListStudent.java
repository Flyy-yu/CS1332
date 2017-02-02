import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

/**
 * Student JUnit tests for Homework 1.
 *
 * @version 1.2
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ArrayListStudent {

    private ArrayList<String> list;

    private static final int TIMEOUT = 200;
    
    private static final int INITIAL_CAPACITY = ArrayListInterface.INITIAL_CAPACITY;

    @Before
    public void setup() {
        list = new ArrayList<>();
    }

    /**
     *  A helper method for various tests.
     *
     * First, this method tests isEmpty and size.
     * All non-null elements in actual are compared against the elements in expected.
     * Using getExpectedLength, this method tests the nullity of
     * remaining elements in actual.backingArray, as well as length.
     * 
     * @param method - String for assertion message, e.g. "Did addToFront update size?"
     * @param expected - An array of the non-null elements expected
     * @param actual - The actual list to be tested
     */
    private void assertArrayListEquals(String method, Object[] expected, ArrayList<String> actual) {
        if (expected.length == 0) {
            assertTrue(actual.isEmpty());
        } else {
            assertFalse(actual.isEmpty());
        }
        Object[] actualArray = actual.getBackingArray();
        assertEquals("Did " + method + " update size?", expected.length, actual.size());
        assertEquals(getExpectedLength(expected.length), actualArray.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actualArray[i]);
        }
        for (int i = expected.length; i < actualArray.length; i++) {
            assertNull(actualArray[i]);
        }
    }

    /**
     * A helper method for assertArrayListEquals
     *
     * @param i - The 0-indexed number of non-null elements in the list
     * @return - The expected length of the backingArray, accounting for null elements
     */
    private int getExpectedLength(int i) {
        int expectedLength = INITIAL_CAPACITY;
        if (i > 0) {
            expectedLength = INITIAL_CAPACITY *
                    (int) Math.ceil(new Integer(i).doubleValue() /
                            INITIAL_CAPACITY);
        }
        return expectedLength;
    }

    @Test(timeout = TIMEOUT)
    public void test_00_constructor() {
        assertEquals("A new empty ArrayList should have a backing array of size " +
                        INITIAL_CAPACITY + ".",
                INITIAL_CAPACITY, list.getBackingArray().length);
        assertEquals("A new empty ArrayList should be size 0.", 0, list.size());
    }

    @Test(timeout = TIMEOUT)
    public void test_01_addToFront() {
        // test exception
        try {
            list.addToFront(null);
            Assert.fail();
        } catch (Exception e) {
            assertEquals("addToFront must throw java.lang.IllegalArgumentException if data is null.",
                    IllegalArgumentException.class, e.getClass());
        }
        // test function
        for (int i = 1; i <= INITIAL_CAPACITY + 1; i++) {
            String[] expected = new String[i];
            for (int j = i; j > 0; j--) {
                expected[i - j] = "" + j;
            }
            list.addToFront(i + "");
            assertArrayListEquals("addToFront", expected, list);
        }
    }

    @Test(timeout = TIMEOUT)
    public void test_02_addToBack() {
        // test exception
        try {
            list.addToBack(null);
            Assert.fail();
        } catch (Exception e) {
            assertEquals("addToBack must throw java.lang.IllegalArgumentException if data is null.",
                    IllegalArgumentException.class, e.getClass());
        }
        // test function
        for (int i = 1; i <= INITIAL_CAPACITY + 1; i++) {
            String[] expected = new String[i];
            for (int j = i; j > 0; j--) {
                expected[j - 1] = "" + j;
            }
            list.addToBack(i + "");
            assertArrayListEquals("addToBack", expected, list);
        }
    }

    @Test(timeout = TIMEOUT)
    public void test_03_addAtIndex() {
        // test exceptions
        String message = "addAtIndex must throw java.lang.IndexOutOfBoundsException" +
                " if index is negative or index > size.";
        for (int i: new int[]{-1, 1}) {
            try {
                list.addAtIndex(i, "");
                Assert.fail();
            } catch (Exception e) {
                assertEquals(message, IndexOutOfBoundsException.class, e.getClass());
            }
        }
        message = "addAtIndex must throw java.lang.IllegalArgumentException if data is null.";
        try {
            list.addAtIndex(0, null);
            Assert.fail();
        } catch (Exception e) {
            assertEquals(message, IllegalArgumentException.class, e.getClass());
        }
        // test function
        // test addAtIndex(0, data) for empty list
        list.addAtIndex(0, "foo");
        assertArrayListEquals("addAtIndex", new String[]{"foo"}, list);
        list = new ArrayList<>(); // reset
        // test addAtIndex(0, data) for partial list
        list.addToFront("2");
        list.addAtIndex(0, "1");
        assertArrayListEquals("addAtIndex", new String[]{"1", "2"}, list);
        // test addAtIndex(size, data) for partial list
        list.addAtIndex(2, "4");
        assertArrayListEquals("addAtIndex", new String[]{"1", "2", "4"}, list);
        // test addAtIndex(size / 2, data) for partial list
        list.addAtIndex(2, "3");
        assertArrayListEquals("addAtIndex", new String[]{"1", "2", "3", "4"}, list);
        list = new ArrayList<>(); // reset
        // test addAtIndex(0, data) for full list
        for (int i = 1; i <= INITIAL_CAPACITY; i++) {
            list.addToBack("" + i);
        }
        list.addAtIndex(0, "" + 0);
        assertArrayListEquals("addAtIndex", new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}, list);
        list = new ArrayList<>(); // reset
        // test addAtIndex(size, data) for full list
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            list.addToBack("" + i);
        }
        list.addAtIndex(10, "" + 10);
        assertArrayListEquals("addAtIndex", new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}, list);
        list = new ArrayList<>(); // reset
        // test addAtIndex(size / 2, data) for full list
        for (int i = 0; i <= INITIAL_CAPACITY; i++) {
            if (i != 6) {
                list.addToBack("" + i);
            }
        }
        list.addAtIndex(6, "" + 6);
        assertArrayListEquals("addAtIndex", new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}, list);
    }

    @Test(timeout = TIMEOUT)
    public void test_04_removeFromFront() {
        // test no exceptions
        // test function
        assertNull(list.removeFromFront());
        list.addToFront("0");
        assertEquals("0", list.removeFromFront());
        assertArrayListEquals("removeFromFront", new String[]{}, list);
        list.addToFront("0");
        list.addToFront("foo");
        assertEquals("foo", list.removeFromFront());
        assertArrayListEquals("removeFromFront", new String[]{"0"}, list);
    }

    @Test(timeout = TIMEOUT)
    public void test_05_removeFromBack() {
        // test no exceptions
        // test function
        assertNull(list.removeFromBack());
        list.addToFront("0");
        assertEquals("0", list.removeFromBack());
        assertArrayListEquals("removeFromBack", new String[]{}, list);
        list.addToFront("foo");
        list.addToFront("0");
        assertEquals("foo", list.removeFromBack());
        assertArrayListEquals("removeFromBack", new String[]{"0"}, list);
    }

    @Test(timeout = TIMEOUT)
    public void test_06_removeAtIndex() {
        // test exceptions
        String message = "removeAtIndex must throw java.lang.IndexOutOfBoundsException" +
                " if index is negative or index >= size.";
        for (int i: new int[]{-1, 0, 1}) {
            try {
                list.removeAtIndex(i);
                Assert.fail();
            } catch (Exception e) {
                assertEquals(message, IndexOutOfBoundsException.class, e.getClass());
            }
        }
        // test function
        // test removeAtIndex(0, data) for partial list
        list.addToBack("0");
        list.addToBack("1");
        assertEquals("0", list.removeAtIndex(0));
        assertArrayListEquals("removeAtIndex", new String[]{"1"}, list);
        // test removeAtIndex(0 == size, data) for partial list
        assertEquals("1", list.removeAtIndex(0));
        assertArrayListEquals("removeAtIndex", new String[]{}, list);
        // test removeAtIndex(size, data) for partial list
        list.addToBack("0");
        list.addToBack("1");
        assertEquals("1", list.removeAtIndex(1));
        assertArrayListEquals("removeAtIndex", new String[]{"0"}, list);
        // test removeAtIndex(size / 2, data) for partial list
        list.addToBack("foo");
        list.addToBack("1");
        assertEquals("foo", list.removeAtIndex(1));
        assertArrayListEquals("removeAtIndex", new String[]{"0", "1"}, list);
    }

    @Test(timeout = TIMEOUT)
    public void test_07_get() {
        // test exceptions
        String message = "get must throw java.lang.IndexOutOfBoundsException" +
                " if index is negative or index >= size.";
        for (int i: new int[]{-1, 0, 1}) {
            try {
                list.get(i);
                Assert.fail();
            } catch (Exception e) {
                assertEquals(message, IndexOutOfBoundsException.class, e.getClass());
            }
        }
        // test function
        list.addToBack("0");
        assertEquals("0", list.get(0));
    }

    @Test(timeout = TIMEOUT)
    public void test_08_clear() {
        // test no exceptions
        // test function
        list.addToBack("0");
        list.clear();
        assertArrayListEquals("clear", new String[]{}, list);
    }

}