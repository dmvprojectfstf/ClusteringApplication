package kmeansclustering;

import java.util.ArrayList;
import java.util.List;
import util.Point;

/**
 *
 * @author dmvproject
 */
public class Cluster {

    private final List<Point> points;
    public  Point centroid;

    public Cluster(Point firstPoint) {
        points = new ArrayList<Point>();
        centroid = firstPoint;
    }

    public Point getCentroid() {
        return centroid;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void updateCentroid() {
        Point newpoint = new Point(centroid.getLength());
        for (Point point : points) {
            for (int i = 0; i < point.v.length; i++) {
                newpoint.v[i] += point.v[i];
            }
        }
        for (int i = 0; i < newpoint.v.length; i++) {
            newpoint.v[i] = newpoint.v[i]/points.size();
        }
        centroid = newpoint;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("ce cluster contient les points:\n");
        for (Point point : points) {
            builder.append(point.toString() + " id ="+point.id+" ,\n");
        }
        return builder.deleteCharAt(builder.length() - 2).toString();
    }
}
