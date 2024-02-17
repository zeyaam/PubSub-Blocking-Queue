package PubSub.Producer;

import java.io.FileReader;
import java.util.Scanner;

import Model.Point;
import Model.Task.CoordinateTask;
import PubSub.PubSub;

/**
 * The CoordinatesProducer class represents a producer that reads coordinate
 * tasks from a file
 * and adds them to a blocking queue.
 */
public class CoordinatesProducer extends Thread {
    private final PubSub pubSub;
    private final Scanner scanner;
    private final int NUM_COORDS;
    private final String topicName;

    /**
     * Constructs a new CoordinatesProducer with the specified parameters.
     *
     * @param fileReader the FileReader object used to read the coordinates from a
     *                   file
     * @param pubSub      the PubSub object used to publish tasks
     * @param NUM_COORDS the number of coordinates to be produced
     */
    public CoordinatesProducer(PubSub pubSub, String topicName, FileReader fileReader, int NUM_COORDS) {
        this.pubSub = pubSub;
        this.scanner = new Scanner(fileReader);
        this.NUM_COORDS = NUM_COORDS;
        this.topicName = topicName;
    }

    @Override
    public void run() {
        while (true) {
            CoordinateTask newTask = readTask();
            if (newTask == null) {
                pubSub.stopPublishing(topicName);
                System.out.println("No more points to read, producer is shutting down");
                return;
            }
            pubSub.publish(topicName, newTask);
        }
    }

    /**
     * Processes coordinates from the input file.
     * 
     * @return the Coordinate task read from the file, or null if there are no more
     */
    private CoordinateTask readTask() {
        Point[] points = new Point[100];
        CoordinateTask res = new CoordinateTask();
        for (int i = 0; i < NUM_COORDS + 1; i++) {
            if (!scanner.hasNext()) {
                return null;
            }
            String[] line = scanner.nextLine().split(",");
            if (line[0].isEmpty() || line[1].isEmpty()) {
                return null;
            }
            Point point = new Point(Double.parseDouble(line[0]), Double.parseDouble(line[1]));
            if (i == 0) {
                res.point = point;
            } else {
                points[i - 1] = point;
            }
        }
        scanner.nextLine();
        res.points = points;
        return res;
    }
}
