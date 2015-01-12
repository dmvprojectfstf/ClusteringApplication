package kmeansclustering;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import util.Point;

/**
 *
 * @author dmvproject
 */
public class KMeans {

    
    public final List<Point> points;
    private static final Random random = new Random();
    public final int k;
    public int iteration = 0;
    public ArrayList<Cluster> clusters = null;
    public static boolean isChanged = true;

    public KMeans(ArrayList<Point> points, int k) {
        this.k = k;
        //clusters = new ArrayList<>();
        this.points = Collections.unmodifiableList(points);
    }

    public int clusterCount() {
        return clusters.size();
    }

    private ArrayList<Cluster> getInitialKRandomSeeds(List<Point> points, int k) {
        //Clusters clusters = new Clusters(points);
        ArrayList<Cluster> clusters = new ArrayList<>();
        List<Point> randomPoints = getKRandomPoints(points, k);
        for (int i = 0; i < k; i++) {
            randomPoints.get(i).setClasse(i);
            clusters.add(new Cluster(randomPoints.get(i)));
        }
        return clusters;
    }

    /**
     *
     * @param points
     * @param k
     * @return
     */
    private List<Point> getKRandomPoints(List<Point> points, int k) {
        List<Point> kRandomPoints = new ArrayList<>();
        int size = points.size();
        boolean[] alreadyChosen = new boolean[size];
        for (int i = 0; i < size; i++) {
            alreadyChosen[i] = false;
        }
//        System.out.println("Size : " + size);
        System.out.println("k : " + k);
        for (int i = 0; i < k; i++) {
            int index = -1, r = random.nextInt(size--) + 1;
            for (int j = 0; j < r; j++) {
                index++;
                while (alreadyChosen[index]) {
                    index++;
                }
            }
            //System.out.println(allPoints.get(index));
            kRandomPoints.add(points.get(index));
            alreadyChosen[index] = true;
        }
        return kRandomPoints;
    }

    /**
     *
     * @return
     */
    public ArrayList<Cluster> getClusters() {
        return clusters;
    }

    // run classification 
    public void run() {
        // etape 1 : initialisation
        clusters = getInitialKRandomSeeds(points, k);
        isChanged = assignPointsToClusters();
        // etape 2 : classification 
        while (isChanged) {
            isChanged = false;
            // etape 2.1 : Calcule des controids
            updateClusters();
            // etape 2.2 : mise a jours des clusters 
            isChanged = assignPointsToClusters();
            // incrementation du nombre d'iteration
            iteration++;
        }
    }

    public boolean assignPointsToClusters() {
        for (Point point : points) {
            int previousIndex = point.getClasse();
            int newIndex = getNearestCluster(clusters, point);
            if (previousIndex != newIndex) {
                KMeans.isChanged = true;
            }
            Cluster target = clusters.get(newIndex);
            point.setClasse(newIndex);
            target.getPoints().add(point);
        }
        return isChanged;
    }

    public static Integer getNearestCluster(ArrayList<Cluster> clusters, Point point) {
        double minDistance = Double.MAX_VALUE;
        int itsIndex = -1;
        for (int i = 0; i < clusters.size(); i++) {
            double squareOfDistance = Point.euclideanDistance(point, clusters.get(i).getCentroid());
            if (squareOfDistance < minDistance) {
                minDistance = squareOfDistance;
                itsIndex = i;
            }
        }
        return itsIndex;
    }

    public void updateClusters() {
        for (Cluster cluster : clusters) {
            cluster.updateCentroid();
            cluster.getPoints().clear();
        }
    }

    public static void main_1(String[] args) {
        ArrayList<Point> points = readFile("C:\\Users\\vector\\Documents\\iris_.data");
        //KMeans kmeans = new KMeans(Point.RandomPoint0(), 4);
        KMeans km = new KMeans(points, 3);
        System.out.println("########################");
        km.run();
        System.out.println("#########################");
        for (int i = 0; i < km.points.size(); i++) {
            System.out.println(km.points.get(i) + "    " + km.points.get(i).getClasse());
        }
    }

    public ArrayList<ArrayList<Object>> toResult() {
        ArrayList<ArrayList<Object>> resulta = new ArrayList<>();
        // System.out.println("#########################");
        for (int i = 0; i < points.size(); i++) {
            //    System.out.println(points.get(i) + "    " + classes[i]);
            resulta.add(points.get(i).pointtoDouble());
            resulta.get(i).add((int) points.get(i).classe);
        }
        return resulta;
    }

    public ArrayList<ArrayList<Object>> toResultImage() {
        ArrayList<ArrayList<Object>> resulta = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Point center = clusters.get(points.get(i).classe).centroid;
            // System.out.println("Centre de  i =" +i+" "+center);
            resulta.add(center.pointtoDouble());
        }
        return resulta;
    }

    public static ArrayList<Point> readFile(String file) {
        ArrayList<Point> points = new ArrayList<Point>();
        try {
            InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file), "UTF-8");
            BufferedReader reader = new BufferedReader(read);
            String line;
            int cc = 0;
            String[] country = null;
            while (true) {
                line = reader.readLine();
                if (line == null || line.equals("")) {
                    break;
                }
                country = line.split(",|;|\t");
                //System.out.println(line);
                Point p = new Point(country.length);
                for (int i = 0; i < country.length; i++) {
                    p.v[i] = Double.parseDouble(country[i]);
                }
                p.id = cc++;
                points.add(p);
                //System.out.println();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return points;
    }

}
