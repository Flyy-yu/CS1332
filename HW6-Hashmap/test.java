import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for the HashMap assignment.
 *
 * @version 7
 * @author Timothy J. Aveni
 */

public class test {

    private static final int TIMEOUT = 2000; 
    
    HashMap<Integer, Integer> map;
    
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
            boolean foundException = false;
            for (Class<? extends Exception> exceptionClass: exceptionClasses) {
                if (exceptionClass.equals(e.getClass())) {
                    foundException = true;
                }
            }
            
            if (!foundException) {
                e.printStackTrace();
                Assert.fail(message);
            } else {
                  assertNotNull(
                          "Exception messages must not be empty",
                          e.getMessage());
                  assertNotEquals(
                          "Exception messages must not be empty",
                          "",
                          e.getMessage());
            }
        }
    }
    
    private void assertHashMapEquals(String message, String expected, HashMap<Integer, Integer> map) {
        assertHashMapEquals(message, expected, map, Integer::new, Integer::new);
    }
    
    private <K, V> void assertHashMapEquals(String message, String expected, HashMap<K, V> map, Function<String, K> keyResolver, Function<String, V> valueResolver) {
        MapEntry<K, V>[] table = map.getTable();
        String[] cells = Arrays.stream(expected.split(",", -1))
                .map(element -> element.trim())
                .toArray(String[]::new);
        
        assertEquals(
                message + " - The size of the table array was wrong",
                cells.length,
                table.length);
        
        int countAll = 0;
        
        for (int i = 0; i < cells.length; i++) {
            int count = 0;
            MapEntry<K, V> current = table[i];
            
            if (cells[i].isEmpty()) {
                assertNull(
                        " - There were too many entries at index " + i,
                        current);
                continue;
            }
            
            MapEntry<K, V>[] theseEntries = Arrays.stream(cells[i].split(" "))
                    .map(entry -> entry.split(">"))
                    .map(entry -> new MapEntry<K, V>(keyResolver.apply(entry[0]), valueResolver.apply(entry[1])))
                    .toArray(MapEntry[]::new);
            
            while (current != null) {
                assertTrue(
                        message + " - There were too many entries at index " + i,
                        theseEntries.length > count);
                
                assertEquals(
                        message + " - There was an incorrect entry at index " + i + ", entry " + count + " (both zero-indexed)",
                        theseEntries[count],
                        current);
                
                count++;
                current = current.getNext();
            }
            assertEquals(
                    message + " - There were too few entries at index " + i,
                    theseEntries.length,
                    count);
            countAll += count;
        }
        
        assertEquals(
                "size() returned the wrong value",
                countAll,
                map.size());
    }
    
    @Before
    public void init() {
        map = new HashMap<Integer, Integer>();
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
    public void checkConstructor() {
        assertHashMapEquals(
                "The constructor did not correctly initialize the map", 
                ", , , , , , , , , ,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void checkValueConstructor() {
        HashMap<Integer, Integer> smallMap = new HashMap<Integer, Integer>(5);
        assertHashMapEquals(
                "The constructor with initialCapacity did not correctly initialize the map", 
                ", , , ,",
                smallMap);
    }
    
    @Test(timeout = TIMEOUT)
    public void addExceptions() {
        assertException(
                "Adding with a null key should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> map.put(null,  4));
        
        assertException(
                "Adding with a null value should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> map.put(0,  null));
    }
    
    @Test(timeout = TIMEOUT)
    public void addSingle() {
        assertNull(
                "Adding an element failed to return null when the element did not already exist",
                map.put(0, 4));
        
        assertHashMapEquals(
                "Adding a single element did not work", 
                "0>4, , , , , , , , , ,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void addNegativeHashCode() {
        map.put(-1, 4);
        
        assertHashMapEquals(
                "Adding an element with a negative hashCode didn't work: did you remember to take the absolute value of the hashCode?",
                ", -1>4, , , , , , , , ,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void addFour() {
        map.put(0, 4);
        assertNull(
                "Adding an element failed to return null when the element did not already exist",
                map.put(1, 5));
        assertNull(
                "Adding an element failed to return null when the element did not already exist",
                map.put(2, 6));
        assertNull(
                "Adding an element failed to return null when the element did not already exist",
                map.put(3, 7));
        
        assertHashMapEquals(
                "Adding multiple elements did not work", 
                "0>4, 1>5, 2>6, 3>7, , , , , , ,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void addNonContiguous() {
        map.put(0, 4);
        assertNull(
                "Adding an element failed to return null when the element did not already exist",
                map.put(2, 5));
        assertNull(
                "Adding an element failed to return null when the element did not already exist",
                map.put(4, 6));
        assertNull(
                "Adding an element failed to return null when the element did not already exist",
                map.put(6, 7));
        assertNull(
                "Adding an element failed to return null when the element did not already exist",
                map.put(8, 8));
        assertNull(
                "Adding an element failed to return null when the element did not already exist",
                map.put(10, 9));
        
        assertHashMapEquals(
                "Adding five elements across the table did not work", 
                "0>4, , 2>5, , 4>6, , 6>7, , 8>8, , 10>9",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void addLargerThanLength() {
        map.put(0, 4);
        assertNull(
                "Adding an element failed to return null when the element did not already exist",
                map.put(13, 5));
        
        assertHashMapEquals(
                "Adding multiple elements did not work", 
                "0>4, , 13>5, , , , , , , ,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void addOverwrite() {
        Integer four = new Integer(4);
        map.put(0, four);
        Integer old = map.put(0, 5);
        assertSame(
                "Overwriting an element should return the old element",
                four,
                old);
                
        assertHashMapEquals(
                "Overwriting an element did not work", 
                "0>5, , , , , , , , , ,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void addOverwriteNonSameKeys() {
        Integer zero1 = new Integer(0);
        Integer zero2 = new Integer(0);
        map.put(zero1, 4);
        map.put(zero2, 5);
                
        assertHashMapEquals(
                "Overwriting an element did not work when the keys were value-equal but not reference-equal", 
                "0>5, , , , , , , , , ,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void addSameCompressedHash() {
        map.put(0, 4);
        assertNull(
                "Adding an element failed to return null when the element did not already exist (but the element had the same compressed hash as another)",
                map.put(11, 5));
        
        assertHashMapEquals(
                "Adding two elements with the same compressed hash did not work",
                "11>5 0>4, , , , , , , , , ,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void addOverwriteAtLaterLocation() {
        Integer four = new Integer(4);
        map.put(0, four);
        map.put(11, 5);
        
        Integer old = map.put(0, 6);
        
        assertSame(
                "Overwriting the second element in a chain did not return the old element",
                four,
                old);
        
        assertHashMapEquals(
                "Overwriting the second element in a chain did not work",
                "11>5 0>6, , , , , , , , , ,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void addDuplicateValues() {
        Integer four = new Integer(4);
        
        map.put(0, 4);
        map.put(1, four);
        map.put(2, four);
        map.put(12, four);
        
        assertSame(
                "The map misbehaved when adding duplicate values",
                four,
                map.get(1));
        
        assertSame(
                "The map misbehaved when adding duplicate values",
                four,
                map.get(2));
        
        assertSame(
                "The map misbehaved when adding duplicate values",
                four,
                map.get(12));
        
        assertHashMapEquals(
                "The map misbehaved when adding duplicate values",
                "0>4, 12>4 1>4, 2>4, , , , , , , ,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void resize() {
        map.put(0, 4);
        map.put(1, 5);
        map.put(2, 6);
        map.put(3, 7);
        map.put(4, 8);
        map.put(5, 9);
        map.put(6, 10);
        
        assertHashMapEquals(
                "Adding seven elements did not work",
                "0>4, 1>5, 2>6, 3>7, 4>8, 5>9, 6>10, , , ,",
                map);
        
        map.put(7, 11);
        
        assertHashMapEquals(
                "Resizing the backing array did not work",
                "0>4, 1>5, 2>6, 3>7, 4>8, 5>9, 6>10, 7>11, , , , , , , , , , , , , , ,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void resizeEvenIfNoSizeIncrease() {
        Integer four = new Integer(4);
        map.put(0, four);
        map.put(1, 5);
        map.put(2, 6);
        map.put(3, 7);
        map.put(4, 8);
        map.put(5, 9);
        map.put(6, 10);
        
        Integer old = map.put(0, 11);
        
        assertSame(
                "Overwriting an element failed to return the old element when resizing",
                four,
                old);
        
        assertHashMapEquals(
                "The backing array should resize even when put() is modifying an element rather than adding one",
                "0>11, 1>5, 2>6, 3>7, 4>8, 5>9, 6>10, , , , , , , , , , , , , , , ,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void resizeStrictlyGreater() {
        // this is tricky to test because floats aren't exact, but it seems to work here
        HashMap<Integer, Integer> bigMap = new HashMap<Integer, Integer>(100);
        for (int i = 0; i < 67; i++) {
            bigMap.put(i, i);
        }
        
        assertEquals(
                "The backing array should only be resized when the load factor will become greater than .67",
                100,
                bigMap.getTable().length);
        
        bigMap.put(69, 69); // :)
        
        assertEquals(
                "The backing array was not resized when the load factor became .68",
                201,
                bigMap.getTable().length);
    }
    
    @Test(timeout = TIMEOUT)
    public void resizeWithNewHashes1() {
        map.put(0, 4);
        map.put(1, 5);
        map.put(13, 6);
        map.put(3, 7);
        map.put(4, 8);
        map.put(5, 9);
        map.put(6, 10);
        
        // 0>4, 1>5, 13>6, 3>7, 4>8, 5>9, 6>10, , , , 
        
        map.put(7, 11);
        
        assertHashMapEquals(
                "Resizing the backing array used the wrong compressed hashes",
                "0>4, 1>5, , 3>7, 4>8, 5>9, 6>10, 7>11, , , , , , 13>6, , , , , , , , ,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void resizeWithNewHashes2() {
        map.put(0, 4);
        map.put(1, 5);
        map.put(2, 6);
        map.put(13, 7);
        map.put(3, 8);
        map.put(4, 9);
        map.put(5, 10);
        map.put(6, 11);
        
        assertHashMapEquals(
                "Resizing the backing array used the wrong compressed hashes",
                "0>4, 1>5, 2>6, 3>8, 4>9, 5>10, 6>11, , , , , , , 13>7, , , , , , , , ,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void resizeReverseWithSameHash() {
        HashMap<Integer, Integer> smallMap = new HashMap<Integer, Integer>(3);
        smallMap.put(-1, 5);
        smallMap.put(1, 6);
        smallMap.put(0, 4);
        
        assertHashMapEquals(
                "Resizing the array should reverse elements with the same hash (because they are taken left-to-right and then inserted at the head)",
                "0>4, -1>5 1>6, , , , ,",
                smallMap);
    }
    
    @Test(timeout = TIMEOUT)
    public void resizeReverseWhenAddingSameHash() {
        HashMap<Integer, Integer> smallMap = new HashMap<Integer, Integer>(3);
        smallMap.put(0, 4);
        smallMap.put(-1, 5);
        smallMap.put(1, 6);
        
        assertHashMapEquals(
                "Resizing the array should happen before the newest element is added; make sure the order of your operations is correct",
                "0>4, 1>6 -1>5, , , , ,",
                smallMap);
    }
    
    private class SameHashObject {
        private int data;
        
        public SameHashObject(int data) {
            this.data = data;
        }
        
        @Override
        public int hashCode() {
            return 0;
        }
        
        @Override
        public boolean equals(Object other) {
            return data == ((SameHashObject) other).data;
        }
        
        @Override
        public String toString() {
            return "" + data;
        }
    }
    
    @Test(timeout = TIMEOUT)
    public void resizeReverseWhenAddingSameHash2() {
        HashMap<SameHashObject, Integer> smallMap = new HashMap<SameHashObject, Integer>(3);
        SameHashObject key0 = new SameHashObject(0);
        SameHashObject key1 = new SameHashObject(1);
        SameHashObject key2 = new SameHashObject(2);
        smallMap.put(key0, 4);
        smallMap.put(key1, 5);
        smallMap.put(key2, 6);
        
        // after the first add, we have
        //      [0>4, null, null]
        // after the second add, we have
        //      [1>5 -> 0>4, null, null]
        // the third add triggers a resize, which reverses the links with the same hash (@459)
        //      [0>4 -> 1>5, null, null, ...]
        // finally, we add the third element
        //      [2>6 -> 0>4 -> 1>5, null, null, ...]
        
        assertHashMapEquals(
                "Resizing the array should happen before the newest element is added; make sure the order of your operations is correct",
                "2>6 0>4 1>5, , , , , ,",
                smallMap,
                ((Function<Integer, SameHashObject>) SameHashObject::new).compose(Integer::new),
                Integer::new);
    }
    
    private class EqualsChecker {
        private int data;
        private int equalsCount;
        
        public EqualsChecker(int data) {
            this.data = data;
            this.equalsCount = 0;
        }
        
        @Override
        public int hashCode() {
            return 0;
        }
        
        @Override
        public boolean equals(Object other) {
            equalsCount++;
            ((EqualsChecker) other).equalsCount++;
            return data == ((EqualsChecker) other).data;
        }
        
        @Override
        public String toString() {
            return "" + data;
        }
    }
    
    @Test(timeout = TIMEOUT)
    public void clear() {
        map.put(0, 4);
        map.put(1, 5);
        map.put(2, 6);
        map.put(3, 7);
        map.put(4, 8);
        map.put(5, 9);
        map.put(6, 10);
        
        map.clear();
        
        assertHashMapEquals(
                "The clear() method did not work", 
                ", , , , , , , , , ,",
                map);
        
        map.put(0, 4);
        map.put(1, 5);
        map.put(2, 6);
        map.put(3, 7);
        map.put(4, 8);
        map.put(5, 9);
        map.put(6, 10);
        map.put(7, 11);
        
        map.clear();
        
        assertHashMapEquals(
                "The clear() method did not reset the backing array size",
                ", , , , , , , , , ,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void containsKeyException() {
        assertException(
                "Calling containsKey with a null key should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> map.containsKey(null));
    }
    
    @Test(timeout = TIMEOUT)
    public void containsSingle() {
        assertFalse(
                "The containsKey() method returned true on an empty HashMap...",
                map.containsKey(0));
        
        map.put(0, 4);
        assertTrue(
                "The containsKey() method failed to return true when one element was in the HashMap",
                map.containsKey(0));
    }
    
    @Test(timeout = TIMEOUT)
    public void containsNonSameKey() {
        Integer zero1 = new Integer(0);
        Integer zero2 = new Integer(0);
        
        map.put(zero1, 4);
        
        assertTrue(
                "The containsKey() method failed to return true when using a key that was value-equal but not reference-equal",
                map.containsKey(zero2));
    }
    
    @Test(timeout = TIMEOUT)
    public void containsMultiple() {
        map.put(0, 4);
        map.put(2, 5);
        map.put(4, 6);
        
        assertTrue(
                "The containsKey() method failed to return true when the element was in the HashMap",
                map.containsKey(0));
        
        assertTrue(
                "The containsKey() method failed to return true when the element was in the HashMap",
                map.containsKey(2));
        
        assertTrue(
                "The containsKey() method failed to return true when the element was in the HashMap",
                map.containsKey(4));
        
        assertFalse(
                "The containsKey() method failed to return false when the element was not in the HashMap",
                map.containsKey(1));
        
        assertFalse(
                "The containsKey() method failed to return false when the element was not in the HashMap but had the same compressed hash as another",
                map.containsKey(13));
    }
    
    @Test(timeout = TIMEOUT)
    public void containsAfterRemove() {
        map.put(0, 4);
        map.remove(0);
        
        assertFalse(
                "The containsKey() method returned true after removing the element",
                map.containsKey(0));
    }
    
    @Test(timeout = TIMEOUT)
    public void containsAfterClear() {
        map.put(0, 4);
        map.clear();
        
        assertFalse(
                "The containsKey() method returned true after clearing the map",
                map.containsKey(0));
    }
    
    @Test(timeout = TIMEOUT)
    public void containsAfterOverwrite() {
        map.put(0, 4);
        map.put(0, 5);
        
        assertTrue(
                "The containsKey() method failed to return true after overwriting a value",
                map.containsKey(0));
    }
    
    @Test(timeout = TIMEOUT)
    public void containsInsideChain() {
        map.put(0, 4);
        map.put(11, 5);
        map.put(22, 6);
        
        assertTrue(
                "The containsKey() method failed to return true on the first element in the chain",
                map.containsKey(22));
        
        assertTrue(
                "The containsKey() method failed to return true on a middle element in the chain",
                map.containsKey(11));
        
        assertTrue(
                "The containsKey() method failed to return true on the last element in the chain",
                map.containsKey(0));
    }
    
    @Test(timeout = TIMEOUT)
    public void manualException() {
        assertException(
                "Resizing to a negative size should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> map.resizeBackingTable(-2));
        
        assertException(
                "Resizing to a non-positive size (including 0) should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> map.resizeBackingTable(0));
        
        map.put(0, 4);
        map.put(1, 5);
        map.put(2, 6);
        map.put(14, 7);
        map.put(3, 8);
        map.put(4, 9);
        map.put(5, 10);

        assertException(
                "Resizing to a size less than the number of elements should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> map.resizeBackingTable(6));
        
        map.resizeBackingTable(18);
        
        assertHashMapEquals(
                "Resizing the backing array manually didn't work",
                "0>4, 1>5, 2>6, 3>8, 4>9, 5>10, , , , , , , , , 14>7, , ,",
                map);

        map.resizeBackingTable(7);
        
        assertHashMapEquals(
                "Resizing the backing array manually didn't work (should work even if the load factor is still too high)",
                "14>7 0>4, 1>5, 2>6, 3>8, 4>9, 5>10,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void manualResize() {
        // mimics resizeWithNewHashes2
        
        map.put(0, 4);
        map.put(1, 5);
        map.put(2, 6);
        map.put(13, 7);
        map.put(3, 8);
        map.put(4, 9);
        map.put(5, 10);

        map.resizeBackingTable(18);
        
        assertHashMapEquals(
                "Resizing the backing array manually didn't work",
                "0>4, 1>5, 2>6, 3>8, 4>9, 5>10, , , , , , , , 13>7, , , ,",
                map);
    }
    
    
    @Test(timeout = TIMEOUT)
    public void getException() {
        assertException(
                "Calling get with a null key should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> map.get(null));
    }
    
    @Test(timeout = TIMEOUT)
    public void getSingle() {
        assertException(
                "Getting a key that wasn't in the map failed to throw a NoSuchElementException",
                java.util.NoSuchElementException.class,
                () -> map.get(0));
        
        map.put(0, 4);
        
        assertEquals(
                "Getting an element from a map with size 1 failed",
                new Integer(4),
                map.get(0));
        
        assertException(
                "The map failed to throw a NoSuchElementException when getting an element not in the map",
                java.util.NoSuchElementException.class,
                () -> map.get(1));
        
        assertException(
                "The map failed to throw a NoSuchElementException when getting an element not in the map but with the same compressed hash as another",
                java.util.NoSuchElementException.class,
                () -> map.get(11));
    }
    
    @Test(timeout = TIMEOUT)
    public void getNonSameKey() {
        Integer zero1 = new Integer(0);
        Integer zero2 = new Integer(0);
        
        map.put(zero1, 4);
        
        assertEquals(
                "Getting an element from a map with size 1 failed when the key was value-equal but not reference-equal",
                new Integer(4),
                map.get(zero2));
    }
    
    @Test(timeout = TIMEOUT)
    public void getMultiple() {
        map.put(0, 4);
        map.put(2, 5);
        map.put(4, 6);
        
        assertEquals(
                "Getting an element from the map failed",
                new Integer(4),
                map.get(0));
        
        assertEquals(
                "Getting an element from the map failed",
                new Integer(5),
                map.get(2));
        
        assertEquals(
                "Getting an element from the map failed",
                new Integer(6),
                map.get(4));
        
        assertException(
                "The map failed to throw a NoSuchElementException when getting an element not in the map",
                java.util.NoSuchElementException.class,
                () -> map.get(1));
        
        assertException(
                "The map failed to throw a NoSuchElementException when getting an element not in the map but with the same compressed hash as another",
                java.util.NoSuchElementException.class,
                () -> map.get(13));
    }
    
    @Test(timeout = TIMEOUT)
    public void getAfterRemove() {
        map.put(0, 4);
        map.remove(0);
        
        assertException(
                "The get() method failed to throw a NoSuchElementException when getting a removed element",
                java.util.NoSuchElementException.class,
                () -> map.get(0));
    }
    
    @Test(timeout = TIMEOUT)
    public void getAfterClear() {
        map.put(0, 4);
        map.clear();
        
        assertException(
                "The get() method failed to throw a NoSuchElementException after clearing the map",
                java.util.NoSuchElementException.class,
                () -> map.get(0));
    }
    
    @Test(timeout = TIMEOUT)
    public void getAfterOverwrite() {
        map.put(0, 4);
        map.put(0, 5);
        
        assertEquals(
                "The get() method failed to return the correct value after overwriting a value",
                new Integer(5),
                map.get(0));
    }
    
    @Test(timeout = TIMEOUT)
    public void getInsideChain() {
        map.put(0, 4);
        map.put(11, 5);
        map.put(22, 6);
        
        assertEquals(
                "The get() method failed to locate the first element in the chain",
                new Integer(6),
                map.get(22));
        
        assertEquals(
                "The get() method failed to locate a middle element in the chain",
                new Integer(5),
                map.get(11));
        
        assertEquals(
                "The get() method failed to locate the last element in the chain",
                new Integer(4),
                map.get(0));
    }
    
    @Test(timeout = TIMEOUT)
    public void removeException() {
        assertException(
                "Calling remove with a null key should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> map.remove(null));
    }
    
    @Test(timeout = TIMEOUT)
    public void removeSingle() {
        assertException(
                "Removing a key that wasn't in the map failed to throw a NoSuchElementException",
                java.util.NoSuchElementException.class,
                () -> map.remove(0));
        
        map.put(0, 4);
        
        assertEquals(
                "Removing an element from a map with size 1 failed to return the correct value",
                new Integer(4),
                map.remove(0));
        
        assertHashMapEquals(
                "Removing an element from a map with size 1 failed", 
                ", , , , , , , , , ,",
                map);
        
        assertException(
                "Removing a key that wasn't in the map failed to throw a NoSuchElementException",
                java.util.NoSuchElementException.class,
                () -> map.remove(0));
        
        assertException(
                "The map failed to throw a NoSuchElementException when removing an element not in the map",
                java.util.NoSuchElementException.class,
                () -> map.remove(1));
        
        map.put(0, 4);
        
        assertException(
                "The map failed to throw a NoSuchElementException when removing an element not in the map but with the same compressed hash as another",
                java.util.NoSuchElementException.class,
                () -> map.remove(11));
    }
    
    @Test(timeout = TIMEOUT)
    public void removeNonSameKey() {
        Integer zero1 = new Integer(0);
        Integer zero2 = new Integer(0);
        
        map.put(zero1, 4);
        
        assertEquals(
                "Removing an element from a map with size 1 failed to return the correct value when the key was value-equal but not reference-equal",
                new Integer(4),
                map.remove(zero2));

        assertHashMapEquals(
                "Removing an element from a map with size 1 failed when the key was value-equal but not reference-equal",
                ", , , , , , , , , ,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeCorrectValue() {
        Integer four = new Integer(4);
        
        map.put(0, four);
        
        assertSame(
                "Removing an element from a map with size 1 failed to return the same value that was inserted",
                four,
                map.remove(0));
    }
    
    @Test(timeout = TIMEOUT)
    public void removeMultiple() {
        map.put(0, 4);
        map.put(2, 5);
        map.put(4, 6);
        
        assertEquals(
                "Removing an element from the map failed to return the right value",
                new Integer(4),
                map.remove(0));
        
        assertHashMapEquals(
                "Removing an element from a map failed",
                ", , 2>5, , 4>6, , , , , ,",
                map);
        
        assertEquals(
                "Removing an element from the map failed to return the right value",
                new Integer(5),
                map.remove(2));
        
        assertHashMapEquals(
                "Removing an element from a map failed",
                ", , , , 4>6, , , , , ,",
                map);
        
        assertEquals(
                "Removing an element from the map failed to return the right value",
                new Integer(6),
                map.remove(4));
        
        assertHashMapEquals(
                "Removing an element from a map failed",
                ", , , , , , , , , ,",
                map);
        
        assertException(
                "The map failed to throw a NoSuchElementException when removing an element not in the map",
                java.util.NoSuchElementException.class,
                () -> map.get(0));
        
        assertException(
                "The map failed to throw a NoSuchElementException when removing an element not in the map",
                java.util.NoSuchElementException.class,
                () -> map.get(4));
        
        assertException(
                "The map failed to throw a NoSuchElementException when removing an element not in the map",
                java.util.NoSuchElementException.class,
                () -> map.get(6));
        
        map.put(2, 5);
        
        assertException(
                "The map failed to throw a NoSuchElementException when removing an element not in the map but with the same compressed hash as another",
                java.util.NoSuchElementException.class,
                () -> map.get(13));
    }
    
    @Test(timeout = TIMEOUT)
    public void removeAfterClear() {
        map.put(0, 4);
        map.clear();
        
        assertException(
                "The remove() method failed to throw a NoSuchElementException after clearing the map",
                java.util.NoSuchElementException.class,
                () -> map.remove(0));
    }
    
    @Test(timeout = TIMEOUT)
    public void removeAfterOverwrite() {
        map.put(0, 4);
        map.put(0, 5);
        
        assertEquals(
                "The remove() method failed to return the correct value after overwriting a value",
                new Integer(5),
                map.remove(0));
        
        assertHashMapEquals(
                "Removing an element from a map failed after that element had been overwritten",
                ", , , , , , , , , ,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeInsideChain() {
        map.put(0, 4);
        map.put(11, 5);
        map.put(22, 6);
        
        assertEquals(
                "The remove() method failed to return the correct element when it was the first element in the chain",
                new Integer(6),
                map.remove(22));
        
        assertHashMapEquals(
                "The remove() method failed to remove the first element in the chain",
                "11>5 0>4, , , , , , , , , ,",
                map);
        
        map.put(22, 6);
        
        assertEquals(
                "The remove() method failed to return the correct element when it was a middle element in the chain",
                new Integer(5),
                map.remove(11));
        
        assertHashMapEquals(
                "The remove() method failed to remove the middle element in the chain",
                "22>6 0>4, , , , , , , , , ,",
                map);
        
        map.put(11, 5);
        
        assertEquals(
                "The remove() method failed to return the correct element when it was the last element in the chain",
                new Integer(4),
                map.remove(0));
        
        assertHashMapEquals(
                "The remove() method failed to remove the last element in the chain",
                "11>5 22>6, , , , , , , , , ,",
                map);
    }
    
    @Test(timeout = TIMEOUT)
    public void keySet() {
        map.put(0, 4);
        map.put(1, 5);
        map.put(2, 6);
        map.put(13, 7);
        map.put(3, 8);
        map.put(4, 9);
        map.put(5, 10);
        
        Set<Integer> expected = new HashSet<Integer>();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        expected.add(13);
        expected.add(3);
        expected.add(4);
        expected.add(5);
        
        assertEquals(
                "keySet() did not return a correct set of keys",
                expected,
                map.keySet());
        
        assertTrue(
                "keySet() should return an instance of java.util.HashSet",
                map.keySet() instanceof HashSet);
    }
    
    @Test(timeout = TIMEOUT)
    public void values() {
        map.put(0, 4);
        map.put(1, 5);
        map.put(2, 6);
        map.put(13, 7);
        map.put(3, 8);
        map.put(4, 9);
        map.put(5, 10);
        
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(4);
        expected.add(5);
        expected.add(7);
        expected.add(6);
        expected.add(8);
        expected.add(9);
        expected.add(10);
        
        assertEquals(
                "values() did not return a correct list of values",
                expected,
                map.values());
    }
}