/**
 * Your implementation of a DoublyLinkedList
 *
 * @author Peiyu Wang
 * @version 1.0
 */
public class DoublyLinkedList<T> implements LinkedListInterface<T> {
    // Do not add new instance variables.
    private LinkedListNode<T> head;
    private LinkedListNode<T> tail;
    private int size;

    @
    Override
    public void addAtIndex(int index, T data) {
        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Error, index is negative" 
                + "or larger than size");
        }



        LinkedListNode<T> tempnode = new LinkedListNode(data);


        if (index == 0) {

            if (head == null) {
                head = tempnode;
                tail = tempnode;
            } else {
                tempnode.setNext(head);
                tempnode.setPrevious(null);
                head.setPrevious(tempnode);
                head = tempnode;
            }






        } else if (index == size) {

            if (tail == null) {
                head = tempnode;
                tail = tempnode;

            } else {
                tempnode.setNext(null);
                tempnode.setPrevious(tail);
                tail.setNext(tempnode);
                tail = tempnode;
            }



        } else {

            LinkedListNode<T> current = head;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }

            tempnode.setNext(current);
            tempnode.setPrevious(current.getPrevious());
            (current.getPrevious()).setNext(tempnode);
            current.setPrevious(tempnode);
        }
        size++;

    }

    @
    Override
    public void addToFront(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }
        LinkedListNode<T> tempnode = new LinkedListNode(data);

        if (head == null) {
            head = tempnode;
            tail = tempnode;
        } else {
            tempnode.setNext(head);
            tempnode.setPrevious(null);
            head.setPrevious(tempnode);
            head = tempnode;
        }
        size++;
    }

    @
    Override
    public void addToBack(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }
        LinkedListNode<T> tempnode = new LinkedListNode(data);
        if (tail == null) {
            head = tempnode;
            tail = tempnode;

        } else {
            tempnode.setNext(null);
            tempnode.setPrevious(tail);
            tail.setNext(tempnode);
            tail = tempnode;
        }
        size++;
    }

    @
    Override
    public T removeAtIndex(int index) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Error, index is negative" 
                + "or larger than size");
        }

        if (index == 0) {
            LinkedListNode<T> current = head;


            if (size == 1) {
                head = null;
                tail = null;
            } else {
                head.getNext().setPrevious(null);
                head = head.getNext();
            }
            size--;
            return current.getData();
        } else if (index == size - 1) {
            LinkedListNode<T> current = tail;
            if (size == 1) {
                head = null;
                tail = null;
            } else {
                tail.getPrevious().setNext(null);
                tail = tail.getPrevious();
            }
            size--;
            return current.getData();


        } else {

            LinkedListNode<T> current = head;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }

            current.getNext().setPrevious(current.getPrevious());
            current.getPrevious().setNext(current.getNext());
            size--;
            return current.getData();
        }

    }

    @
    Override
    public T removeFromFront() {
        if (size == 0) {
            return null;
        }
        LinkedListNode<T> current = head;

        if (size == 1) {
            head = null;
            tail = null;
        } else {
            head.getNext().setPrevious(null);
            head = head.getNext();
        }

        size--;
        return current.getData();
    }

    @
    Override
    public T removeFromBack() {
        if (size == 0) {
            return null;
        }

        LinkedListNode<T> current = tail;


        if (size == 1) {
            head = null;
            tail = null;
        } else {
            tail.getPrevious().setNext(null);
            tail = tail.getPrevious();
        }





        size--;
        return current.getData();
    }

    @
    Override
    public boolean removeAllOccurrences(T data) {

        if (data == null) {
            throw new IllegalArgumentException("Error,data is null");
        }
        int check = 0;

        if (size == 0) {
            return false;
        }
        LinkedListNode<T> current = head;
        for (int i = 0; i < size; i++) {
            if (current.getData().equals(data)) {
                check++;
                if (current.getNext() != null) {
                    if (current.getPrevious() == null) {
                        current.getNext().setPrevious(null);
                        head = current.getNext();
                    } else {
                        current.getPrevious().setNext(current.getNext());
                        current.getNext().setPrevious(current.getPrevious());
                    }
                } else {
                    if (current.getPrevious() == null) {
                        head = null;
                        tail = null;
                    } else {
                        current.getPrevious().setNext(null);
                        tail = current.getPrevious();
                    }
                }
            }
            current = current.getNext();
        }
        size = size - check;
        return !(check == 0);
    }

    @
    Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Error, index is negative" 
                + "or larger than size");
        }
        LinkedListNode<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData();
    }

    @
    Override
    public Object[] toArray() {
        LinkedListNode<T> current = head;
        Object[] backingArray = (Object[]) new Object[size];
        for (int i = 0; i < size; i++) {
            backingArray[i] = current.getData();
            current = current.getNext();

        }
        return backingArray;
    }

    @
    Override
    public boolean isEmpty() {
        return size == 0;
    }

    @
    Override
    public int size() {
        return size;
    }

    @
    Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    @
    Override
    public LinkedListNode<T> getHead() {
        // DO NOT MODIFY!
        return head;
    }

    @
    Override
    public LinkedListNode<T> getTail() {
        // DO NOT MODIFY!
        return tail;
    }
}