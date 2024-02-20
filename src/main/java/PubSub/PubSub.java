package PubSub;

import DataStructures.Queue.BlockingQueue;

import java.util.HashMap;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
/**
 * The PubSub class represents a publish-subscribe messaging pattern implementation.
 * It allows publishers to publish messages to specific topics, and subscribers to subscribe to those topics and receive the messages.
 */
public class PubSub {
    private final HashMap<String, BlockingQueue<Object>> topics;

    /**
     * Constructs a new PubSub object.
     * Initializes the topics HashMap.
     */
    PubSub() {
        this.topics = new HashMap<>();
    }

    /**
     * Publishes a message to the specified topic.
     * @param topic The topic to publish the message to.
     * @param value The message to be published.
     */
    public void publish(String topic, Object value) {
        topics.computeIfAbsent(topic, x -> new BlockingQueue<>()).addToQueue(value);
    }

    /**
     * Subscribes to a topic and starts a new thread to receive messages from that topic.
     * @param <T>           The type of the messages.
     * @param topic         The topic to subscribe to.
     * @param subscriber    The consumer function that will be called with each received message.
     */
    public <T> void subscribe(String topic, Consumer<T> subscriber) {
        BlockingQueue<Object> queue = topics.computeIfAbsent(topic, x -> new BlockingQueue<>());
        new Thread(() -> {
            while (true) {
                T task = (T) queue.removeFromQueue();
                subscriber.accept(task);
            }
        }).start();
    }

    /**
     * Stops publishing messages to the specified topic.
     * 
     * @param topic The topic to stop publishing messages to.
     */
    public void stopPublishing(String topic) {
        topics.computeIfAbsent(topic, x -> new BlockingQueue<>()).stop();
    }

    /**
     * Checks if publishing has stopped for the specified topic.
     * 
     * @param topic The topic to check.
     * @return true if publishing has stopped for the topic, false otherwise.
     */
    public boolean hasStoppedPublishing(String topic) {
        return !topics.computeIfAbsent(topic, x -> new BlockingQueue<>()).getIsRunning();
    }
}
