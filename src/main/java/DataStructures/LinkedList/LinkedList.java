package DataStructures.LinkedList;

/**
 * A linked list implementation in Java.
 * This class provides methods to add and remove elements from the list.
 * @param <T> the type of elements stored in the linked list
 */
public class LinkedList<T> {
    private LinkedListNode<T> head = null;
    private LinkedListNode<T> tail = null;
    private int size = 0;

    public LinkedList() {}

    /**
     * Represents a linked list data structure.
     * 
     * @param <T> the type of elements stored in the linked list
     */
    public LinkedList(LinkedListNode<T> node) {
        this.head = node;
        this.tail = node;
        this.size++;
    }

    public int size() {
        return size;
    }

    /**
     * Adds a node to the end of the linked list.
     * If the list is empty, the head and tail will be set to the new node.
     * Otherwise, the new node will be appended to the tail.
     *
     * @param node the node to be added
     */
    public synchronized void add(LinkedListNode<T> node) {
        if (size == 0) {
            head = node;
            tail = node;
            size++;
            return;
        }
        tail.next = node;
        tail = tail.next;
        size++;
    }

    /**
     * Adds a node to the left end of the linked list.
     * If the list is empty, the node becomes the head and tail.
     * Otherwise, the node is added as the new head and the previous head is updated.
     *
     * @param node the node to be added
     */
    public synchronized void addLeft(LinkedListNode<T> node) {
        if (size == 0) {
            head = node;
            tail = node;
            size++;
            return;
        }
        head.prev = node;
        head = head.prev;
        size++;
    }

    /**
     * Removes and returns the element at the head of the linked list.
     * If the linked list is empty, returns null.
     *
     * @return the element at the head of the linked list, or null if the linked list is empty
     */
    public synchronized T remove() {
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            head = null;
            tail = null;
            size--;
            return null;
        }
        LinkedListNode<T> node = tail;
        tail = tail.prev;
        tail.next = null;
        size--;
        return node.value;
    }

    /**
        * Removes and returns the element at the left end of the linked list.
        *
        * @return the element at the left end of the linked list, or null if the list is empty
        */
    public synchronized T removeLeft() {
        if (size == 0) {
            return null;
        }
        LinkedListNode<T> node = head;
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        size--;
        return node.value;
    }
}
