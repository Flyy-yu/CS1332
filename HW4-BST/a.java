import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class a<T extends Comparable<? super T>> implements BSTInterface<T> {
    private BSTNode<T> root;
    private int size;

    /**
     * A no argument constructor that should initialize an empty BST
     */
    public a() {
        root = null;
        size = 0;
    }

    /**
     * Initializes the BST with the data in the collection. The data in the BST
     * should be added in the same order it is in the collection.
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public a(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        for (T e: data) {
            add(e);
        }
    }
    @Override
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            ++size;
            root = new BSTNode<T>(data);
//        } else if (root.getData().compareTo(data) > 0) {
//            root.setLeft(addHelper(data, root.getLeft()));
//        } else if (root.getData().compareTo(data) < 0) {
//            root.setRight(addHelper(data, root.getRight()));
        } else {
        	addHelper(data, root);
        }
    }
    /**
    * @author Tuan Nguyen
    * @version 1.19
    * @param data which is the data of the node
    * @param current  which is the  current node
    * @return
    * set the node in order
    */
    private BSTNode<T> addHelper(T data, BSTNode<T> current) {
        // At a leaf
        if (current == null) {
            ++size;
            return new BSTNode<T>(data);
        } else {  //Not at a leaf continue to traverse the tree
            if (current.getData().compareTo(data) > 0) {
                current.setLeft(addHelper(data, current.getLeft()));
            } else if (current.getData().compareTo(data) < 0) {
                current.setRight(addHelper(data, current.getRight()));
            }
            return current;
        }
    }

    
    @Override
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException();
        }

        return removeHelper(data, null);
    }
    /**
    * @author Tuan Nguyen
    * @version 1.19
    * @param data which is the data of the node
    * @param parent which is the node of the parent
    * @return
    * set the node in order
    */
    private T removeHelper(T data, BSTNode<T> parent) {
        BSTNode<T> current;
        if (parent == null) {
            current = root;
        } else if (data.compareTo(parent.getData()) < 0) {
            current = parent.getLeft();
        } else {
            current = parent.getRight();
        }
        if (current == null) {
            throw new java.util.NoSuchElementException();
        }
        if (current.getData().compareTo(data) == 0) {
            T ret = current.getData();
            --size;
            if (current.getLeft() == null
                    && current.getRight() == null) {
                current = null;
            } else if (current.getLeft() == null) {
                current = current.getRight();
            } else if (current.getRight() == null) {
                current = current.getLeft();
            } else {
                T newData = getMin(current.getRight());
                current.setData(newData);
                ++size;
                removeHelper(newData, current);
            }
            if (parent == null) {
                root = current;
            } else if (data.compareTo(parent.getData()) < 0) {
                parent.setLeft(current);
            } else {
                parent.setRight(current);
            }
            return ret;
        } else {
            return removeHelper(data, current);
        }
    }
    /**
    * @author Tuan Nguyen
    * @version 1.19
    * @param current which is current node
    * @return the minimum node
    * set the node in order
    */
    private T getMin(BSTNode<T> current) {
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current.getData();
    }
    @Override
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        return getHelper(data, root);
    }
    /**
    * @author Tuan Nguyen
    * @version 1.19
    * @param current which is current node
    * @param data which is data type
    * @return the data of that node
    * set the node in order
    */
    private T getHelper(T data, BSTNode<T> current) {
        if (current == null) {
            throw new java.util.NoSuchElementException();
        }
        if (data.equals(current.getData())) {
            return current.getData();
        } else if (data.compareTo(current.getData()) < 0)  {
            return getHelper(data, current.getLeft());
        } else {
            return getHelper(data, current.getRight());
        }
    }
    @Override
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        return containsHelper(data, root);
    }
    /**
    * @author Tuan Nguyen
    * @version 1.19
    * @param data of the node
    * @param current node
    * @return true or false of the tree contains the data
    * set the node in order
    */
    private boolean containsHelper(T data, BSTNode<T> current) {
        if (current == null) {
            return false;
        }
        if (data.equals(current.getData())) {
            return true;
        } else if (data.compareTo(current.getData()) < 0) {
            return containsHelper(data, current.getLeft());
        } else {
            return containsHelper(data, current.getRight());
        }
    }
    @Override
    public int size() {
        return size;

    }
    @Override
    public List<T> preorder() {
        List<T> list = new ArrayList<T>();
        preorderHelper(root, list);
        return list;
    }
    /**
    * @author Tuan Nguyen
    * @version 1.19
    * @param current which is current node
    * @param list of the node
    * set the node in order
    */
    private void preorderHelper(BSTNode<T> current, List<T> list) {
        if (current == null) {
            return;
        }
        list.add(current.getData());
        preorderHelper(current.getLeft(), list);
        preorderHelper(current.getRight(), list);
    }

    @Override
    public List<T> postorder() {
        List<T> list = new ArrayList<T>();
        postorderHelper(root, list);
        return list;
    }
    /**
    * @author Tuan Nguyen
    * @version 1.19
    * @param current which is current node
    * @param list of the node
    * set the node in order
    */
    private void postorderHelper(BSTNode<T> current, List<T> list) {
        if (current == null) {
            return;
        }
        postorderHelper(current.getLeft(), list);
        postorderHelper(current.getRight(), list);
        list.add(current.getData());
    }

    @Override
    public List<T> inorder() {
        List<T> list = new ArrayList<T>();
        inorderHelper(root, list);
        return list;
    }
    /**
    * @author Tuan Nguyen
    * @version 1.19
    * @param current which is current node
    * @param list which is the list of the data
    ** set the node in order
    */
    private void inorderHelper(BSTNode<T> current, List<T> list) {
        if (current == null) {
            return;
        }
        inorderHelper(current.getLeft(), list);
        list.add(current.getData());
        inorderHelper(current.getRight(), list);
    }

    @Override
    public List<T> levelorder() {
        Queue<BSTNode<T>> queue = new LinkedList<BSTNode<T>>();
        List<T> list = new ArrayList<T>();
        if (root != null) {
            queue.add(root);
        }
        BSTNode<T> current;
        while (!queue.isEmpty()) {
            current = queue.remove();
            list.add(current.getData());
            if (current.getLeft() != null) {
                queue.add(current.getLeft());
            }
            if (current.getRight() != null) {
                queue.add(current.getRight());
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
        return heightHelper(root);
    }
    /**
    * @author Tuan Nguyen
    * @version 1.19
    * @param current which is current node
    * @return return the depth
    ** get the depth level of the tree
    */
    private int heightHelper(BSTNode<T> current) {
        if (current == null) {
            return -1;
        } else {
            return Integer.max(heightHelper(current.getLeft()),
                heightHelper(current.getRight())) + 1;
        }
    }

    //@Override
    public int depth(T data) {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        return depthHelper(data, root, 0);
    }
    /**
    * @author Tuan Nguyen
    * @version 1.19
    * @Param takes in a data, current, currentDepth
    * @param data takes in the data
    * @param current which is current node
    * @param currentDepth which is current depth.
    * @return return the depth
    ** get the depth level of the tree
    */
    private int depthHelper(T data, BSTNode<T> current, int currentDepth) {
        ++currentDepth;
        if (current == null) {
            return 0;
        } else if (data.equals(current.getData())) {
            return currentDepth;
        } else if (data.compareTo(current.getData()) < 0) {
            return depthHelper(data, current.getLeft(), currentDepth);
        } else {
            return depthHelper(data, current.getRight(), currentDepth);
        }
    }

    /**
     * THIS METHOD IS ONLY FOR TESTING PURPOSES.
     * DO NOT USE IT IN YOUR CODE
     * DO NOT CHANGE THIS METHOD
     *
     * @return the root of the tree
     */
    public BSTNode<T> getRoot() {
        return root;
    }
}