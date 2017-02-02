import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Your implementation of an AVL Tree.
 *
 * @author peiyu wang
 * @version 1.0
 */
public class AVL<T extends Comparable<? super T>> implements AVLInterface<T> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private AVLNode<T> root;
    private int size;

    /**
     * A no argument constructor that should initialize an empty AVL tree.
     * DO NOT IMPLEMENT THIS CONSTRUCTOR!
     */
    public AVL() {

    }

    /**
     * Initializes the AVL tree with the data in the Collection. The data
     * should be added in the same order it is in the Collection.
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }
        for (T t: data) {
            add(t);
        }
    }

    @
    Override
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }
        root = realadd(root, data);
    }

    /**
     * description: the real add method
     * @author peiyu wang
     * @param data mean the data
     * @param tempnode mean the node
     * @return the node
     *
     */

    private AVLNode<T> realadd(AVLNode<T> tempnode, T data) {
        if (tempnode == null) {
            size++;
            tempnode = new AVLNode<T>(data);
        }


        if (data.compareTo(tempnode.getData()) < 0) {
            tempnode.setLeft(realadd(tempnode.getLeft(), data));
            tempnode.setBalanceFactor(myGetH(tempnode.getLeft())
                - myGetH(tempnode.getRight()));
            tempnode = balance(tempnode);
        } else if (data.compareTo(tempnode.getData()) > 0) {
            tempnode.setRight(realadd(tempnode.getRight(), data));
            tempnode.setBalanceFactor(myGetH(tempnode.getLeft())
                - myGetH(tempnode.getRight()));
            tempnode = balance(tempnode);
        }
        tempnode.setHeight(Math.max(myGetH(tempnode.getRight()),
            myGetH(tempnode.getLeft())) + 1);
        tempnode.setBalanceFactor(myGetH(tempnode.getLeft())
            - myGetH(tempnode.getRight()));

        return tempnode;


    }

    /**
     * description: banalce method
     * @author peiyu wang
     * @param node mean the node
     * @return the node
     *
     */

    private AVLNode<T> balance(AVLNode<T> node) {
        if (node.getBalanceFactor() > 1) {
            if (node.getLeft() != null
                && node.getLeft().getBalanceFactor() >= 0) {

                node = rightR(node);
            } else {
                node = leftrightR(node);
            }
        } else if (node.getBalanceFactor() < -1) {
            if (node.getRight() != null
                && node.getRight().getBalanceFactor() <= 0) {
                node = leftR(node);
            } else {
                node = rightleftR(node);
            }
        }
        return node;


    }









    @
    Override
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }
        AVLNode<T> lul = new AVLNode<T>(null);
        root = realremove(root, data, lul);
        return lul.getData();
    }
    /**
     * description: the real remove method
     * @author peiyu wang
     * @param data mean the data
     * @param tempnode mean the node
     * @param lul the return node
     * @return the node
     *
     */
    private AVLNode<T> realremove(AVLNode<T> tempnode, T data, AVLNode<T> lul) {
        AVLNode<T> none = new AVLNode<T>(null);
        if (tempnode == null) {
            throw new java.util.NoSuchElementException("data not found!");
        } else if (data.compareTo(tempnode.getData()) < 0) {
            tempnode.setLeft(realremove(tempnode.getLeft(), data, lul));
            tempnode.setHeight(Math.max(myGetH(tempnode.getRight()),
                myGetH(tempnode.getLeft())) + 1);
            tempnode.setBalanceFactor(myGetH(tempnode.getLeft()) 
                - myGetH(tempnode.getRight()));
            tempnode = balance(tempnode);

        } else if (data.compareTo(tempnode.getData()) > 0) {
            tempnode.setRight(realremove(tempnode.getRight(), data, lul));
            tempnode.setHeight(Math.max(myGetH(tempnode.getRight()),
                myGetH(tempnode.getLeft())) + 1);
            tempnode.setBalanceFactor(myGetH(tempnode.getLeft())
                - myGetH(tempnode.getRight()));
            tempnode = balance(tempnode);
        } else {

            if (tempnode.getLeft() == null) {
                size--;
                lul.setData(tempnode.getData());
                tempnode.setBalanceFactor(myGetH(tempnode.getLeft())
                    - myGetH(tempnode.getRight()));

                return tempnode.getRight();
            } else if (tempnode.getRight() == null) {
                size--;
                lul.setData(tempnode.getData());
                tempnode.setBalanceFactor(myGetH(tempnode.getLeft())
                    - myGetH(tempnode.getRight()));

                return tempnode.getLeft();
            } else {
                lul.setData(tempnode.getData());
                tempnode.setData(bignode(tempnode.getLeft()));
                tempnode.setLeft(realremove(tempnode.getLeft(),
                                            tempnode.getData(), none));
                tempnode.setHeight(Math.max(myGetH(tempnode.getRight()),
                    myGetH(tempnode.getLeft())) + 1);
                tempnode.setBalanceFactor(myGetH(tempnode.getLeft())
                    - myGetH(tempnode.getRight()));
                tempnode = balance(tempnode);
            }
        }
        tempnode.setBalanceFactor(myGetH(tempnode.getLeft())
            - myGetH(tempnode.getRight()));

        return tempnode;

    }
    /**
     * description: find the node we want
     * @author peiyu wang
     * @param onenode mean the node
     * @return the node
     *
     */
    private T bignode(AVLNode<T> onenode) {
        while (onenode.getRight() != null) {
            onenode = onenode.getRight();
        }
        return onenode.getData();
    }









    /**
     * Right rotation
     * @param thenode is the node to rotate
     * @return the rotated node
     */
    private AVLNode<T> rightR(AVLNode<T> thenode) {
        AVLNode<T> temp = thenode.getLeft();
        thenode.setLeft(temp.getRight());
        temp.setRight(thenode);
        thenode.setHeight(Math.max(myGetH(thenode.getRight()),
            myGetH(thenode.getLeft())) + 1);
        temp.setHeight(Math.max(myGetH(temp.getRight()),
            myGetH(temp.getLeft())) + 1);
        thenode.setBalanceFactor(myGetH(thenode.getLeft())
            - myGetH(thenode.getRight()));
        temp.setBalanceFactor(myGetH(temp.getLeft()) - myGetH(temp.getRight()));
        return temp;

    }

    /**
     * left rotation
     * @param thenode is the node to rotate
     * @return the rotated node
     */
    private AVLNode<T> leftR(AVLNode<T> thenode) {
        AVLNode<T> temp = thenode.getRight();
        thenode.setRight(temp.getLeft());
        temp.setLeft(thenode);
        thenode.setHeight(Math.max(myGetH(thenode.getRight()),
            myGetH(thenode.getLeft())) + 1);
        temp.setHeight(Math.max(myGetH(temp.getRight()),
            myGetH(temp.getLeft())) + 1);
        thenode.setBalanceFactor(myGetH(thenode.getLeft())
            - myGetH(thenode.getRight()));
        temp.setBalanceFactor(myGetH(temp.getLeft())
            - myGetH(temp.getRight()));

        return temp;
    }


    /**
     * rightleft rotation
     * @param thenode is the node to rotate
     * @return the rotated node
     */
    private AVLNode<T> rightleftR(AVLNode<T> thenode) {
        thenode.setRight(rightR(thenode.getRight()));
        return leftR(thenode);
    }


    /**
     * leftright rotation
     * @param thenode is the node to rotate
     * @return the rotated node
     */
    private AVLNode<T> leftrightR(AVLNode<T> thenode) {
        thenode.setLeft(leftR(thenode.getLeft()));
        return rightR(thenode);
    }


    /**
     * description: method to get the hight
     * @author peiyu wang
     * @param tempnode mean the node
     * @return the height
     *
     */

    private int myGetH(AVLNode<T> tempnode) {
        if (tempnode == null) {
            return -1;
        } else {
            return tempnode.getHeight();
        }
    }

    @
    Override
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }
        return realget(root, data);
    }
    /**
     * description: method to get the node from tree
     * @author peiyu wang
     * @param data mean the data
     * @param tempnode mean the node
     * @return the node
     *
     */

    private T realget(AVLNode<T> tempnode, T data) {
        if (tempnode == null) {
            throw new java.util.NoSuchElementException("data is not found!");
        }
        if (data.compareTo(tempnode.getData()) == 0) {
            return tempnode.getData();
        } else if (data.compareTo(tempnode.getData()) < 0) {
            return realget(tempnode.getLeft(), data);
        } else {
            return realget(tempnode.getRight(), data);
        }

    }

    @
    Override
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }
        return realcontains(root, data);

    }
    /**
     * description: method to check the tree has centain node or not
     * @author peiyu
     *
     * @param data of the node
     * @param tempnode node
     * @return true or false of the tree contains the data
     *
     */

    private boolean realcontains(AVLNode<T> tempnode, T data) {
        if (tempnode == null) {
            return false;
        }
        if (data.compareTo(tempnode.getData()) == 0) {
            return true;
        } else if (data.compareTo(tempnode.getData()) < 0) {
            return realcontains(tempnode.getLeft(), data);
        } else {
            return realcontains(tempnode.getRight(), data);
        }


    }

    @
    Override
    public int size() {
        return size;
    }

    @
    Override
    public List<T> preorder() {
        List<T> list = new ArrayList<T>();
        realpreorder(root, list);
        return list;
    }
    /**
     * description: get the preorder list of tree
     * @author peiyu wang
     * @param list mean the list
     * @param tempnode mean the node
     *
     *
     */

    private void realpreorder(AVLNode<T> tempnode, List<T> list) {
        if (tempnode == null) {
            return;
        }
        list.add(tempnode.getData());
        realpreorder(tempnode.getLeft(), list);
        realpreorder(tempnode.getRight(), list);
    }

    @
    Override
    public List<T> postorder() {
        List<T> list = new ArrayList<T>();
        realpostorder(root, list);
        return list;
    }

    /**
     * description get the postorder list of tree
     * @author peiyu w
     * @param list mean the list
     * @param tempnode mean the node
     *
     *
     */
    private void realpostorder(AVLNode<T> tempnode, List<T> list) {
        if (tempnode == null) {
            return;
        }

        realpostorder(tempnode.getLeft(), list);
        realpostorder(tempnode.getRight(), list);
        list.add(tempnode.getData());
    }

    @
    Override
    public List<T> inorder() {
        List<T> list = new ArrayList<T>();
        realinorder(root, list);
        return list;
    }
    /**
     * description get the inorder list of tree
     * @author peiyu wang
     * @param list mean the list
     * @param tempnode mean the node
     *
     */
    private void realinorder(AVLNode<T> tempnode, List<T> list) {
        if (tempnode == null) {
            return;
        }
        realinorder(tempnode.getLeft(), list);
        list.add(tempnode.getData());
        realinorder(tempnode.getRight(), list);
    }

    @
    Override
    public List<T> levelorder() {
        AVLNode<T> tempnode;
        List<T> list = new ArrayList<T>();
        Queue<AVLNode<T>> myQ = new LinkedList<AVLNode<T>>();
        if (root != null) {
            myQ.add(root);
        }
        while (!myQ.isEmpty()) {
            tempnode = myQ.remove();
            list.add(tempnode.getData());
            if (tempnode.getLeft() != null) {
                myQ.add(tempnode.getLeft());
            }
            if (tempnode.getRight() != null) {
                myQ.add(tempnode.getRight());
            }
        }
        return list;
    }

    @
    Override
    public void clear() {
        root = null;
        size = 0;
    }

    @
    Override
    public int height() {
        return realheight(root);
    }
    /**
     * description: function to return tree hight
     * @author peiyu wang
     * @param tempnode mean the node
     * @return the height
     *
     */
    private int realheight(AVLNode<T> tempnode) {
        if (tempnode == null) {
            return -1;
        } else {
            return 1 + Math.max(realheight(tempnode.getLeft()),
                                realheight(tempnode.getRight()));
        }

    }

    @
    Override
    public AVLNode<T> getRoot() {
        // DO NOT EDIT THIS METHOD!
        return root;
    }
}