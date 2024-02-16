package PubSub.Consumer;

import DataStructures.Graph.GraphUtils;
import DataStructures.Queue.BlockingQueue;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The GraphConsumer class represents a consumer thread that processes graphs from a blocking queue
 * and writes the valid graphs to a file.
 */
public class GraphConsumer extends Thread {
    private FileWriter fileWriter;
    private BlockingQueue<HashMap<Integer, ArrayList<Integer>>> queue;
    private GraphUtils<Integer> utils = new GraphUtils<>();

    /**
     * Constructs a GraphConsumer object with the specified file writer and blocking queue.
     *
     * @param fileWriter the file writer used to write the valid graphs
     * @param queue the blocking queue from which the graphs are consumed
     */
    public GraphConsumer(FileWriter fileWriter, BlockingQueue<HashMap<Integer, ArrayList<Integer>>> queue) {
        this.fileWriter = fileWriter;
        this.queue = queue;
    }

    /**
     * Runs the consumer thread. It continuously consumes graphs from the blocking queue,
     * checks if the graph is valid, and writes the valid graphs to the file.
     */
    @Override
    public void run() {
        while(true) {
            HashMap<Integer, ArrayList<Integer>> graph = queue.removeFromQueue();
            if (graph == null) {
                return;
            }
            if (!utils.hasCycle(graph) && utils.isConnected(graph)) {
                try {
                    String[] graphString = GraphUtils.convertGraphToString(graph);
                    for (String s : graphString) {
                        fileWriter.write(s);
                        fileWriter.write('\n');
                    }
                    fileWriter.write('\n');
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
