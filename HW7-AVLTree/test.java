import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * JUnit test cases for the AVL assignment.
 *
 * @version 4
 * @author Timothy J. Aveni
 */

public class test {

    private static final int TIMEOUT = 2000; 
    
    AVL<Integer> avl;
    
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
    
    @Rule
    public TestWatcher watcher = new TestWatcher() {
        @Override
        public void failed(Throwable e, Description description) {
            String avlString = getAvlString(avl);
            System.out.println(description.getMethodName() + " failed: http://timothyaveni.com/avl/#" + description.getMethodName() + "/" + avlString);
        }
    };
    
    private <T extends Comparable<? super T>> String getAvlString(AVLInterface<T> avl) {
        Set<T> alreadyInTree = new HashSet<T>();
        return subAvlString(avl.getRoot(), alreadyInTree);
    }
    
    private <T extends Comparable<? super T>> String subAvlString(AVLNode<T> top, Set<T> alreadyInTree) {
        if (top == null) {
            return "";
        }
        
        if (alreadyInTree.contains(top.getData())) {
            return top.getData() + "_" + top.getHeight() + "_" + top.getBalanceFactor() + "(,)";
        } else {
            alreadyInTree.add(top.getData());
            return top.getData() + "_" + top.getHeight() + "_" + top.getBalanceFactor() + "(" + subAvlString(top.getLeft(), alreadyInTree) + "," + subAvlString(top.getRight(), alreadyInTree) + ")";
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
    
    private <T extends Comparable<? super T>> void assertBstEquals(String message, T[] expected, AVL<T> avl) {
        int height = assertSubtreeEquals(message, expected, 0, expected.length, avl.getRoot());
        
        assertEquals(
                "The tree's size was out of sync",
                expected.length,
                avl.size());
        
        assertEquals(
                "The tree's height() method failed to return the correct height",
                height,
                avl.height());
    }
    
    private <T extends Comparable<? super T>> int assertSubtreeEquals(String message, T[] expected, int startIndex, int endIndex, AVLNode<T> top) {
        if (top == null) {
            assertEquals(0, endIndex - startIndex);
            return -1;
        } else if (endIndex - startIndex == 0) {
            Assert.fail();
            return -1;
        }
        
        assertEquals(expected[startIndex], top.getData());
        
        int i = startIndex + 1;
        int finalLeftIndex = i;
        while (i < endIndex) {
            if (expected[i].compareTo(top.getData()) < 0) {
                finalLeftIndex++;
            }
            i++;
        }
        
        int leftHeight = assertSubtreeEquals(message, expected, startIndex + 1, finalLeftIndex, top.getLeft());
        int rightHeight = assertSubtreeEquals(message, expected, finalLeftIndex, endIndex, top.getRight());
        
        assertEquals(
                "Heights were out of sync",
                1 + Math.max(leftHeight, rightHeight),
                top.getHeight());
        
        assertEquals(
                "Balance factor was incorrect",
                leftHeight - rightHeight,
                top.getBalanceFactor());
        
        return top.getHeight();
    }

    private void add(Integer ... objects) {
        for (Integer object: objects) {
            avl.add(object);
        }
    }

    @Before
    public void init() {
        avl = new AVL<Integer>();
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
    public void addOne() {
        add(1);
        
        assertBstEquals(
                "Adding one element to the AVL failed",
                new Integer[]{1},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void addSinglyUnbalanced1() {
        add(2, 1);
        
        assertBstEquals(
                "Adding two elements, with a height differing by one, to the AVL failed",
                new Integer[]{2, 1},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void addSinglyUnbalanced2() {
        add(1, 2);
        
        assertBstEquals(
                "Adding two elements, with a height differing by one, to the AVL failed",
                new Integer[]{1, 2},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void addSinglyUnbalanced3() {
        add(3, 2, 4, 1);
        
        assertBstEquals(
                "Adding four elements, with subtree heights differing by one, to the AVL failed",
                new Integer[]{3, 2, 1, 4},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void addSinglyUnbalanced4() {
        add(3, 1, 4, 2);
        
        assertBstEquals(
                "Adding four elements, with subtree heights differing by one, to the AVL failed",
                new Integer[]{3, 1, 2, 4},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void addSinglyUnbalanced5() {
        add(2, 1, 4, 3);
        
        assertBstEquals(
                "Adding four elements, with subtree heights differing by one, to the AVL failed",
                new Integer[]{2, 1, 4, 3},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void addSinglyUnbalanced6() {
        add(2, 1, 3, 4);
        
        assertBstEquals(
                "Adding four elements, with subtree heights differing by one, to the AVL failed",
                new Integer[]{2, 1, 3, 4},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void addTwoLevels() {
        add(2, 1, 3);
        
        assertBstEquals(
                "Adding two levels of elements to the AVL failed",
                new Integer[]{2, 1, 3},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void addThreeLevels() {
        add(4, 2, 6, 1, 3, 5, 7);
        
        assertBstEquals(
                "Adding two levels of elements to the AVL failed",
                new Integer[]{4, 2, 1, 3, 6, 5, 7},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void rightRotateNoParent() {
        add(3, 2, 1);
        
        assertBstEquals(
                "A simple right rotation with no parent failed",
                new Integer[]{2, 1, 3},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void leftRotateNoParent() {
        add(1, 2, 3);
        
        assertBstEquals(
                "A simple left rotation with no parent failed",
                new Integer[]{2, 1, 3},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void doubleRightRotateNoParent() {
        add(3, 1, 2);
        
        assertBstEquals(
                "A double right rotation with no parent failed",
                new Integer[]{2, 1, 3},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void doubleLeftRotateNoParent() {
        add(1, 3, 2);
        
        assertBstEquals(
                "A double left rotation with no parent failed",
                new Integer[]{2, 1, 3},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void rightRotateOnLeft() {
        add(4, 3, 5, 2, 1);
        
        assertBstEquals(
                "A right rotation on the root's left failed",
                new Integer[]{4, 2, 1, 3, 5},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void leftRotateOnLeft() {
        add(4, 1, 5, 2, 3);
        
        assertBstEquals(
                "A left rotation on the root's left failed",
                new Integer[]{4, 2, 1, 3, 5},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void doubleRightRotateOnLeft() {
        add(4, 3, 5, 1, 2);
        
        assertBstEquals(
                "A double right rotation on the root's left failed",
                new Integer[]{4, 2, 1, 3, 5},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void doubleLeftRotateOnLeft() {
        add(4, 1, 5, 3, 2);
        
        assertBstEquals(
                "A double left rotation on the root's left failed",
                new Integer[]{4, 2, 1, 3, 5},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void rightRotateOnRight() {
        add(2, 1, 5, 4, 3);
        
        assertBstEquals(
                "A right rotation on the root's right failed",
                new Integer[]{2, 1, 4, 3, 5},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void leftRotateOnRight() {
        add(2, 1, 3, 4, 5);
        
        assertBstEquals(
                "A left rotation on the root's right failed",
                new Integer[]{2, 1, 4, 3, 5},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void doubleRightRotateOnRight() {
        add(2, 1, 5, 3, 4);
        
        assertBstEquals(
                "A double right rotation on the root's right failed",
                new Integer[]{2, 1, 4, 3, 5},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void doubleLeftRotateOnRight() {
        add(2, 1, 3, 5, 4);
        
        assertBstEquals(
                "A double left rotation on the root's right failed",
                new Integer[]{2, 1, 4, 3, 5},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void rightRotateWithRightChildOnLeft() {
        add(5, 3, 6, 2, 4, 1);
        
        assertBstEquals(
                "A right rotation on the root's left failed when the subtree had a right child",
                new Integer[]{3, 2, 1, 5, 4, 6},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void rightRotateWithRightChildOnLeft2() {
        add(5, 3, 6, 1, 4, 2);
        
        assertBstEquals(
                "A right rotation on the root's left failed when the subtree had a right child",
                new Integer[]{3, 1, 2, 5, 4, 6},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void doubleRightRotateWithLeftChildOnLeft() {
        add(5, 2, 6, 1, 4, 3);
        
        assertBstEquals(
                "A double right rotation on the root's left failed when the subtree had a left child",
                new Integer[]{4, 2, 1, 3, 5, 6},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void doubleRightRotateWithLeftChildOnLeft2() {
        add(5, 2, 6, 1, 3, 4);
        
        assertBstEquals(
                "A double right rotation on the root's left failed when the subtree had a left child",
                new Integer[]{3, 2, 1, 5, 4, 6},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void leftRotateWithLeftChildOnRight() {
        add(2, 1, 4, 3, 5, 6);
        
        assertBstEquals(
                "A left rotation on the root's right failed when the subtree had a left child",
                new Integer[]{4, 2, 1, 3, 5, 6},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void leftRotateWithLeftChildOnRight2() {
        add(2, 1, 4, 3, 6, 5);
        
        assertBstEquals(
                "A left rotation on the root's right failed when the subtree had a left child",
                new Integer[]{4, 2, 1, 3, 6, 5},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void doubleLeftRotateWithRightChildOnRight() {
        add(2, 1, 5, 3, 6, 4);
        
        assertBstEquals(
                "A double left rotation on the root's right failed when the subtree had a right child",
                new Integer[]{3, 2, 1, 5, 4, 6},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void doubleLeftRotateWithRightChildOnRight2() {
        add(2, 1, 5, 4, 6, 3);
        
        assertBstEquals(
                "A double left rotation on the root's right failed when the subtree had a right child",
                new Integer[]{4, 2, 1, 3, 5, 6},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void simpleRemoveRightLeaf() {
        add(2, 1, 3);
        
        String action = "Removing a right leaf from the AVL";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(3),
                avl.remove(3));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{2, 1},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void simpleRemoveLeftLeaf() {
        add(2, 1, 3);
        
        String action = "Removing a left leaf from the AVL";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(1),
                avl.remove(1));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{2, 3},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void simpleRemoveLeafAtRoot() {
        add(1);
        
        String action = "Removing the final node from the AVL";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(1),
                avl.remove(1));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void simpleRemoveLeftInternalWithLeft() {
        add(3, 2, 4, 1);

        String action = "Removing an internal node with a left child on the left of the root";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(2),
                avl.remove(2));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{3, 1, 4},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void simpleRemoveLeftInternalWithRight() {
        add(3, 1, 4, 2);

        String action = "Removing an internal node with a right child on the left of the root";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(1),
                avl.remove(1));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{3, 2, 4},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void simpleRemoveRightInternalWithLeft() {
        add(2, 1, 4, 3);

        String action = "Removing an internal node with a left child on the right of the root";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(4),
                avl.remove(4));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{2, 1, 3},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void simpleRemoveRightInternalWithRight() {
        add(2, 1, 3, 4);

        String action = "Removing an internal node with a right child on the right of the root";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(3),
                avl.remove(3));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{2, 1, 4},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void simpleRemoveRootWithLeft() {
        add(2, 1);

        String action = "Removing the root node with a left child";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(2),
                avl.remove(2));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{1},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void simpleRemoveRootWithRight() {
        add(1, 2);

        String action = "Removing the root node with a right child";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(1),
                avl.remove(1));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{2},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void simpleRemoveWithPredecessorOnLeft() {
        add(4, 2, 5, 1, 3);

        String action = "Removing a node with two children with the predecessor directly to the left of the removed node";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(2),
                avl.remove(2));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{4, 1, 3, 5},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void simpleRemoveRootWithPredecessorOnLeft() {
        add(2, 1, 3);

        String action = "Removing the root with two children with the predecessor directly to the left of the root";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(2),
                avl.remove(2));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{1, 3},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void simpleRemoveWithPredecessorInSubtree() {
        // this is a really big tree because I didn't want to trigger any rotates,
        //     but I did need some pretty serious depth in the testing subtree.
        add(8, 5, 10, 2, 6, 9, 12, 1, 4, 7, 11, 13, 3);

        String action = "Removing a node with two children with the predecessor in the subtree and having a child";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(5),
                avl.remove(5));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 4, 2, 1, 3, 6, 7, 10, 9, 12, 11, 13},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void simpleRemoveRootWithPredecessorInSubtree() {
        add(5, 2, 6, 1, 4, 7, 3);

        String action = "Removing the root with two children with the predecessor in the subtree and having a child";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(5),
                avl.remove(5));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{4, 2, 1, 3, 6, 7},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void simpleRemoveWithPredecessorTwoLevelsDeep() {
        add(5, 2, 6, 1, 3, 7, 4);

        String action = "Removing an item with two children and its predecessor to the right of two ancestors";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(5),
                avl.remove(5));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{4, 2, 1, 3, 6, 7},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeRightLeafRightRotateOnRoot() {
        add(4, 2, 5, 1, 3);
        
        String action = "Removing a leaf on the left, triggering a right rotate on the root";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(5),
                avl.remove(5));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{2, 1, 4, 3},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeLeftLeafLeftRotateOnRoot() {
        add(2, 1, 4, 3, 5);
        
        String action = "Removing a leaf on the right, triggering a left rotate on the root";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(1),
                avl.remove(1));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{4, 2, 3, 5},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeRightLeafDoubleRightRotateOnRoot() {
        add(3, 1, 4, 2);
        
        String action = "Removing a leaf on the right, triggering a double right rotate on the root";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(4),
                avl.remove(4));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{2, 1, 3},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeLeftLeafDoubleLeftRotateOnRoot() {
        add(2, 1, 4, 3);
        
        String action = "Removing a leaf on the left, triggering a double left rotate on the root";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(1),
                avl.remove(1));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{3, 2, 4},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeRightLeafRightRotateOnLeft() {
        add(6, 4, 8, 2, 5, 7, 1, 3);
        
        String action = "Removing a leaf on the right of the left subtree, triggering a right rotate on the left subtree";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(5),
                avl.remove(5));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{6, 2, 1, 4, 3, 8, 7},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeLeftLeafLeftRotateOnLeft() {
        add(6, 2, 8, 1, 4, 7, 3, 5);
        
        String action = "Removing a leaf on the left of the left subtree, triggering a left rotate on the left subtree";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(1),
                avl.remove(1));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{6, 4, 2, 3, 5, 8, 7},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeRightLeafDoubleRightRotateOnLeft() {
        add(5, 3, 7, 1, 4, 6, 2);
        
        String action = "Removing a leaf on the right of the left subtree, triggering a double right rotate on the left subtree";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(4),
                avl.remove(4));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{5, 2, 1, 3, 7, 6},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeLeftLeafDoubleLeftRotateOnLeft() {
        add(5, 2, 6, 1, 4, 7, 3);
        
        String action = "Removing a leaf on the left of the left subtree, triggering a double left rotate on the left subtree";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(1),
                avl.remove(1));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{5, 3, 2, 4, 6, 7},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeRightLeafRightRotateOnRight() {
        add(3, 1, 7, 2, 5, 8, 4, 6);
        
        String action = "Removing a leaf on the right of the right subtree, triggering a right rotate on the right subtree";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(8),
                avl.remove(8));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{3, 1, 2, 5, 4, 7, 6},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeLeftLeafLeftRotateOnRight() {
        add(3, 1, 5, 2, 4, 7, 6, 8);
        
        String action = "Removing a leaf on the left of the right subtree, triggering a left rotate on the right subtree";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(4),
                avl.remove(4));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{3, 1, 2, 7, 5, 6, 8},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeRightLeafDoubleRightRotateOnRight() {
        add(3, 1, 6, 2, 4, 7, 5);
        
        String action = "Removing a leaf on the right of the right subtree, triggering a double right rotate on the right subtree";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(7),
                avl.remove(7));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{3, 1, 2, 5, 4, 6},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeLeftLeafDoubleLeftRotateOnRight() {
        add(3, 1, 5, 2, 4, 7, 6);
        
        String action = "Removing a leaf on the left of the right subtree, triggering a double left rotate on the right subtree";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(4),
                avl.remove(4));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{3, 1, 2, 6, 5, 7},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeInternalWithOneChildRightRotate() {
        add(6, 4, 7, 2, 5, 8, 1, 3);
        
        String action = "Removing an internal node with a right child on the right subtree, triggering a right rotate";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(7),
                avl.remove(7));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{4, 2, 1, 3, 6, 5, 8},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeInternalWithOneChildLeftRotate() {
        add(3, 2, 5, 1, 4, 7, 6, 8);
        
        String action = "Removing an internal node with a left child on the left subtree, triggering a left rotate";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(2),
                avl.remove(2));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{5, 3, 1, 4, 7, 6, 8},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeInternalWithOneChildDoubleRightRotate() {
        add(6, 2, 7, 1, 4, 8, 3, 5);
        
        String action = "Removing an internal node with a right child on the right subtree, triggering a double right rotate";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(7),
                avl.remove(7));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{4, 2, 1, 3, 6, 5, 8},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeInternalWithOneChildDoubleLeftRotate() {
        add(3, 2, 7, 1, 5, 8, 4, 6);
        
        String action = "Removing an internal node with a left child on the left subtree, triggering a double left rotate";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(2),
                avl.remove(2));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{5, 3, 1, 4, 7, 6, 8},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeInternalWithTwoChildrenRightRotate() {
        add(5, 3, 7, 2, 4, 6, 8, 1);
        
        String action = "Removing the root with two children and a right predecessor, triggering a right rotate";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(5),
                avl.remove(5));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{4, 2, 1, 3, 7, 6, 8},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeInternalWithTwoChildrenDoubleRightRotate() {
        add(5, 3, 7, 1, 4, 6, 8, 2);
        
        String action = "Removing the root with two children and a right predecessor, triggering a double right rotate";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(5),
                avl.remove(5));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{4, 2, 1, 3, 7, 6, 8},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeInternalWithTwoPredecessorOnLeftLeftRotate() {
        add(5, 2, 7, 1, 3, 6, 8, 4);
        
        String action = "Removing a node with two children and a left predecessor, triggering a left rotate";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(2),
                avl.remove(2));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{5, 3, 1, 4, 7, 6, 8},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeInternalWithTwoPredecessorOnLeftDoubleLeftRotate() {
        add(5, 2, 7, 1, 4, 6, 8, 3);
        
        String action = "Removing a node with two children and a left predecessor, triggering a double left rotate";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(2),
                avl.remove(2));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{5, 3, 1, 4, 7, 6, 8},
                avl);
    }

    @Test(timeout = TIMEOUT)
    public void removeCorrectRotateWhenNotStrictlyUnequalOnRight() {
        add(3, 2, 7, 1, 5, 9, 4, 6, 8, 10);
        
        String action = "Removing a node that should trigger only a single rotate on the other side of the tree";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(1),
                avl.remove(1));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{7, 3, 2, 5, 4, 6, 9, 8, 10},
                avl);
    } 
    
    @Test(timeout = TIMEOUT)
    public void removeCorrectRotateWhenNotStrictlyUnequalOnLeft() {
        add(8, 4, 9, 2, 6, 10, 1, 3, 5, 7);
        
        String action = "Removing a node that should trigger only a single rotate on the other side of the tree";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(10),
                avl.remove(10));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{4, 2, 1, 3, 8, 6, 5, 7, 9},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesLeafRightRight() {
        add(8, 5, 11, 3, 6, 10, 12, 2, 4, 7, 9, 1);
        
        String action = "Removing a leaf node that triggers a right rotate, followed by a right rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(12),
                avl.remove(12));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{5, 3, 2, 1, 4, 8, 6, 7, 10, 9, 11},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesLeafRightDoubleRight() {
        add(8, 3, 11, 2, 5, 10, 12, 1, 4, 6, 9, 7);
        
        String action = "Removing a leaf node that triggers a right rotate, followed by a double right rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(12),
                avl.remove(12));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{5, 3, 2, 1, 4, 8, 6, 7, 10, 9, 11},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesLeafRightDoubleLeft() {
        add(5, 3, 10, 2, 4, 8, 11, 1, 7, 9, 12, 6);
        
        String action = "Removing a leaf node that triggers a right rotate, followed by a double left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(4),
                avl.remove(4));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 5, 2, 1, 3, 7, 6, 10, 9, 11, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesLeafLeftLeft() {
        add(5, 2, 8, 1, 3, 7, 10, 4, 6, 9, 11, 12);
        
        String action = "Removing a leaf node that triggers a left rotate, followed by a left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(1),
                avl.remove(1));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 5, 3, 2, 4, 7, 6, 10, 9, 11, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesLeafLeftDoubleRight() {
        add(8, 3, 10, 2, 5, 9, 11, 1, 4, 6, 12, 7);
        
        String action = "Removing a leaf node that triggers a left rotate, followed by a double right rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(9),
                avl.remove(9));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{5, 3, 2, 1, 4, 8, 6, 7, 11, 10, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesLeafLeftDoubleLeft() {
        add(5, 2, 10, 1, 3, 8, 11, 4, 7, 9, 12);
        
        String action = "Removing a leaf node that triggers a left rotate, followed by a double left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(1),
                avl.remove(1));
          
        assertBstEquals(
                action + " failed",
                new Integer[]{5, 3, 2, 4, 10, 8, 7, 9, 11, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesLeafDoubleRightRight() {
        add(8, 5, 11, 3, 6, 9, 12, 2, 4, 7, 10, 1);
        
        String action = "Removing a leaf node that triggers a double right rotate, followed by a right rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(1),
                avl.remove(1));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 5, 3, 2, 4, 6, 7, 11, 9, 10, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesLeafDoubleRightLeft() {
        add(5, 3, 8, 1, 4, 7, 10, 2, 6, 9, 11, 12);
        
        String action = "Removing a leaf node that triggers a double right rotate, followed by a left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(4),
                avl.remove(4));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 5, 2, 1, 3, 7, 6, 10, 9, 11, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesLeafDoubleRightDoubleRight() {
        add(8, 3, 11, 2, 5, 9, 12, 1, 4, 6, 10, 7);
        
        String action = "Removing a leaf node that triggers a double right rotate, followed by a double right rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(12),
                avl.remove(12));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{5, 3, 2, 1, 4, 8, 6, 7, 10, 9, 11},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesLeafDoubleRightDoubleLeft() {
        add(5, 3, 10, 1, 4, 8, 11, 2, 7, 9, 12, 6);
        
        String action = "Removing a leaf node that triggers a double right rotate, followed by a double left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(4),
                avl.remove(4));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 5, 2, 1, 3, 7, 6, 10, 9, 11, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesLeafDoubleLeftRight() {
        add(8, 5, 10, 3, 6, 9, 12, 2, 4, 7, 11, 1);
        
        String action = "Removing a leaf node that triggers a double left rotate, followed by a right rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(9),
                avl.remove(9));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{5, 3, 2, 1, 4, 8, 6, 7, 11, 10, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesLeafDoubleLeftLeft() {
        add(5, 2, 8, 1, 4, 7, 10, 3, 6, 9, 11, 12);
        
        String action = "Removing a leaf node that triggers a double left rotate, followed by a left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(1),
                avl.remove(1));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 5, 3, 2, 4, 7, 6, 10, 9, 11, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesLeafDoubleLeftDoubleRight() {
        add(8, 3, 10, 2, 5, 9, 12, 1, 4, 6, 11, 7);
        
        String action = "Removing a leaf node that triggers a double left rotate, followed by a double right rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(9),
                avl.remove(9));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{5, 3, 2, 1, 4, 8, 6, 7, 11, 10, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesLeafDoubleLeftDoubleLeft() {
        add(5, 2, 10, 1, 4, 8, 11, 3, 7, 9, 12, 6);
        
        String action = "Removing a leaf node that triggers a double left rotate, followed by a double left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(1),
                avl.remove(1));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 5, 3, 2, 4, 7, 6, 10, 9, 11, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesOneChildRightRight() {
        add(13, 8, 18, 5, 11, 16, 19, 3, 6, 9, 12, 15, 17, 20, 2, 4, 7, 10, 14, 1);
        
        String action = "Removing an internal node with one child that triggers"
                + "a right rotate, followed by a right rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(19),
                avl.remove(19));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 5, 3, 2, 1, 4, 6, 7, 13, 11, 9, 10, 12, 16, 15, 14, 18, 17, 20},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesOneChildRightLeft() {
        add(8, 5, 13, 3, 6, 10, 16, 2, 4, 7, 9, 12, 15, 18, 1, 11, 14, 17, 19, 20);
        
        String action = "Removing an internal node with one child that triggers"
                + "a right rotate, followed by a left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(6),
                avl.remove(6));
        
        assertBstEquals(
                action + " failed",
                new Integer[]{13, 8, 3, 2, 1, 5, 4, 7, 10, 9, 12, 11, 16, 15, 14, 18, 17, 19, 20},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesOneChildRightDoubleRight() {
        add(13, 5, 18, 2, 8, 16, 19, 1, 4, 7, 10, 15, 17, 20, 3, 6, 9, 11, 14, 12);
        
        String action = "Removing an internal node with one child that triggers"
                + "a right rotate, followed by a double right rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(19),
                avl.remove(19));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 5, 2, 1, 4, 3, 7, 6, 13, 10, 9, 11, 12, 16, 15, 14, 18, 17, 20},
                avl);
    }

    @Test(timeout = TIMEOUT)
    public void twoRotatesOneChildRightDoubleLeft() {
        add(8, 5, 16, 3, 6, 13, 19, 2, 4, 7, 11, 14, 17, 20, 1, 10, 12, 15, 18, 9);
        
        String action = "Removing an internal node with one child that triggers"
                + "a right rotate, followed by a double left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(6),
                avl.remove(6));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{13, 8, 3, 2, 1, 5, 4, 7, 11, 10, 9, 12, 16, 14, 15, 19, 17, 18, 20},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesOneChildLeftRight() {
        add(13, 8, 16, 5, 11, 15, 18, 3, 6, 9, 12, 14, 17, 19, 2, 4, 7, 10, 20, 1);
        
        String action = "Removing an internal node with one child that triggers"
                + "a left rotate, followed by a right rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(15),
                avl.remove(15));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 5, 3, 2, 1, 4, 6, 7, 13, 11, 9, 10, 12, 18, 16, 14, 17, 19, 20},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesOneChildLeftLeft() {
        add(8, 3, 13, 2, 5, 10, 16, 1, 4, 6, 9, 12, 15, 18, 7, 11, 14, 17, 19, 20);
        
        String action = "Removing an internal node with one child that triggers"
                + "a left rotate, followed by a left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(2),
                avl.remove(2));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{13, 8, 5, 3, 1, 4, 6, 7, 10, 9, 12, 11, 16, 15, 14, 18, 17, 19, 20},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesOneChildLeftDoubleRight() {
        add(13, 5, 16, 2, 8, 15, 18, 1, 4, 7, 10, 14, 17, 19, 3, 6, 9, 11, 20, 12);
        
        String action = "Removing an internal node with one child that triggers"
                + "a left rotate, followed by a double right rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(15),
                avl.remove(15));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 5, 2, 1, 4, 3, 7, 6, 13, 10, 9, 11, 12, 18, 16, 14, 17, 19, 20},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesOneChildLeftDoubleLeft() {
        add(8, 3, 16, 2, 5, 13, 19, 1, 4, 6, 11, 14, 17, 20, 7, 10, 12, 15, 18, 9);
        
        String action = "Removing an internal node with one child that triggers"
                + "a left rotate, followed by a double left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(2),
                avl.remove(2));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{13, 8, 5, 3, 1, 4, 6, 7, 11, 10, 9, 12, 16, 14, 15, 19, 17, 18, 20},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesOneChildDoubleRightRight() {
        add(13, 8, 18, 5, 11, 15, 19, 3, 6, 9, 12, 14, 16, 20, 2, 4, 7, 10, 17, 1);
        
        String action = "Removing an internal node with one child that triggers"
                + "a double right rotate, followed by a right rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(19),
                avl.remove(19));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 5, 3, 2, 1, 4, 6, 7, 13, 11, 9, 10, 12, 16, 15, 14, 18, 17, 20},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesOneChildDoubleRightLeft() {
        add(8, 5, 13, 2, 6, 10, 16, 1, 3, 7, 9, 12, 15, 18, 4, 11, 14, 17, 19, 20);
        
        String action = "Removing an internal node with one child that triggers"
                + "a double right rotate, followed by a left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(6),
                avl.remove(6));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{13, 8, 3, 2, 1, 5, 4, 7, 10, 9, 12, 11, 16, 15, 14, 18, 17, 19, 20},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesOneChildDoubleRightDoubleRight() {
        add(13, 5, 18, 2, 8, 15, 19, 1, 4, 7, 10, 14, 16, 20, 3, 6, 9, 11, 17, 12);
        
        String action = "Removing an internal node with one child that triggers"
                + "a double right rotate, followed by a double right rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(19),
                avl.remove(19));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 5, 2, 1, 4, 3, 7, 6, 13, 10, 9, 11, 12, 16, 15, 14, 18, 17, 20},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesOneChildDoubleRightDoubleLeft() {
        add(8, 5, 16, 2, 6, 13, 19, 1, 3, 7, 11, 14, 17, 20, 4, 10, 12, 15, 18, 9);
        
        String action = "Removing an internal node with one child that triggers"
                + "a double right rotate, followed by a double left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(6),
                avl.remove(6));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{13, 8, 3, 2, 1, 5, 4, 7, 11, 10, 9, 12, 16, 14, 15, 19, 17, 18, 20},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesOneChildDoubleLeftRight() {
        add(13, 8, 16, 5, 11, 15, 19, 3, 6, 9, 12, 14, 18, 20, 2, 4, 7, 10, 17, 1);
        
        String action = "Removing an internal node with one child that triggers"
                + "a double left rotate, followed by a right rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(15),
                avl.remove(15));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 5, 3, 2, 1, 4, 6, 7, 13, 11, 9, 10, 12, 18, 16, 14, 17, 19, 20},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesOneChildDoubleLeftLeft() {
        add(8, 3, 13, 2, 6, 10, 16, 1, 5, 7, 9, 12, 15, 18, 4, 11, 14, 17, 19, 20);
        
        String action = "Removing an internal node with one child that triggers"
                + "a double left rotate, followed by a left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(2),
                avl.remove(2));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{13, 8, 5, 3, 1, 4, 6, 7, 10, 9, 12, 11, 16, 15, 14, 18, 17, 19, 20},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesOneChildDoubleLeftDoubleRight() {
        add(13, 5, 16, 2, 8, 15, 19, 1, 4, 7, 10, 14, 18, 20, 3, 6, 9, 11, 17, 20, 12);
        
        String action = "Removing an internal node with one child that triggers"
                + "a double left rotate, followed by a double right rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(15),
                avl.remove(15));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 5, 2, 1, 4, 3, 7, 6, 13, 10, 9, 11, 12, 18, 16, 14, 17, 19, 20},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesOneChildDoubleLeftDoubleLeft() {
        add(8, 3, 16, 2, 6, 13, 19, 1, 5, 7, 11, 14, 17, 20, 4, 10, 12, 15, 18, 9);
        
        String action = "Removing an internal node with one child that triggers"
                + "a double left rotate, followed by a double left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(2),
                avl.remove(2));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{13, 8, 5, 3, 1, 4, 6, 7, 11, 10, 9, 12, 16, 14, 15, 19, 17, 18, 20},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesTwoChildrenRightLeft() {
        add(5, 3, 8, 2, 4, 7, 10, 1, 6, 9, 11, 12);
        
        String action = "Removing an internal node with one child that triggers"
                + "a right rotate, followed by a left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(5),
                avl.remove(5));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 4, 2, 1, 3, 7, 6, 10, 9, 11, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesTwoChildrenRightDoubleLeft() {
        add(5, 3, 10, 2, 4, 8, 11, 1, 7, 9, 12, 6);
        
        String action = "Removing an internal node with one child that triggers"
                + "a right rotate, followed by a double left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(5),
                avl.remove(5));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 4, 2, 1, 3, 7, 6, 10, 9, 11, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesTwoChildrenLeftLeft() {
        add(5, 2, 8, 1, 3, 7, 10, 4, 6, 9, 11, 12);
        
        String action = "Removing an internal node with one child that triggers"
                + "a left rotate, followed by a left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(5),
                avl.remove(5));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 4, 2, 1, 3, 7, 6, 10, 9, 11, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesTwoChildrenLeftDoubleLeft() {
        add(5, 2, 10, 1, 3, 8, 11, 4, 7, 9, 12, 6);
        
        String action = "Removing an internal node with one child that triggers"
                + "a left rotate, followed by a double left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(5),
                avl.remove(5));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 4, 2, 1, 3, 7, 6, 10, 9, 11, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesTwoChildrenDoubleRightLeft() {
        add(5, 3, 8, 1, 4, 7, 10, 2, 6, 9, 11, 12);
        
        String action = "Removing an internal node with one child that triggers"
                + "a double right rotate, followed by a left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(5),
                avl.remove(5));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 4, 2, 1, 3, 7, 6, 10, 9, 11, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesTwoChildrenDoubleRightDoubleLeft() {
        add(5, 3, 10, 1, 4, 8, 11, 2, 7, 9, 12, 6);
        
        String action = "Removing an internal node with one child that triggers"
                + "a double right rotate, followed by a double left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(5),
                avl.remove(5));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 4, 2, 1, 3, 7, 6, 10, 9, 11, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesTwoChildrenDoubleLeftLeft() {
        add(5, 2, 8, 1, 4, 7, 10, 3, 6, 9, 11, 12);
        
        String action = "Removing an internal node with one child that triggers"
                + "a double left rotate, followed by a left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(5),
                avl.remove(5));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 4, 2, 1, 3, 7, 6, 10, 9, 11, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void twoRotatesTwoChildrenDoubleLeftDoubleLeft() {
        add(5, 2, 10, 1, 4, 8, 11, 3, 7, 9, 12, 6);
        
        String action = "Removing an internal node with one child that triggers"
                + "a double left rotate, followed by a double left rotate on another ancestor";
        
        assertEquals(
                action + " returned the wrong value",
                new Integer(5),
                avl.remove(5));
      
        assertBstEquals(
                action + " failed",
                new Integer[]{8, 4, 2, 1, 3, 7, 6, 10, 9, 11, 12},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void addDuplicates() { 
        avl.add(4);
        avl.add(4);
        
        assertBstEquals(
                "Adding a single duplicate to the tree caused it to break",
                new Integer[]{4},
                avl);
        
        add(2, 6, 1, 3, 5, 7);
        
        avl.add(3);
        
        assertBstEquals(
                "Adding duplicates to the tree caused it to break",
                new Integer[]{4, 2, 1, 3, 6, 5, 7},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void addException() {
        assertException(
                "Adding null to the tree should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> avl.add(null));
        
        avl.add(4);

        assertException(
                "Adding null to the tree should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> avl.add(null));
    }
    
    @Test(timeout = TIMEOUT)
    public void removeException() {
        assertException(
                "Removing null from an empty tree failed to throw an IllegalArgumentException", 
                IllegalArgumentException.class,
                () -> avl.remove(null));
        
        avl.add(4);
        
        assertException(
                "Removing null from a tree failed to throw an IllegalArgumentException", 
                IllegalArgumentException.class,
                () -> avl.remove(null));
    }
    
    @Test(timeout = TIMEOUT)
    public void getException() {
        assertException(
                "Getting null from an empty tree failed to throw an IllegalArgumentException", 
                IllegalArgumentException.class,
                () -> avl.get(null));
        
        avl.add(4);
        
        assertException(
                "Getting null from a tree failed to throw an IllegalArgumentException", 
                IllegalArgumentException.class,
                () -> avl.get(null));
    }
    
    @Test(timeout = TIMEOUT)
    public void containsException() {
        assertException(
                "Checking if an empty tree contains null failed to throw an IllegalArgumentException", 
                IllegalArgumentException.class,
                () -> avl.contains(null));
        
        avl.add(4);
        
        assertException(
                "Checking if a tree contains null failed to throw an IllegalArgumentException", 
                IllegalArgumentException.class,
                () -> avl.contains(null));
    }

    @Test(timeout = TIMEOUT)
    public void get() {
        assertException(
                "Calling get() on an empty tree failed to throw a java.util.NoSuchElementException",
                java.util.NoSuchElementException.class,
                () -> avl.get(4));
        
        Integer[] numbers = new Integer[]{4, 2, 1, 3, 6, 5, 7};
        for (Integer number: numbers) {
            avl.add(number);
        }
        
        for (Integer number: numbers) {
            Integer out = avl.get(new Integer(number.intValue()));
            assertSame(
                    "Calling get() failed to return the same object as the one stored in the tree",
                    number,
                    out);
        }
        
        assertBstEquals(
                "Calling get() on values in the tree changed the state of the tree",
                new Integer[]{4, 2, 1, 3, 6, 5, 7},
                avl);
        
        assertException(
                "Calling get() on a value not in the tree failed to throw a java.util.NoSuchElementException",
                java.util.NoSuchElementException.class,
                () -> avl.get(8));
        
        assertBstEquals(
                "Calling get() changed the state of the tree",
                new Integer[]{4, 2, 1, 3, 6, 5, 7},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void contains() {
        assertFalse(
                "Calling contains() on an empty tree failed to return false",
                avl.contains(4));
        
        Integer[] numbers = new Integer[]{4, 2, 1, 3, 6, 5, 7};
        for (Integer number: numbers) {
            avl.add(number);
        }
        
        for (Integer number: numbers) {
            assertTrue(
                    "contains() did not report true when the value was present in the tree",
                    avl.contains(new Integer(number.intValue())));
        }
        
        assertBstEquals(
                "Calling contains() on values in the tree changed the state of the tree",
                new Integer[]{4, 2, 1, 3, 6, 5, 7},
                avl);
        
        assertFalse(
                "Calling contains() on a value not in the tree failed to return false",
                avl.contains(8));
        
        assertBstEquals(
                "Calling contains() changed the state of the tree",
                new Integer[]{4, 2, 1, 3, 6, 5, 7},
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void removeSame() {
        Integer four = new Integer(4);
        avl.add(four);
        
        assertSame(
                "Removing a node did not return the same object as the one stored in the tree",
                four,
                avl.remove(4));
    }
    
    @Test(timeout = TIMEOUT)
    public void preorder() {
        add(4, 2, 6, 1, 3, 5, 7);
        assertArrayEquals(
                "Preorder failed",
                new Integer[]{4, 2, 1, 3, 6, 5, 7},
                avl.preorder().toArray());
    }
    
    @Test(timeout = TIMEOUT)
    public void postorder() {
        add(4, 2, 6, 1, 3, 5, 7);
        
        assertArrayEquals(
                "Postorder failed",
                new Integer[]{1, 3, 2, 5, 7, 6, 4},
                avl.postorder().toArray());
    }
    
    @Test(timeout = TIMEOUT)
    public void inorder() {
        add(4, 2, 6, 1, 3, 5, 7);
        
        assertArrayEquals(
                "Inorder failed",
                new Integer[]{1, 2, 3, 4, 5, 6, 7},
                avl.inorder().toArray());
    }
    
    @Test(timeout = TIMEOUT)
    public void levelorder() {
        add(4, 2, 6, 1, 3, 5, 7);
        
        assertArrayEquals(
                "Levelorder failed",
                new Integer[]{4, 2, 6, 1, 3, 5, 7},
                avl.levelorder().toArray());
    }
    
    @Test(timeout = TIMEOUT)
    public void clear() {
        add(4, 2, 6, 1, 3, 5, 7);
        
        avl.clear();
        
        assertBstEquals(
                "The clear() method failed",
                new Integer[0],
                avl);
    }
    
    @Test(timeout = TIMEOUT)
    public void collectionConstructor() {
        assertException(
                "Passing null to the collection constructor should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> new AVL<Integer>(null));
        
        ArrayList<Integer> invalidCollection = new ArrayList<Integer>(Arrays.asList(new Integer[]{4, 2, 6, 1, null, 5, 7}));
        assertException(
                "Passing a collection with null values to the collection constructor should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> new AVL<Integer>(invalidCollection));
        
        ArrayList<Integer> collection = new ArrayList<Integer>(Arrays.asList(new Integer[]{4, 2, 6, 1, 3, 5, 7}));
        AVL<Integer> collectionAvl = new AVL<Integer>(collection);
        
        assertBstEquals(
                "The collection constructor failed to add the elements correctly",
                new Integer[]{4, 2, 1, 3, 6, 5, 7},
                collectionAvl);

        collectionAvl.add(8);
        
        assertBstEquals(
                "Adding an element after using the collection constructor failed",
                new Integer[]{4, 2, 1, 3, 6, 5, 7, 8},
                collectionAvl);
    }
    
    @Test(timeout = TIMEOUT)
    public void collectionConstructorRotate() {
        ArrayList<Integer> collection = new ArrayList<Integer>(Arrays.asList(new Integer[]{1, 2, 3}));
        AVL<Integer> collectionAvl = new AVL<Integer>(collection);
        
        assertBstEquals(
                "The collection constructor did not perform the correct rotations",
                new Integer[]{2, 1, 3},
                collectionAvl);
    }
    
}