import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * This is a basic set of unit tests for DoublyLinkedList. Passing these does
 * NOT guarantee any grade on this assignment. This is only a sanity check to
 * help you get started on the homework and writing JUnits in general.
 *
 * @author The 1332 TAs
 * @version 1.0
 */
public class LinkedListStudentTests {
    private LinkedListInterface<String> list;

    public static final int TIMEOUT = 200;

    @Before
    public void setUp() {
        list = new DoublyLinkedList<String>();
    }

    @Test(timeout = TIMEOUT)
    public void testAddStrings() {
        assertEquals(0, list.size());
        assertNull(list.getHead());

        list.addAtIndex(0, "0a"); //0a
        list.addAtIndex(1, "1a"); //0a 1a
        list.addAtIndex(2, "2a"); //0a 1a 2a
        list.addAtIndex(3, "3a"); //0a 1a 2a 3a

        assertEquals(4, list.size());

        LinkedListNode<String> current = list.getHead();
        assertNotNull(current);
        assertEquals("0a", current.getData());
        assertNull(current.getPrevious());

        current = current.getNext();
        assertNotNull(current);
        assertEquals("1a", current.getData());

        current = current.getNext();
        assertNotNull(current);
        assertEquals("2a", current.getData());

        current = current.getNext();
        assertNotNull(current);
        assertEquals("3a", current.getData());
        assertSame(current, list.getTail());

        assertNull(current.getNext());
    }

    @Test(timeout = TIMEOUT)
    public void testAddStringsFront() {
        assertEquals(0, list.size());

        list.addToFront("0a");
        list.addToFront("1a");
        list.addToFront("2a");
        list.addToFront("3a");
        list.addToFront("4a");
        list.addToFront("5a"); //5a 4a 3a 2a 1a 0a

        assertEquals(6, list.size());

        LinkedListNode<String> current = list.getHead();
        assertNotNull(current);
        assertEquals("5a", current.getData());
        assertNull(current.getPrevious());

        current = current.getNext();
        assertNotNull(current);
        assertEquals("4a", current.getData());

        current = current.getNext();
        assertNotNull(current);
        assertEquals("3a", current.getData());

        current = current.getNext();
        assertNotNull(current);
        assertEquals("2a", current.getData());

        current = current.getNext();
        assertNotNull(current);
        assertEquals("1a", current.getData());

        current = current.getNext();
        assertNotNull(current);
        assertEquals("0a", current.getData());
        assertSame(current, list.getTail());

        assertNull(current.getNext());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveStrings() {
        assertEquals(0, list.size());

        list.addAtIndex(0, "0a");
        list.addAtIndex(1, "1a");
        list.addAtIndex(2, "2a");
        list.addAtIndex(3, "3a");
        list.addAtIndex(4, "4a");
        list.addAtIndex(5, "5a"); //0a 1a 2a 3a 4a 5a

        assertEquals(6, list.size());

        assertEquals("2a", list.removeAtIndex(2)); //0a 1a 3a 4a 5a

        assertEquals(5, list.size());

        LinkedListNode<String> current = list.getHead();
        assertNotNull(current);
        assertEquals("0a", current.getData());
        assertNull(current.getPrevious());

        current = current.getNext();
        assertNotNull(current);
        assertEquals("1a", current.getData());

        current = current.getNext();
        assertNotNull(current);
        assertEquals("3a", current.getData());

        current = current.getNext();
        assertNotNull(current);
        assertEquals("4a", current.getData());

        current = current.getNext();
        assertNotNull(current);
        assertEquals("5a", current.getData());
        assertSame(current, list.getTail());

        assertNull(current.getNext());
    }

    @Test(timeout = TIMEOUT)
    public void testGetGeneral() {
        list.addAtIndex(0, "0a");
        list.addAtIndex(1, "1a");
        list.addAtIndex(2, "2a");
        list.addAtIndex(3, "3a");
        list.addAtIndex(4, "4a"); //0a 1a 2a 3a 4a

        assertEquals("0a", list.get(0));
        assertEquals("1a", list.get(1));
        assertEquals("2a", list.get(2));
        assertEquals("3a", list.get(3));
        assertEquals("4a", list.get(4));
    }

    @Test(timeout = TIMEOUT)
    public void testToArray() {
        String[] expectedItems = new String[10];

        for (int x = 0; x < expectedItems.length; x++) {
            expectedItems[x] = "a" + x;
            list.addToBack(expectedItems[x]);
        }

        Object[] array = list.toArray();
        assertArrayEquals(expectedItems, array);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void removeAllOccurancesNullItemPassed() {
        list.removeAllOccurrences(null);
    }
}
