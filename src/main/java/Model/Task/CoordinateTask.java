package Model.Task;

import Model.Point;

/**
 * Represents a task that involves coordinates.
 */
public class CoordinateTask {
    public Point point;
    public Point[] points;

    /**
     * Default constructor for CoordinateTask.
     */
    public CoordinateTask() {}

    /**
     * Constructor for CoordinateTask with specified point and points array.
     * 
     * @param point The main point of the task.
     * @param points An array of points related to the task.
     */
    public CoordinateTask(Point point, Point[] points) {
        this.point = point;
        this.points = points;
    }
}
