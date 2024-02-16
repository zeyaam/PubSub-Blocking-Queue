package DataStructures.LinkedList;

/**
 * Represents a node in a linked list.
 *
 * @param <T> the type of value stored in the node
 */
public class LinkedListNode<T> {
    T value;
    LinkedListNode<T> next = null;
    LinkedListNode<T> prev = null;

    /**
     * Constructs a new LinkedListNode with the specified value, next node, and previous node.
     *
     * @param value the value to be stored in the node
     * @param next  the next node in the linked list
     * @param prev  the previous node in the linked list
     */
    public LinkedListNode(T value, LinkedListNode<T> next, LinkedListNode<T> prev) {
        this.value = value;
        this.next = next;
        this.prev = prev;
    }

    /**
     * Constructs a new LinkedListNode with the specified value.
     *
     * @param value the value to be stored in the node
     */
    public LinkedListNode(T value) {
        this.value = value;
    }
}
