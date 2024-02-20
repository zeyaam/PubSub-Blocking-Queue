import PubSub.PubSubController;

import java.io.*;

public class Main {
    private static final String INPUT_COORDINATES_TXT = "./input/coordinates.txt";
    private static final String OUTPUT_CLOSEST_COORDS_TXT = "./output/closest_coords.txt";
    private static final String INPUT_GRAPH_TXT = "./input/graphs.txt";
    private static final String OUTPUT_GRAPH_TXT = "./output/dependency_graphs.txt";

    public static void main(String[] args) throws IOException, InterruptedException {
        int numProducers = 2;
        int numConsumers = 1;

        // Setup files for coordinates 
        File coordsInputFile = new File(INPUT_COORDINATES_TXT);
        File coordsOutputFile = new File(OUTPUT_CLOSEST_COORDS_TXT);

        // Setup files for graphs
        File graphInputFile = new File(INPUT_GRAPH_TXT);
        File graphOutputFile = new File(OUTPUT_GRAPH_TXT);

    
        PubSubController.startCoordinatesPubSub(coordsInputFile, coordsOutputFile, numProducers, numConsumers);
        PubSubController.startGraphPubSub(graphInputFile, graphOutputFile, numProducers, numConsumers);
        
        System.exit(0);
    }

}