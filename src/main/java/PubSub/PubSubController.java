package PubSub;

import DataStructures.Graph.GraphUtils;
import DataStructures.Queue.BlockingQueue;
import PubSub.Consumer.CoordinatesConsumer;
import PubSub.Consumer.GraphConsumer;
import PubSub.Producer.CoordinatesProducer;
import PubSub.Producer.GraphProducer;
import Model.Task.CoordinateTask;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * The PubSubController class is responsible for coordinating the publishing and
 * subscribing of data
 * using blocking queues. It provides methods to start the publication and
 * subscription of coordinates
 * and graphs.
 */
public class PubSubController {
    private static final int NUM_COORDS = 100;
    private static final int NUM_GRAPHS = 200;
    private static final boolean POPULATE_COORDS = true;
    private static final boolean CREATE_GRAPHS = false;
    private static final int GRAPH_SIZE = 30;

    public PubSubController() {
    }

    /**
     * Starts the coordinates publishing and subscribing process.
     * 
     * @param inputFile  the input file containing coordinates
     * @param outputFile the output file to write the processed coordinates
     * @param numProd    the number of producer threads
     * @param numCons    the number of consumer threads
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    public static void startCoordinatesPubSub(File inputFile, File outputFile, int numProd, int numCons)
            throws IOException, InterruptedException {
        FileWriter fileWriter = new FileWriter(outputFile);
        BlockingQueue<CoordinateTask> queue = new BlockingQueue<CoordinateTask>();
        if (POPULATE_COORDS) {
            populateCoordinates(inputFile);
        }

        ArrayList<CoordinatesProducer> coordinatesProducers = new ArrayList<CoordinatesProducer>();
        ArrayList<CoordinatesConsumer> coordinatesConsumers = new ArrayList<CoordinatesConsumer>();

        for (int i = 0; i < numProd; i++) {
            coordinatesProducers.add(new CoordinatesProducer(new FileReader(inputFile), queue, NUM_COORDS));
        }

        for (int j = 0; j < numCons; numCons++) {
            coordinatesConsumers.add(new CoordinatesConsumer(fileWriter, queue));
        }

        for (CoordinatesConsumer c : coordinatesConsumers) {
            c.start();
        }

        for (CoordinatesProducer p : coordinatesProducers) {
            p.start();
        }

        while (true) {
            if (!queue.getIsRunning()) {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                    for (int i = 0; i < numProd; i++) {
                        coordinatesProducers.get(i).join();
                    }
                    for (int i = 0; i < numCons; i++) {
                        coordinatesConsumers.get(i).join();
                    }
                    break;
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            TimeUnit.SECONDS.sleep(1);
        }
    }

    /**
     * Starts the graph publishing and subscribing process.
     * 
     * @param inputFile  the input file containing graph data
     * @param outputFile the output file to write the results
     * @param numProd    the number of graph producers
     * @param numCons    the number of graph consumers
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public static void startGraphPubSub(File inputFile, File outputFile, int numProd, int numCons)
            throws IOException, InterruptedException {
        FileWriter fileWriter = new FileWriter(outputFile);
        BlockingQueue<HashMap<Integer, ArrayList<Integer>>> queue = new BlockingQueue<>();
        if (CREATE_GRAPHS) {
            generateAdjacencyLists(inputFile);
        }
        ArrayList<GraphProducer> graphProducers = new ArrayList<>();
        ArrayList<GraphConsumer> graphConsumers = new ArrayList<>();

//        GraphProducer graphProducer = new GraphProducer(new FileReader(inputFile), queue, NUM_GRAPHS, GRAPH_SIZE);
//        graphProducer.join(3000);
//
//        GraphConsumer graphConsumer = new GraphConsumer(fileWriter, queue);
//        graphConsumer.join(3000);
//
//        graphConsumer.start();
//        graphProducer.start();

        for (int i = 0; i < numProd; i++) {
            graphProducers.add(new GraphProducer(new FileReader(inputFile), queue, NUM_GRAPHS, GRAPH_SIZE));
        }

        for (int j = 0; j < numCons; j++) {
            graphConsumers.add(new GraphConsumer(fileWriter, queue));
        }

        for (GraphConsumer c : graphConsumers) {
            c.start();
        }

        for (GraphProducer p : graphProducers) {
            p.start();
        }

        while (true) {
            if (!queue.getIsRunning()) {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                    for (int i = 0; i < numProd; i++) {
                        graphProducers.get(i).join();
                    }
                    for (int i = 0; i < numCons; i++) {
                        graphConsumers.get(i).join();
                    }
                    break;
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            TimeUnit.SECONDS.sleep(1);
        }

    }

    /**
     * Generates adjacency lists for graphs and writes them to the specified output
     * file.
     *
     * @param outputFile the file to write the adjacency lists to
     * @throws IOException if an I/O error occurs while writing to the file
     */
    public static void generateAdjacencyLists(File outputFile) throws IOException {
        FileWriter fileWriter = new FileWriter(outputFile);
        Random rand = new Random();
        GraphUtils<Integer> utils = new GraphUtils<>();
        HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
        for (int i = 0; i < NUM_GRAPHS; i++) {
            int graphType = rand.nextInt(3);
            switch (graphType) {
                case 0:
                    graph = GraphUtils.generateDependencyGraph(GRAPH_SIZE);
                    break;
                case 1:
                    graph = GraphUtils.generateRandomGraph(GRAPH_SIZE, true);
                    break;
                case 2:
                    graph = GraphUtils.generateRandomGraph(GRAPH_SIZE, false);
                    break;
                default:
                    break;
            }
            String[] graphString = GraphUtils.convertGraphToString(graph);
            for (String s : graphString) {
                fileWriter.write(s);
                fileWriter.write('\n');
            }
            fileWriter.write('\n');
        }
        fileWriter.flush();
        fileWriter.close();
    }

    /**
     * Populates the given output file with random coordinates.
     * Each coordinate is written as a pair of x and y values separated by a comma.
     * The coordinates are generated within the range of 0.0 to 100.0.
     * The file is populated with a total of 250 sets of coordinates.
     *
     * @param outputFile the file to populate with coordinates
     * @throws IOException if an I/O error occurs while writing to the file
     */
    public static void populateCoordinates(File outputFile) throws IOException {
        FileWriter fileWriter = new FileWriter(outputFile);
        Random rand = new Random();
        for (int c = 0; c < 250; c++) {
            for (int i = 0; i < NUM_COORDS + 1; i++) {
                StringJoiner stringJoiner = new StringJoiner(", ");
                double x = rand.nextDouble() * 100.0;
                double y = rand.nextDouble() * 100.0;
                stringJoiner.add(String.valueOf(x));
                stringJoiner.add(String.valueOf(y));
                fileWriter.write(stringJoiner.toString());
                fileWriter.write('\n');
            }
            fileWriter.write('\n');
        }
        fileWriter.flush();
        fileWriter.close();
    }

}
