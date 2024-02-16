package PubSub.Consumer;

import DataStructures.Queue.BlockingQueue;
import Model.Task.CoordinateTask;
import Model.Point;

import java.io.FileWriter;
import java.io.IOException;

/**
 * The CoordinatesConsumer class represents a consumer thread that consumes
 * CoordinateTask objects from a blocking queue,
 * calculates the closest point to a given point, and writes the result to a
 * file using a FileWriter.
 */
public class CoordinatesConsumer extends Thread {
    private FileWriter fileWriter;
    private BlockingQueue<CoordinateTask> queue;

    /**
     * A consumer class that reads CoordinateTask objects from a blocking queue and
     * writes them to a file.
     *
     * @param fileWriter The FileWriter object used to write the coordinates to a
     *                   file.
     * @param queue      The blocking queue from which CoordinateTask objects are
     *                   consumed.
     */
    public CoordinatesConsumer(FileWriter fileWriter, BlockingQueue<CoordinateTask> queue) {
        this.fileWriter = fileWriter;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            CoordinateTask task = queue.removeFromQueue();
            if (task == null) {
                return;
            }
            double minDist = Double.MAX_VALUE;
            Point closestPoint = null;
            for (int i = 0; i < task.points.length; i++) {
                double currDist = task.point.getDistance(task.points[i]);
                if (currDist < minDist) {
                    minDist = currDist;
                    closestPoint = task.points[i];
                }
            }
            assert closestPoint != null;
            String response = "Closest point to " + task.point.print() + " is " + closestPoint.print()
                    + " with distance " + minDist + '\n';
            System.out.println("Writing to File: " + response);
            try {
                fileWriter.write(response);
                fileWriter.write('\n');
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
