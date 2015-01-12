/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author dmvproject
 */
public class Point {

    /**
     * vecteur de double
     */
    public double[] v;
    // point de base DBScan
    public boolean isCore;
    // point de bord DBScan
    public boolean isBound;
    // point bruit DBScan
    public boolean isOutlier;
    // id pour identifier le point
    public int id = 0;
    // cluster associer au point 
    public int classe = -1;

    public Point(double[] x) {
        this.v = x;
    }

    public int getId() {
        return id;
    }

    public void setClasse(int classe) {
        this.classe = classe;
    }

    public int getClasse() {
        return classe;
    }

    public int getLength() {
        return v.length;
    }

    public double[] getP() {
        return v;
    }

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("(");
        for (int i = 0; i < v.length; i++) {
            s.append(v[i] + ",");
        }
        s.deleteCharAt(s.length() - 1);
        s.append(")");
        return s.toString();
    }

    public Point(double[] x, int id) {
        this.v = x;
        this.id = id;
    }

    public Point(int l) {
        v = new double[l];
        for (int i = 0; i < l; i++) {
            v[i] = 0d;
        }
    }

    public Point(Double[] l) {
        v = new double[l.length];
        for (int i = 0; i < l.length; i++) {
            v[i] = l[i].doubleValue();
        }
    }

    /**
     * calcule de distance eucliean carré
     *
     * @param p1 point
     * @param p2 point
     * @return distance
     */
    public static double squareEuclideanDistance(Point p1, Point p2) {
        double d = 0d;
        for (int i = 0; i < p1.v.length; i++) {
            d += (p1.v[i] - p2.v[i]) * (p1.v[i] - p2.v[i]);
        }
        return Math.sqrt(d);
    }

    /**
     * calculer la distance
     *
     * @param p1 point
     * @param p2 point
     * @return distance
     */
    public static Double euclideanDistance(Point p1, Point p2) {
        double distance = 0d;
        for (int i = 0; i < p2.v.length; i++) {
            distance += (p1.v[i] - p2.v[i]) * (p1.v[i] - p2.v[i]);
        }
        return distance;
    }

    /**
     * point aleatoir
     *
     * @param rand
     * @param size
     * @return
     */
    public static Point RandomPoint(Random rand, int size) {
        Point p = new Point(size);
        for (int i = 0; i < size; i++) {
            p.v[i] = rand.nextDouble() * 10;
        }
        return p;
    }

    /**
     * transformer des données numerique vers array list de points
     *
     * @param data
     * @return
     */
    public static ArrayList<Point> arrayDoubleToPoints(ArrayList<ArrayList<Object>> data) {
        ArrayList<Point> points = new ArrayList<>();
        int cmp = 0;
        for (ArrayList<Object> arrayList : data) {
            Point p = new Point(arrayList, cmp++);
            points.add(p);
        }
        return points;
    }

    public Point(ArrayList<Object> p, int id) {
        this.v = new double[p.size()];
        for (int i = 0; i < p.size(); i++) {
            v[i] = (Double) p.get(i);
        }
        this.id = id;
    }

    /**
     * transformer un point vers un double
     *
     * @return
     */
    public ArrayList<Object> pointtoDouble() {
        ArrayList<Object> d = new ArrayList<>();
        for (int i = 0; i < v.length; i++) {
            d.add(v[i]);
        }
        return d;
    }

    /**
     * calculer le centre d'un arraylist de point
     *
     * @param points
     * @return
     */
    public Point calculerCenter(ArrayList<Point> points) {
        Point s = new Point(points.get(0).v.length);
        return s;
    }

    public static ArrayList<Point> RandomPoint() {
        ArrayList<Point> listp = new ArrayList<>();
//            {1.0, 8.0, 1.5},
//            {2.0, 3.5, 1.6},
//            {4.0, 0.1, 1.7},
//            {1.0, 6.0, 2.5},
//            {1.0, 3.5, 0.6},
//            {1.0, 12.1, 3.7},
//            {6.0, 0.0, 2.5},
//            {2.0, 0.5, 0.6},
//            {0.5, 0.1, 3.7},
//            {8.0, 0.002, 1.5},
//            {8.0, 0.001, 0.6},
//            {8.02, 0.009, 1.2}
        listp.add(new Point(new double[]{0, 6}));
        listp.add(new Point(new double[]{1, 6}));
        listp.add(new Point(new double[]{2, 6}));
        listp.add(new Point(new double[]{3, 6}));
        listp.add(new Point(new double[]{4, 6}));
        listp.add(new Point(new double[]{5, 6}));
        listp.add(new Point(new double[]{5, 5}));
        listp.add(new Point(new double[]{6, 5}));
        listp.add(new Point(new double[]{6, 4}));
        listp.add(new Point(new double[]{7, 4}));
        listp.add(new Point(new double[]{7, 3}));
        listp.add(new Point(new double[]{8, 3}));
        listp.add(new Point(new double[]{8, 4}));
        listp.add(new Point(new double[]{8, 5}));

        listp.add(new Point(new double[]{2, 7}));
        listp.add(new Point(new double[]{1, 7}));
        listp.add(new Point(new double[]{3, 7}));

        listp.add(new Point(new double[]{3, 3}));
        listp.add(new Point(new double[]{4, 3}));
        listp.add(new Point(new double[]{3, 4}));

        listp.add(new Point(new double[]{21, 19}));
        listp.add(new Point(new double[]{20, 19}));
        listp.add(new Point(new double[]{20, 18}));
        for (Point point : listp) {
            // DBScan.showln(point + " ");
            System.out.println(point + " ");
        }
        return listp;
    }

    public static ArrayList<Point> RandomPoint0() {
        ArrayList<Point> listp = new ArrayList<>();
        listp.add(new Point(new double[]{1.0, 8.0, 1.5}));
        listp.add(new Point(new double[]{2.0, 3.5, 1.6}));
        listp.add(new Point(new double[]{4.0, 0.1, 1.7}));
        listp.add(new Point(new double[]{1.0, 6.0, 2.5}));
        listp.add(new Point(new double[]{1.0, 3.5, 0.6}));
        listp.add(new Point(new double[]{1.0, 12.1, 3.7}));
        listp.add(new Point(new double[]{6.0, 0.0, 2.5}));
        listp.add(new Point(new double[]{2.0, 0.5, 0.6}));
        listp.add(new Point(new double[]{0.5, 0.1, 3.7}));
        listp.add(new Point(new double[]{8.0, 0.002, 1.5}));
        listp.add(new Point(new double[]{8.0, 0.001, 0.6}));
        listp.add(new Point(new double[]{8.02, 0.009, 1.2}));
        for (Point point : listp) {
            //DBScan.showln(point + " ");
            System.out.println(point + " ");
        }
        return listp;
    }
}
