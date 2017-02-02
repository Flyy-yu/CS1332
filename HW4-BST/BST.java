import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
/**
 * Your implementation of a binary search tree.
 *
 * @author peiyu 0
 */
public class BST<T extends Comparable<? super T>> implements BSTInterface<T> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private BSTNode<T> root;
    private int size;

    /**
     * A no argument constructor that should initialize an empty BST.
     * YOU DO NOT NEED TO IMPLEMENT THIS CONSTRUCTOR!
     */
    public BST() {

    }

    /**
     * Initializes the BST with the data in the Collection. The data in the BST
     * should be added in the same order it is in the Collection.
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public BST(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }
        for (T t: data) {
            add(t);
        }

    }

    @Override
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

    private BSTNode<T> realadd(BSTNode<T> tempnode, T data) {
        if (tempnode == null) {
            size++;
            return new BSTNode<T>(data);

        }

        if (data.compareTo(tempnode.getData()) == 0) {
            return tempnode;
        }

        if (data.compareTo(tempnode.getData()) < 0) {
            tempnode.setLeft(realadd(tempnode.getLeft(), data));
        } else {
            tempnode.setRight(realadd(tempnode.getRight(), data));
        }
        return tempnode;

    }






    @Override
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }
        root = realremove(root, data);
        return data;

    }
    /**
         * description: the real remove method
         * @author peiyu wang
         * @param data mean the data
         * @param tempnode mean the node
         * @return the node
         * 
         */
    private BSTNode<T> realremove(BSTNode<T> tempnode, T data) {
        if (tempnode == null) {
            throw new java.util.NoSuchElementException("data not found!");
        } else if (data.compareTo(tempnode.getData()) < 0) {
            tempnode.setLeft(realremove(tempnode.getLeft(), data));
        } else if (data.compareTo(tempnode.getData()) > 0) {
            tempnode.setRight(realremove(tempnode.getRight(), data));
        } else {
            if (tempnode.getLeft() == null) {
                size--;
                return tempnode.getRight();
            } else if (tempnode.getRight() == null) {
                size--;
                return tempnode.getLeft();
            } else {
                tempnode.setData(bignode(tempnode.getLeft()));
                tempnode.setLeft(realremove(tempnode.getLeft(), 
                    tempnode.getData()));
            }
        }
        return tempnode;

    }
    /**
         * @author peiyu wang
         * @param onenode mean the node
         * @return the node
         * 
         */
    private T bignode(BSTNode<T> onenode) {
        while (onenode.getRight() != null) {
            onenode = onenode.getRight();
        }
        return onenode.getData();
    }

    @Override
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }
        return realget(root, data);
    }
    /**
         * description: method to get the note from tree
         * @author peiyu wang
         * @param data mean the data
         * @param tempnode mean the node
         * @return the node
         * 
         */

    private T realget(BSTNode<T> tempnode, T data) {
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

    @Override
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }
        return realcontains(root, data);

    }
    /**
    * description: method to check the tree has centain note or not
    * @author peiyu
    * 
    * @param data of the node
    * @param tempnode node
    * @return true or false of the tree contains the data
    * 
    */

    private boolean realcontains(BSTNode<T> tempnode, T data) {
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

    @Override
    public int size() {
        return size;
    }

    @Override
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

    private void realpreorder(BSTNode<T> tempnode, List<T> list) {
        if (tempnode == null) {
            return;
        }
        list.add(tempnode.getData());
        realpreorder(tempnode.getLeft(), list);
        realpreorder(tempnode.getRight(), list);
    }

    @Override
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
    private void realpostorder(BSTNode<T> tempnode, List<T> list) {
        if (tempnode == null) {
            return;
        }

        realpostorder(tempnode.getLeft(), list);
        realpostorder(tempnode.getRight(), list);
        list.add(tempnode.getData());
    }

    @Override
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
    private void realinorder(BSTNode<T> tempnode, List<T> list) {
        if (tempnode == null) {
            return;
        }
        realinorder(tempnode.getLeft(), list);
        list.add(tempnode.getData());
        realinorder(tempnode.getRight(), list);
    }

    @Override
    public List<T> levelorder() {
        BSTNode<T> tempnode;
        List<T> list = new ArrayList<T>();
        Queue<BSTNode<T>> myQ = new LinkedList<BSTNode<T>>();
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
   
    



    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
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
    private int realheight(BSTNode<T> tempnode) {
        if (tempnode == null) {
            return -1;
        } else {
            return 1 + Math.max(realheight(tempnode.getLeft()), 
                realheight(tempnode.getRight()));
        }

    }




    @Override
    public BSTNode<T> getRoot() {
        // DO NOT EDIT THIS METHOD!
        return root;
    }
}