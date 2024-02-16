package Model;

/**
 * Represents a point in a two-dimensional coordinate system.
 */
public class Point {
    private volatile double x;
    private volatile double y;

    /**
     * Constructs a Point object with the specified x and y coordinates.
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate of the point.
     *
     * @return the x-coordinate of the point
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the point.
     *
     * @return the y-coordinate of the point
     */
    public double getY() {
        return y;
    }

    /**
     * Calculates the distance between this point and the specified point.
     *
     * @param coordinate the point to calculate the distance to
     * @return the distance between this point and the specified point
     */
    public double getDistance(Point coordinate) {
        return Math.sqrt(Math.pow(x - coordinate.getX(), 2) + Math.pow(y - coordinate.getY(), 2));
    }

    /**
     * Returns a string representation of the point.
     *
     * @return a string representation of the point in the format "X: x-coordinate Y: y-coordinate"
     */
    public String print() {
        return "X: " + x + " Y: " + y;
    }
}
