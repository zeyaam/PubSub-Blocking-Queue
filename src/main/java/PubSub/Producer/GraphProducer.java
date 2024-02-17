package PubSub.Producer;

import PubSub.PubSub;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The GraphProducer class represents a producer that reads graphs from a file
 * and adds them to a blocking queue.
 */
public class GraphProducer extends Thread {
    private final Scanner scanner;
    private final PubSub pubSub;
    private final int GRAPH_SIZE;
    private final String topicName;

    public GraphProducer(PubSub pubSub,
            String topicName,
            FileReader fileReader,
            int GRAPH_SIZE) {
        this.pubSub = pubSub;
        this.scanner = new Scanner(fileReader);
        this.GRAPH_SIZE = GRAPH_SIZE;
        this.topicName = topicName;
    }

    @Override
    public void run() {
        while (true) {
            HashMap<Integer, ArrayList<Integer>> newTask = readTask();
            if (newTask == null) {
                pubSub.stopPublishing(topicName);
                System.out.println("No more points to read, producer is shutting down");
                return;
            }
            pubSub.publish(topicName, newTask);
        }
    }

    /**
     * Reads a task from the input and returns a graph representation.
     * 
     * @return the graph representation as a HashMap<Integer, ArrayList<Integer>>
     *         object,
     *         or null if there are no more tasks or the task is invalid.
     */
    private HashMap<Integer, ArrayList<Integer>> readTask() {
        HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
        for (int i = 0; i < GRAPH_SIZE; i++) {
            if (!scanner.hasNext()) {
                return null;
            }
            ArrayList<Integer> line = Arrays.stream(scanner.nextLine().split(", ")).map(Integer::parseInt)
                    .collect(Collectors.toCollection(ArrayList::new));
            if (line.isEmpty()) {
                return null;
            }
            graph.put(line.get(0), new ArrayList<Integer>(line.subList(1, line.size())));
        }
        scanner.nextLine();
        return graph;
    }
}
