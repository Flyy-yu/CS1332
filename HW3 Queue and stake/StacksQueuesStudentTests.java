import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Basic tests for the array-backed stack and queue classes.
 *
 * @author CS 1332 TAs
 * @version 1.0
 */
public class StacksQueuesStudentTests {

    private StackInterface<Integer> stack;
    private QueueInterface<Integer> queue;

    public static final int TIMEOUT = 200;

    @Test(timeout = TIMEOUT)
    public void testArrayStackPush() {
        stack = new ArrayStack<>();
        assertEquals(0, stack.size());

        stack.push(34);
        stack.push(29);
        stack.push(48);
        stack.push(59);

        assertEquals(4, stack.size());

        Object[] backingArray = ((ArrayStack<Integer>) stack).getBackingArray();

        Object[] expected = new Object[10];
        expected[0] = 34;
        expected[1] = 29;
        expected[2] = 48;
        expected[3] = 59;

        assertArrayEquals(expected, backingArray);
    }

    @Test(timeout = TIMEOUT)
    public void testArrayStackPop() {
        stack = new ArrayStack<>();
        assertEquals(0, stack.size());

        stack.push(34);
        stack.push(29);
        stack.push(48);
        stack.push(59);
        assertEquals((Integer) 59, stack.pop());

        assertEquals(3, stack.size());

        Object[] backingArray = ((ArrayStack<Integer>) stack).getBackingArray();

        Object[] expected = new Object[10];
        expected[0] = 34;
        expected[1] = 29;
        expected[2] = 48;

        assertArrayEquals(expected, backingArray);
    }

    @Test(timeout = TIMEOUT)
    public void testArrayQueueEnqueue() {
        queue = new ArrayQueue<>();
        assertEquals(0, queue.size());

        queue.enqueue(34);
        queue.enqueue(29);
        queue.enqueue(48);
        queue.enqueue(59);

        assertEquals(4, queue.size());

        Object[] backingArray = ((ArrayQueue<Integer>) queue).getBackingArray();

        Object[] expected = new Object[10];
        expected[0] = 34;
        expected[1] = 29;
        expected[2] = 48;
        expected[3] = 59;

        assertArrayEquals(expected, backingArray);
    }

    @Test(timeout = TIMEOUT)
    public void testArrayQueueDequeue() {
        queue = new ArrayQueue<>();
        assertEquals(0, queue.size());

        queue.enqueue(34);
        queue.enqueue(29);
        queue.enqueue(48);
        queue.enqueue(59);
        assertEquals((Integer) 34, queue.dequeue());

        assertEquals(3, queue.size());

        Object[] backingArray = ((ArrayQueue<Integer>) queue).getBackingArray();

        Object[] expected = new Object[10];
        expected[1] = 29;
        expected[2] = 48;
        expected[3] = 59;

        assertArrayEquals(expected, backingArray);
    }
}
