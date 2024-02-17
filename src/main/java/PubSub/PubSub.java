package PubSub;

import DataStructures.Queue.BlockingQueue;

import java.util.HashMap;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class PubSub {
    private final HashMap<String, BlockingQueue<Object>> topics;

    PubSub() {
        this.topics = new HashMap<>();
    }

    public void publish(String topic, Object value) {
        topics.computeIfAbsent(topic, x -> new BlockingQueue<>()).addToQueue(value);
    }

    public <T> void subscribe(String topic, Consumer<T> subscriber) {
        BlockingQueue<Object> queue = topics.computeIfAbsent(topic, x -> new BlockingQueue<>());
        new Thread(() -> {
            while (true) {
                T task = (T) queue.removeFromQueue();
                subscriber.accept(task);
            }
        }).start();
    }

    public void stopPublishing(String topic) {
        topics.computeIfAbsent(topic, x -> new BlockingQueue<>()).stop();
    }

    public boolean hasStoppedPublishing(String topic) {
        return !topics.computeIfAbsent(topic, x -> new BlockingQueue<>()).getIsRunning();
    }
}
