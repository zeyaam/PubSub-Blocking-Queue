package DataStructures.Queue;

import DataStructures.LinkedList.LinkedList;
import DataStructures.LinkedList.LinkedListNode;

/**
 * A blocking queue implementation that extends LinkedList.
 * This queue has a maximum size and supports thread-safe operations.
 *
 * @param <T> the type of elements stored in the queue.
 */
public class BlockingQueue<T> extends LinkedList<T> {
    private LinkedList<T> queue = new LinkedList<T>();
    private boolean isEmpty = true;
    private boolean isRunning = true;
    private final int MAX_SIZE;

    public BlockingQueue(int size) {
        this.MAX_SIZE = size;
    }

    public BlockingQueue() {
        this.MAX_SIZE = 25;
    }

    /**
     * Adds the specified value to the blocking queue.
     * If the queue is at its maximum capacity and is still running, the method will wait until space becomes available.
     * If the thread is interrupted while waiting, a RuntimeException will be thrown.
     * After adding the value to the queue, the method notifies the consumer and updates the queue size.
     *
     * @param value the value to be added to the queue.
     */
    public synchronized void addToQueue(T value) {
        while(queue.size() >= MAX_SIZE && isRunning) {
            try {
                System.out.println("Queue at capacity " + MAX_SIZE);
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (isRunning) {
            queue.add(new LinkedListNode<T>(value));
            isEmpty = false;
            System.out.println("Notifying consumer. Queue size: " + queue.size());
            notify();
        }
    }

    /**
     * Removes and returns an element from the queue.
     * If the queue is empty and the queue is still running, the method will wait until an element is available.
     * If the queue is empty and the queue is no longer running, the method will return null.
     * If the queue size becomes less than the maximum size, producers will be notified.
     * 
     * @return the element removed from the queue, or null if the queue is empty and no longer running.
     */
    public synchronized T removeFromQueue() {
        while (isEmpty && isRunning) {
            try {
                System.out.println("Queue is empty " + queue.size());
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (queue.size() == 0 && !isRunning) {
            return null;
        }

        T value = queue.removeLeft();
        if (queue.size() == 0) {
            isEmpty = true;
        }

        if (queue.size() < MAX_SIZE) {
            System.out.println("Notifying producers. Queue size: " + queue.size());
            notifyAll();
        }
        System.out.println("Consumed value. Queue size: " + queue.size());

        return value;
    }

    public void stop() {
        isRunning = false;
    }

    public int size() {
        return queue.size();
    }

    public boolean getIsRunning() {
        return isRunning;
    }

}
