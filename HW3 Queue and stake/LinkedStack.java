/**
 * Your implementation of a linked stack.
 *
 * @author peiyu wang
 * @version 1.0
 */
public class LinkedStack<T> implements StackInterface<T> {

    // Do not add new instance variables.
    private LinkedNode<T> head;
    private int size;

    @
    Override
    public boolean isEmpty() {
        return size == 0;
    }

    @
    Override
    public T pop() {
        if (size == 0) {
            throw new java.util.NoSuchElementException("Error, queue is empty");
        }
        LinkedNode<T> temp = head;
        if (head.getNext() != null) {
            head = head.getNext();
        } else {
            head = null;
        }


        size--;
        return temp.getData();
    }

    @
    Override
    public void push(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }
        LinkedNode<T> temp = new LinkedNode(data);
        temp.setNext(head);
        head = temp;
        size++;
    }

    @
    Override
    public int size() {
        return size;
    }

    /**
     * Returns the head of this stack.
     * Normally, you would not do this, but we need it for grading your work.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return the head node
     */
    public LinkedNode<T> getHead() {
        // DO NOT MODIFY!
        return head;
    }
}