import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * HashMapStudentTests
 *
 * These tests are NOT exhaustive.
 * You should definitely write your own.
 *
 * @author CS 1332 TAs
 * @version 1.0
 */
public class HashMapStudentTests {

    private HashMap<String, String> directory;
    private static final int TIMEOUT = 200;

    @Before
    public void setUp() {
        directory = new HashMap<>();
    }

    @Test(timeout = TIMEOUT)
    public void testput() {
        directory.put(new String("Jonathan"), "Former TA: 1332");
        assertEquals(1, directory.size());
        assertEquals("Former TA: 1332", directory.get("Jonathan"));
        assertTrue(directory.containsKey(new String("Jonathan")));
    }

    @Test(timeout = TIMEOUT)
    public void testRemove() {
        putStuff();
        assertEquals("TA: 4400", directory.remove(new String("Jonathan")));
        assertFalse(directory.containsKey(new String("Jonathan")));
    }

    @Test(timeout = TIMEOUT)
    public void testGet() {
        putStuff();
        assertEquals("TA: 1332", directory.get(new String("Carey")));
    }

    /**
     * Put a baseline of items to the hash map.
     */
    private void putStuff() {
        directory.put(new String("Carey"), "TA: 1332");
        directory.put(new String("Jonathan"), "TA: 4400");
        directory.put(new String("Siddu"), "TA: 1332");
        directory.put(new String("Mary"), "Professor: 1332");
        directory.put(new String("Monica"), "Professor: 2050");
    }
}
