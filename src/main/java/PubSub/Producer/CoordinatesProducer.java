package PubSub.Producer;

import java.io.FileReader;
import java.util.Scanner;

import DataStructures.Queue.BlockingQueue;
import Model.Point;
import Model.Task.CoordinateTask;

/**
 * The CoordinatesProducer class represents a producer that reads coordinate
 * tasks from a file
 * and adds them to a blocking queue.
 */
public class CoordinatesProducer extends Thread {
    private Scanner scanner;
    private BlockingQueue<CoordinateTask> queue;
    private int NUM_COORDS;

    /**
     * Constructs a new CoordinatesProducer with the specified parameters.
     *
     * @param fileReader the FileReader object used to read the coordinates from a
     *                   file
     * @param queue      the BlockingQueue object used to store the coordinate tasks
     * @param NUM_COORDS the number of coordinates to be produced
     */
    public CoordinatesProducer(FileReader fileReader, BlockingQueue<CoordinateTask> queue, int NUM_COORDS) {
        this.scanner = new Scanner(fileReader);
        this.queue = queue;
        this.NUM_COORDS = NUM_COORDS;
    }

    @Override
    public void run() {
        while (true) {
            CoordinateTask newTask = readTask();
            if (newTask == null) {
                queue.stop();
                System.out.println("No more points to read, producer is shutting down");
                return;
            }
            queue.addToQueue(newTask);
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
