
package dbscanclustering;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;
import util.Point;

/**
 * Algorithme DBScan
 * @author dmvproject
 */
public class DBScan {

    ArrayList<ArrayList<Point>> neighboursPoint;
    public ArrayList<Point> points;
    ArrayList<ArrayList<Double>> distances;
    ArrayList<Double> distanceFromCores;
    ArrayList<ArrayList<Point>> clusters;
    ArrayList<Point> noise;
    ArrayList<Point> outlier;
    public boolean auto = false;
    private int minPoints;
    private double epsilon;

    public static boolean show = true;

    public static void show(String s) {
        if (show) {
            System.out.print(s);
        }
    }

    public static void showln(String s) {
        if (show) {
            System.out.println(s);
        }
    }
/**
 *  Constructeur 
 * @param p min points
 * @param r rayon epsilon
 */
    DBScan(int p, double r) {
        minPoints = p;
        epsilon = r;
        points = new ArrayList<>();
        distances = new ArrayList<>();
        neighboursPoint = new ArrayList<>();
        distanceFromCores = new ArrayList<>();
        clusters = new ArrayList<>();
        noise = new ArrayList<>();
        outlier = new ArrayList<>();
    }
/**
 * 
 * @param dataset données d'entrée
 * @param p moipoint 
 * @param r epsilon
 */
    public DBScan(ArrayList<Point> dataset, int p, double r) {
        minPoints = p;
        epsilon = r;
        points = dataset;
//      corePoints = new ArrayList<>();
        distances = new ArrayList<>();
        neighboursPoint = new ArrayList<>();
        distanceFromCores = new ArrayList<>();
        clusters = new ArrayList<>();
        noise = new ArrayList<>();
        outlier = new ArrayList<>();
    }
    /**
     * nombre de cluster
     * @return 
     */
    public int clusterCount() {
        return clusters.size();
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public void Print() {
        System.out.println("radius is " + epsilon + " " + "," + " " + "Minimum points are " + minPoints);
    }

    public static void main_1(String args[]) {
        boolean auto = true;
        DBScan db = new DBScan(Point.RandomPoint0(), 5, 0.4);
        db.run();
        for (int i = 0; i < db.points.size(); i++) {
            System.out.println(db.points.get(i) + "    " + db.points.get(i).getClasse());
        }
    }

    static public String typeOf(Point p) {
        if (p.isCore) {
            return "is Core";
        }
        if (p.isBound) {
            return "is Bound";
        }
        return "is OutLier";
    }

    public void initialize() {
        for (int i = 0; i < points.size(); i++) {
            distances.add(new ArrayList<Double>());
            neighboursPoint.add(new ArrayList<Point>());
            distanceFromCores.add(0d);
            for (int j = 0; j < points.size(); j++) {
                distances.get(i).add(0d);
            }
        }
    }

    public void calculeDistances() {
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size(); j++) {
                distances.get(i).set(j, Point.squareEuclideanDistance(points.get(i), points.get(j)));
                //show("d(" + i + "," + j + ")=" + distances.get(i).get(j) + "   \t");
            }
            //showln("");
        }
    }

    public void run() {
        // Etape 1 : intitialisation et calcule de distance
        initialize();
        calculeDistances();
        // calsul automatique de epsilon
        if (auto) {
            double epsilonAuto = 0d;
            for (int i = 0; i < distances.size(); i++) {
                for (int j = 0; j < distances.get(i).size(); j++) {
                    epsilonAuto += distances.get(i).get(j);
                }
            }
            epsilon = 0.6 * epsilonAuto / (distances.size() * distances.size());
            System.out.println("Epsilone automatique = " + BigDecimal.valueOf(epsilonAuto).toPlainString());
        }
        // Etape 2 : trouver les points de base des clusters
        for (int k = 0; k < points.size(); k++) {
            for (int l = 0; l < points.size(); l++) {
                if (distances.get(k).get(l) < epsilon) {
                    neighboursPoint.get(k).add(points.get(l));
                }
            }
            isCore(k);
        }
        // Etape 3 : Etendre les cluster avec des bordes
        while (!noise.isEmpty()) {
            for (int i = 0; i < noise.size(); i++) {
                Point p = noise.get(i);
                isBoundary(points.indexOf(p));
            }
        }
    }

    public boolean isCore(int k) {
        if (neighboursPoint.get(k).size() >= minPoints) {
            //showln(" and is a Core point : " + dbscan.get(k).toString());
            if (clusters.isEmpty()) {
                clusters.add(new ArrayList<Point>());
                clusters.get(clusters.size() - 1).add(points.get(k));
                points.get(k).classe = clusters.size() - 1;
                points.get(k).isCore = true;
            } else {
                for (int i = 0; i < clusters.size(); i++) {
                    for (int l = 0; l < clusters.get(i).size(); l++) {
                        if (neighboursPoint.get(k).contains(clusters.get(i).get(l))) {
                            clusters.get(i).add(points.get(k));
                            points.get(k).isCore = true;
                            points.get(k).classe = i;
                            return true;
                        }
                    }
                }
                clusters.add(new ArrayList<Point>());
                clusters.get(clusters.size() - 1).add(points.get(k));
                points.get(k).classe = clusters.size() - 1;
                points.get(k).isCore = true;
            }
            return true;
        } else {
            noise.add(points.get(k));
            return false;
        }
    }

    public boolean isBoundary(int k) {
        if (neighboursPoint.get(k).size() >= 1) {
            for (int i = 0; i < clusters.size(); i++) {
                for (int j = 0; j < clusters.get(i).size(); j++) {
                    distanceFromCores.set(j, Point.squareEuclideanDistance(points.get(k), clusters.get(i).get(j)));
                    if (distanceFromCores.get(j) <= epsilon) {
                        if (clusters.isEmpty()) {
                            clusters.add(new ArrayList<Point>());
                            clusters.get(clusters.size() - 1).add(points.get(k));
                            points.get(k).classe = clusters.size() - 1;
                            points.get(k).isBound = true;
                            noise.remove(points.get(k));
                        } else {
                            for (int n = 0; n < clusters.size(); n++) {
                                for (int l = 0; l < clusters.get(n).size(); l++) {
                                    if (neighboursPoint.get(k).contains(clusters.get(n).get(l))) {
                                        clusters.get(i).add(points.get(k));
                                        points.get(k).isBound = true;
                                        noise.remove(points.get(k));
                                        return true;
                                    }
                                }
                            }
                            clusters.add(new ArrayList<Point>());
                            clusters.get(clusters.size() - 1).add(points.get(k));
                            points.get(k).classe = clusters.size() - 1;
                            points.get(k).isBound = true;
                            noise.remove(points.get(k));
                        }
                        // System.out.println("Boundary point added to core : " + dbscan.get(k).toString());
                        return true;
                    }
                }
            }
        }
        outlier.add(points.get(k));
        points.get(k).isOutlier = true;
        noise.remove(points.get(k));
        return false;
    }

    public boolean isOutlier(int k) {
        if (neighboursPoint.get(k).size() < minPoints && !points.get(k).isBound) {
            //    System.out.println("Outlier : " + dbscan.get(k) + " class = " + dbscan.get(k).lab);
            return true;
        }
        return false;
    }

    /**
     *
     * @return les centres
     */
    public ArrayList<Point> calculerCenters() {
//        pour chauqe cluster ona un centre donc le nombre de centre = nbcluster 

        ArrayList<Point> centres = new ArrayList<>();
        Double l = 0d;
        // on parcour chaque cluster
        for (int i = 0; i < clusters.size(); i++) {
            // la taille de vecteur (point) centre = taille de point
            Point centre = new Point(points.get(0).v.length);
            // on parcours chaque point de cluster
            for (int j = 0; j < clusters.get(i).size(); j++) {
                // on parcour la vecteur v
                for (int k = 0; k < clusters.get(i).get(j).v.length; k++) {
                    centre.v[k] += clusters.get(i).get(j).v[k];
                }
            }
            // on divise le v[k] par le nombre de pt dans le cluster
            for (int k = 0; k < centre.v.length; k++) {
                // bien
                centre.v[k] = centre.v[k] / clusters.get(i).size();
            }
            // on ajout le centre au centreS
            centres.add(centre);
        }
        // fin du methode alors daba kemli dakchi li b9a hani hadik ila bghiti chi haja ketbiha
        // liya  ok ? 
        return centres;
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
        // pour chaque point a  classe = numero de cluster 
        // = numero de centre car centre de cluster   
        ArrayList<Point> centres = calculerCenters();
        for (int i = 0; i < points.size(); i++) {
            // le problem sont les points bruit alors en evite les point bruit 
            // si class = -1 => bruit 
            // on ajoute  au resultat un nouveau array list => point 
            resulta.add(new ArrayList<Object>());
            if (points.get(i).classe == -1) {
                // en peux dire que ce point est noir par exemple
                // et le centre est  initialiser par  (0,0,0,0, ..... )
                // cas de image c RGB(0,0,0)
                for (int j = 0; j < points.get(i).v.length; j++) {
                    resulta.get(i).add(0d);
                }
            } else {
                // dans les autres cas c'est simple 
                for (int j = 0; j < points.get(i).v.length; j++) {
                    resulta.get(i).add(centres.get(points.get(i).classe).v[j]);
                }

            }
            //Point center=clusters.get(points.get(i).classe);
            //System.out.println("Centre de  i =" +i+" "+center);
            //resulta.add(center.pointtoDouble());
        }
        return resulta;
    }
}
