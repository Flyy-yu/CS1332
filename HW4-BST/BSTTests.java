import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for the CS1332 BST assignment.
 * Now with 100% coverage! - for my code :)
 *
 * @version 2.4
 * @author Timothy J. Aveni
 */

public class BSTTests {

    private static final int TIMEOUT = 2000; 
    
    BST<Integer> bst;
    
    private void assertException(String message, Class<? extends Exception> exceptionClass, Runnable code) {
        try {
            code.run();
            Assert.fail(message);
        } catch (Exception e) {
            assertEquals(message, exceptionClass, e.getClass());
            assertNotNull("Exception messages must not be empty", e.getMessage());
            assertNotEquals("Exception messages must not be empty", "", e.getMessage());
        }
    }
    
    // "5( 2(0, 1), 6( , 7) )" corresponds to:
    /*
     *       5
     *      / \
     *     2   6
     *    / \   \
     *   0   1   7
     */
    
    private void assertBstEquals(String message, String nodeStack, BST<Integer> bst) {
        // trim whitespace
        nodeStack = nodeStack.replaceAll("\\s", "");
        
        checkSubtree(message, nodeStack, bst.getRoot());
        
        Pattern p = Pattern.compile("[0-9]([^0-9]|$)");
        Matcher m = p.matcher(nodeStack);
        int expectedSize = 0;
        while (m.find()) {
            expectedSize++;
        }
        
        assertEquals("The BST reported its size incorrectly", expectedSize, bst.size());
        
        int height = 0;
        int currentHeight = 0;
        for (char c: nodeStack.toCharArray()) {
            if (c == '(') {
                currentHeight++;
                if (currentHeight > height) {
                    height = currentHeight;
                }
            } else if (c == ')') {
                currentHeight--;
            }
        }
        
        if (bst.size() == 0) {
            assertEquals("An empty BST reported its height as something other than -1", -1, bst.height());
        } else {
            assertEquals("The BST reported its height incorrectly", height, bst.height());
        }
    }
    
    private void checkSubtree(String message, String nodeStack, BSTNode<Integer> top) {
        if (nodeStack.isEmpty()) {
            assertNull(message, top);
        } else {
            assertNotNull(message, top);
            int parenIndex = nodeStack.indexOf('(');
            if (parenIndex == -1) {
                assertEquals(message, Integer.parseInt(nodeStack), top.getData().intValue());
                assertNull(message, top.getLeft());
                assertNull(message, top.getRight());
            } else {
                String inner = nodeStack.substring(parenIndex + 1, nodeStack.length() - 1);
                int split;
                int depth = 0;
                for (split = 0; split < inner.length(); split++) {
                    char current = inner.charAt(split); 
                    if (current == '(') {
                        depth++;
                    } else if (current == ')') {
                        depth--;
                    } else if(current == ',') {
                        if (depth == 0) {
                            break;
                        }
                    }
                }
                
                checkSubtree(message, inner.substring(0, split), top.getLeft());
                checkSubtree(message, inner.substring(split + 1), top.getRight());
            }
        }
    }
    
    @Before
    public void init() {
        bst = new BST<Integer>();
    }
    
    @Test(timeout = TIMEOUT)
    public void startEmpty() {
        assertBstEquals("The BST didn't start empty", "", bst);
    }
    
    @Test(timeout = TIMEOUT)
    public void addException() {
        assertException(
                "Adding null to a BST should throw an IllegalArgumentException", 
                IllegalArgumentException.class,
                () -> bst.add(null));
    }
    
    @Test(timeout = TIMEOUT)
    public void addSingle() {
        bst.add(16);
        assertBstEquals("Adding one element to the BST failed", "16", bst);
    }
    
    @Test(timeout = TIMEOUT)
    public void addFirstLevel() {
        bst.add(16);
        bst.add(8);
        bst.add(24);
        assertBstEquals("Adding to the root node of the BST failed", "16(8, 24)", bst);
    }
    
    @Test(timeout = TIMEOUT)
    public void addMultiLevel() {
        bst.add(16);
        bst.add(8);
        bst.add(24);
        bst.add(4);
        bst.add(12);
        bst.add(32);
        bst.add(5);
        bst.add(9);
        bst.add(30);
        bst.add(34);
        bst.add(6);
        bst.add(33);
        
        assertBstEquals("Adding multiple levels to the BST failed", "16( 8( 4( , 5( , 6 ) ), 12( 9, ) ), 24( , 32( 30, 34( 33, ) ) ) )", bst);
    }
    
    @Test(timeout = TIMEOUT)
    public void addDuplicates() {
        bst.add(16);
        bst.add(16);
        assertBstEquals("Adding duplicates to the BST should do nothing", "16", bst);
        
        bst.add(8);
        bst.add(24);
        
        bst.add(8);
        
        assertBstEquals("Adding duplicates to the BST should do nothing", "16(8, 24)", bst);
    }
    
    @Test(timeout = TIMEOUT)
    public void constructorExceptions() {
        assertException(
                "The Collection constructor should throw an IllegalArgumentException when it is called with a null exception",
                IllegalArgumentException.class,
                () -> new BST<Integer>(null));
        
        List<Integer> list = new LinkedList<Integer>();
        
        list.add(16);
        list.add(8);
        list.add(24);
        list.add(4);
        list.add(12);
        list.add(32);
        list.add(5);
        list.add(null);
        list.add(30);
        list.add(34);
        list.add(6);
        list.add(33);
        
        assertException(
                "The Collection constructor should throw an IllegalArgumentException when it is called with a list containing nulls",
                IllegalArgumentException.class,
                () -> new BST<Integer>(list));
    }
    
    @Test(timeout = TIMEOUT)
    public void collectionConstructor() {
        List<Integer> list = new LinkedList<Integer>();
        
        list.add(16);
        list.add(8);
        list.add(24);
        list.add(4);
        list.add(12);
        list.add(32);
        list.add(5);
        list.add(9);
        list.add(30);
        list.add(34);
        list.add(6);
        list.add(33);
        
        list.add(16);   // test for duplicates
        
        BST<Integer> collectionConstructedBst = new BST<Integer>(list);
        assertBstEquals("Adding to the BST using the constructor failed", "16( 8( 4( , 5( , 6 ) ), 12( 9, ) ), 24( , 32( 30, 34( 33, ) ) ) )", collectionConstructedBst);
    }
    
    @Test(timeout = TIMEOUT)
    public void clear() {
        bst.add(16);
        bst.add(8);
        bst.add(24);
        bst.add(4);
        bst.add(12);
        bst.add(32);
        bst.add(5);
        bst.add(9);
        bst.add(30);
        bst.add(34);
        bst.add(6);
        bst.add(33);
        
        bst.clear();
        
        assertBstEquals("The clear() method failed", "", bst);
    }
    
    @Test(timeout = TIMEOUT)
    public void height1() {
        assertEquals("Height was not -1 for the empty BST", -1, bst.height());
        
        bst.add(16);
        
        assertEquals("Height was not 0 with one (root) node", 0, bst.height());
        
        bst.add(8);
        bst.add(24);
        bst.add(4);
        bst.add(12);
        bst.add(32);
        bst.add(5);
        bst.add(9);
        bst.add(30);
        bst.add(34);
        bst.add(6);
        bst.add(33);
        
        assertEquals("BST height was not correct", 4, bst.height());
    }
    
    @Test(timeout = TIMEOUT)
    public void height2() {
        // also test a tree with worse balance
        bst.add(16);
        bst.add(8);
        bst.add(24);
        bst.add(4);
        bst.add(12);
        bst.add(32);
        bst.add(30);
        bst.add(34);
        bst.add(33);
        
        assertEquals("BST height was not correct", 4, bst.height());
    }
    
    @Test(timeout = TIMEOUT)
    public void containsException() {
        assertException(
                "The BST failed to throw an IllegalArgumentException when calling contains(null) on an empty BST",
                IllegalArgumentException.class,
                () -> bst.contains(null));
        
        bst.add(16);
        
        assertException(
                "The BST failed to throw an IllegalArgumentException when calling contains(null)",
                IllegalArgumentException.class,
                () -> bst.contains(null));
    }
    
    @Test(timeout = TIMEOUT)
    public void contains() {
        // depends on a working collection constructor
        
        for (int i = 0; i < 100; i++)
            assertFalse("An empty BST claimed to contain " + i, bst.contains(i));
        
        List<Integer> list = new LinkedList<Integer>();
        
        list.add(16);
        list.add(8);
        list.add(24);
        list.add(4);
        list.add(12);
        list.add(32);
        list.add(5);
        list.add(9);
        list.add(30);
        list.add(34);
        list.add(6);
        list.add(33);
        
        bst = new BST<Integer>(list);
        
        for (int i = 0; i < 40; i++) {
            if (list.contains(i)) {
                assertTrue("contains() returned false for an element the BST contains", bst.contains(i));
            } else {
                assertFalse("contains() returned true for an element the BST does not contain", bst.contains(i));
            }
        }
    }
    
    @Test(timeout = TIMEOUT)
    public void getException() {
        assertException(
                "The BST failed to throw an IllegalArgumentException when calling get(null) on an empty BST",
                IllegalArgumentException.class,
                () -> bst.get(null));
        
        bst.add(16);
        
        assertException(
                "The BST failed to throw an IllegalArgumentException when calling get(null)",
                IllegalArgumentException.class,
                () -> bst.get(null));
    }
    
    @Test(timeout = TIMEOUT)
    public void get() {
        // depends on a working collection constructor
        
        for (int i = 0; i < 100; i++) {
            int closureI = i;
            assertException(
                    "Getting an element not in an empty BST failed to throw a java.util.NoSuchElementException",
                    NoSuchElementException.class,
                    () -> bst.get(closureI));
        }
        
        List<Integer> list = new LinkedList<Integer>();
        
        list.add(16);
        list.add(8);
        list.add(24);
        list.add(4);
        list.add(12);
        list.add(32);
        list.add(5);
        list.add(9);
        list.add(30);
        list.add(34);
        list.add(6);
        list.add(33);
        
        bst = new BST<Integer>(list);
        
        for (int i = 0; i < 40; i++) {
            int closureI = i;
            if (list.contains(i)) {
                try {
                    assertEquals("get() returned the wrong data", i, bst.get(i).intValue());
                } catch (Exception e) {
                    Assert.fail("get() threw an exception for a data element in the BST: " + e);
                }
            } else {
                assertException(
                        "get() failed to throw a NoSuchElementException in a non-empty list",
                        NoSuchElementException.class,
                        () -> bst.get(closureI));
            }
        }
    }
    
    @Test(timeout = TIMEOUT)
    public void removeException() {
        assertException(
                "Removing null from an empty BST failed to throw an IllegalArgumentException", 
                IllegalArgumentException.class,
                () -> bst.remove(null));
        
        bst.add(16);
        
        assertException(
                "Removing null from a BST failed to throw an IllegalArgumentException", 
                IllegalArgumentException.class,
                () -> bst.remove(null));
    }
    
    @Test(timeout = TIMEOUT)
    public void removeLeaf() {
        assertException(
                "Removing from an empty BST failed to throw a NoSuchElementException", 
                NoSuchElementException.class,
                () -> bst.remove(16));
        
        bst.add(16);
        bst.add(8);
        bst.add(24);
        bst.add(4);
        bst.add(12);
        bst.add(32);
        bst.add(5);
        bst.add(9);
        bst.add(30);
        bst.add(34);
        bst.add(6);
        bst.add(33);
        
        assertException(
                "Removing a nonexistent element from a BST failed to throw a NoSuchElementException", 
                NoSuchElementException.class,
                () -> bst.remove(17));
        
        bst.remove(33);
        
        assertBstEquals("Removing a leaf node failed", "16( 8( 4( , 5( , 6 ) ), 12( 9, ) ), 24( , 32( 30, 34 ) ) )", bst);
        
        bst.remove(6);
        
        assertBstEquals("Removing a leaf node failed", "16( 8( 4( , 5), 12( 9, ) ), 24( , 32( 30, 34 ) ) )", bst);
        
    }
    
    @Test(timeout = TIMEOUT)
    public void removeNodeWithSingle() {
        // four cases: single child is on left or right, and this node is left or right of parent
        
        bst.add(16);
        bst.add(8);
        bst.add(24);
        bst.add(4);
        bst.add(12);
        bst.add(32);
        bst.add(5);
        bst.add(10);
        bst.add(30);
        bst.add(34);
        bst.add(6);
        bst.add(33);
        
        bst.remove(4);
        
        assertBstEquals("Removing an inner node with one child failed", "16( 8( 5( , 6 ), 12( 10, ) ), 24( , 32( 30, 34( 33, ) ) ) )", bst);
        
        bst.remove(34);
        
        assertBstEquals("Removing an inner node with one child failed", "16( 8( 5( , 6 ), 12( 10, ) ), 24( , 32( 30, 33 ) ) )", bst);
        
        bst.add(9);
        bst.remove(10);
        
        assertBstEquals("Removing an inner node with one child failed", "16( 8( 5( , 6 ), 12( 9, ) ), 24( , 32( 30, 33 ) ) )", bst);
        
        bst.add(34);
        bst.remove(33);
        
        assertBstEquals("Removing an inner node with one child failed", "16( 8( 5( , 6 ), 12( 9, ) ), 24( , 32( 30, 34 ) ) )", bst);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeNodeWithTwoChildren1() { 
        bst.add(16);
        bst.add(8);
        bst.add(24);
        bst.add(4);
        bst.add(12);
        bst.add(32);
        bst.add(5);
        bst.add(9);
        bst.add(30);
        bst.add(34);
        bst.add(6);
        bst.add(33);
        
        bst.remove(8);
        
        assertBstEquals("Removing an inner node with two children failed", "16( 6( 4( , 5 ), 12( 9, ) ), 24( , 32( 30, 34( 33, ) ) ) )", bst);
        
        bst.remove(33);
        // test when the predecessor is directly to the left of the removed node
        bst.remove(32);
        
        assertBstEquals("Removing an inner node with two children failed", "16( 6( 4( , 5 ), 12( 9, ) ), 24( , 30( , 34 ) ) )", bst);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeNodeWithTwoChildren2() {
    	bst.add(6);
    	bst.add(1);
    	bst.add(7);
    	bst.add(0);
    	bst.add(5);
    	bst.add(3);
    	bst.add(2);
    	bst.add(4);

        // == 6( 1( 0, 5( 3(2, 4), ) ), 7 )
        
        bst.remove(6);
        
        assertBstEquals("Removing a node with two children failed where the predecessor had children", "5( 1( 0, 3(2, 4) ), 7 )", bst);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeLastNode() {
    	bst.add(6);
    	bst.remove(6);
    	
    	assertBstEquals("Removing the last node failed: did you reset root to null?", "", bst);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeHeight() {
        bst.add(16);
        bst.add(8);
        bst.add(24);
        bst.add(4);
        bst.add(12);
        bst.add(32);
        bst.add(5);
        bst.add(9);
        bst.add(30);
        bst.add(34);
        bst.add(6);
        bst.add(33);
        
        bst.remove(33);
        bst.remove(6);
        
        assertEquals("Height was miscalculated after removals", 3, bst.height());
    }
    
    @Test(timeout = TIMEOUT)
    public void removeHeight2() {
    	bst.add(6);
    	bst.add(1);
    	bst.add(7);
    	bst.add(0);
    	bst.add(5);
    	bst.add(3);
    	bst.add(2);
    	bst.add(4);

        // == 6( 1( 0, 5( 3(2, 4), ) ), 7 )
        
        bst.remove(6);
        
       assertEquals("Height was miscalculated after removals", 3, bst.height());
    }
    
    @Test(timeout = TIMEOUT)
    public void removeRoot1() {
        // Some simple tests to test removal of the root node as a leaf and with one child
        bst.add(1);
        bst.add(3);
        bst.add(2);
        /*    1
         *     \
         *      3
         *     /
         *    2
         */
        
        bst.remove(1);
        assertBstEquals("Removal of the root failed when it had one child on the right", "3( 2, )", bst);
        
        bst.remove(3);
        assertBstEquals("Removal of the root failed when it had one child on the left", "2", bst);
        
        // essentially the same as the "removeLastNode" test
        bst.remove(2);
        assertBstEquals("Removing the last node failed: did you reset root to null?", "", bst);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeRoot2() {
        // Some simple tests to test removal of the root node with two children
        // properties of the predecessor:
        //      it never has a right child
        //      it is never the left child, except when the immediate parent is the node being removed
        //      it is never the right child when the immediate parent is the node being removed
        // the tests:
        //                                      predecessor on left of parent  |   predecessor on right of parent
        // predecessor has no children          bst.remove(2)                      bst.remove(4)
        // predecessor has one (left) child     bst.remove(3)                      bst.remove(5)          
        bst.add(6);
        bst.add(2);
        bst.add(7);
        bst.add(1);
        bst.add(4);
        bst.add(3);
        bst.add(5);
        /*       6
         *      / \
         *     2   7
         *    / \
         *   1   4
         *      / \
         *     3   5
         */
        
        bst.remove(6);
        assertBstEquals("Removal of the root failed when it had two children with the predecessor as a leaf node", "5( 2( 1, 4( 3, ) ), 7 )", bst);
        
        bst.remove(5);
        assertBstEquals("Removal of the root failed when it had two children with the predecessor having one child", "4( 2( 1, 3 ), 7 )", bst);
        
        bst.remove(4);
        assertBstEquals("Removal of the root failed when it had two children with the predecessor as a leaf node", "3( 2( 1, ), 7 )", bst);
        
        bst.remove(3);
        assertBstEquals("Removal of the root failed when it had two children with the predecessor as the immediate left child of the removed node, and with the predecessor having a child", "2( 1, 7 )", bst);
        
        bst.remove(2);
        assertBstEquals("Removal of the root failed when it had two children with the predecessor as the immediate left child of the removed node, and with the predecessor a leaf node", "1( , 7 )", bst);
        
        bst.remove(1);
        assertBstEquals("Removal of the root failed when it had one child on the left", "7", bst);
        
        bst.remove(7);
        assertBstEquals("Removing the last node failed: did you reset root to null?", "", bst);
    }
    
    @Test(timeout = TIMEOUT)
    public void preorder() {
    	assertTrue("Calling preorder() on an empty BST did not give an empty list", bst.preorder().isEmpty());
    	
    	bst.add(16);
        bst.add(8);
        bst.add(24);
        bst.add(4);
        bst.add(12);
        bst.add(32);
        bst.add(5);
        bst.add(9);
        bst.add(30);
        bst.add(34);
        bst.add(6);
        bst.add(33);
        
        assertArrayEquals(bst.preorder().toArray(), new Integer[]{16, 8, 4, 5, 6, 12, 9, 24, 32, 30, 34, 33});
    }
    
    @Test(timeout = TIMEOUT)
    public void postorder() {
    	assertTrue("Calling postorder() on an empty BST did not give an empty list", bst.postorder().isEmpty());
    	
    	bst.add(16);
        bst.add(8);
        bst.add(24);
        bst.add(4);
        bst.add(12);
        bst.add(32);
        bst.add(5);
        bst.add(9);
        bst.add(30);
        bst.add(34);
        bst.add(6);
        bst.add(33);
        
        assertArrayEquals(bst.postorder().toArray(), new Integer[]{6, 5, 4, 9, 12, 8, 30, 33, 34, 32, 24, 16});
    }
    
    @Test(timeout = TIMEOUT)
    public void inorder() {
    	assertTrue("Calling inorder() on an empty BST did not give an empty list", bst.inorder().isEmpty());
    	
    	bst.add(16);
        bst.add(8);
        bst.add(24);
        bst.add(4);
        bst.add(12);
        bst.add(32);
        bst.add(5);
        bst.add(9);
        bst.add(30);
        bst.add(34);
        bst.add(6);
        bst.add(33);
        
        assertArrayEquals(bst.inorder().toArray(), new Integer[]{4, 5, 6, 8, 9, 12, 16, 24, 30, 32, 33, 34});
    }
    
    @Test(timeout = TIMEOUT)
    public void levelorder() {
    	assertTrue("Calling levelorder() on an empty BST did not give an empty list", bst.levelorder().isEmpty());
    	
    	bst.add(16);
        bst.add(8);
        bst.add(24);
        bst.add(4);
        bst.add(12);
        bst.add(32);
        bst.add(5);
        bst.add(9);
        bst.add(30);
        bst.add(34);
        bst.add(6);
        bst.add(33);
        
        assertArrayEquals(bst.levelorder().toArray(), new Integer[]{16, 8, 24, 4, 12, 32, 5, 9, 30, 34, 6, 33});
    }
    
    private class EqualObject implements Comparable<EqualObject> {
    	int data;
    	public EqualObject(int d) {
    		data = d;
    	}
    	@Override
    	public boolean equals(Object other) {
    		return true;
    	}
		@Override
		public int compareTo(EqualObject other) {
			return 0;
		}
    }
    
    @Test(timeout = TIMEOUT)
    public void differentElements() {
    	BST<EqualObject> differenceTest = new BST<EqualObject>();
    	
    	EqualObject one = new EqualObject(1);
    	EqualObject two = new EqualObject(2);
    	
    	differenceTest.add(one);
    	assertTrue("BST returned false for contains() when two objects of the same value were compared", differenceTest.contains(two));
    	assertEquals("get() returned the data element passed as a parameter instead of the one obtained from the BST", 1, differenceTest.get(two).data);
    	assertEquals("remove() returned the data element passed as a parameter instead of the one obtained from the BST", 1, differenceTest.get(two).data);
    	assertBstEquals("remove() did not remove a data element when another object of the same value was removed", "", bst);
    }
    
    @Test(timeout = TIMEOUT)
    public void largeScaleTest() {
    	SortedSet<Integer> set = new TreeSet<Integer>();
    	LinkedList<Integer> additionList = new LinkedList<Integer>();
    	LinkedList<Integer> removalList = new LinkedList<Integer>();
    	
    	for (int i = 0; i < 2000; i++) {
    		Integer val = (int) (Math.random() * 2000);
    		// add a bunch of random ints to the sorted set, ensuring duplicates are removed
    		set.add(val);
    		bst.add(val);
    		additionList.add(val);
    	}
    	
    	assertArrayEquals("The large-scale test failed after additions: " + additionList, set.toArray(), bst.inorder().toArray());
    	
    	for (int i = 0; i < 300; i++) {
    	    Integer val = (int) (Math.random() * 2000);
    	    if (set.contains(val)) {
    	        set.remove(val);
    	        bst.remove(val);
    	        removalList.add(val);
    	    } else {
    	        assertException(
    	                "A removal of an element not present in the BST (" + val + ") failed to throw a NoSuchElementException in the large-scale test after additions: " + additionList + " and removals: " + removalList,
    	                NoSuchElementException.class,
    	                () -> bst.remove(val));
    	    }
    	}
    	
    	assertArrayEquals("The large-scale test failed after additions: " + additionList + " and removals: " + removalList, set.toArray(), bst.inorder().toArray());
    	
    	while (bst.size() != 0) {
    	    Integer top = bst.getRoot().getData();
    	    bst.remove(top);
    	    set.remove(top);
    	    removalList.add(top);
    	    
    	    assertArrayEquals("The large-scale test failed while removing the root (" + top + ") after additions: " + additionList + " and removals: " + removalList, set.toArray(), bst.inorder().toArray());
    	}
    }
    
}