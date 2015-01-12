
package fcmclustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import fcmclustering.Matrix.Builder;
import util.Point;

public class FuzzyCMeansAlgorithm {

    /**
     * Actual matrix
     */
    private Matrix matrixU;
    /**
     * Old matrix
     */
    private Matrix oldMatrixU;

    /**
     * Tolerance level, predefined threshold
     */
    private double toleranceLevel;
    /**
     * Fuzziness value
     */
    private double m;

    /**
     * Features or points at cartesian plane
     */
    private List<Point> points;
    /**
     * List of centroids
     */
    private List<Point> centroids;
    /**
     * Number of clusters
     */
    private int nbCluster;

    /**
     * Auxiliary variables
     */
    private boolean initialized;
    private boolean finish;
    private int iteration;
    private double actualError;
    private int maxIteration = 1000;

    public static boolean showComment = false;

    /**
     * @param nbCluster
     * @param initFeatures
     */
    public FuzzyCMeansAlgorithm(int nbCluster, double m, ArrayList<Point> initFeatures, double toleranceLevel, double[][] matrix) {
        this.m = m;
        this.iteration = 0;

        this.nbCluster = nbCluster;
        this.points = initFeatures;
        this.toleranceLevel = toleranceLevel;
        System.out.println("Features Size " + points.size());
        this.centroids = new ArrayList<Point>(this.nbCluster);
        if (matrix != null) {
            initMatrixU(matrix);
            this.initialized = true;
        } else {
            this.initialized = false;
        }
    }

    public FuzzyCMeansAlgorithm(int nbCluster, double m, ArrayList<Point> initFeatures, double toleranceLevel, double[][] matrix, int maxIteration) {
        this(nbCluster, m, initFeatures, toleranceLevel, matrix);
        this.maxIteration = maxIteration;
    }
    public int clusterCount() {
        return centroids.size();
    }
    /**
     * Execute Fuzzy C-Means clustering algorithm: iterative optimization
     */
    public List<Point> run() {
        if (!initialized) {
           // show("  initialize();");
            initialize();
        }
        do {
           // show("  calculateClusterCenters();");
            calculateClusterCenters();
           // show("  updateAllDataSamples();");
            updateAllDataSamples();
            iteration++;
            //show("  iteration++; = " + iteration);
        } while (verifyToleranceLevel() && iteration < maxIteration);
        // assignier chaque point au cetroid qui a le plus grand degrer d'appartennance 
        for (int i = 0; i < points.size(); i++) {
            int indexOfMax = 0;
            double max = Double.MIN_VALUE;
            for (int j = 0; j < getNbCluster(); j++) {
                //    System.out.println("####" + i + "   " + j);
                if (max < getMatrixU().valueAt(j, i)) {
                    indexOfMax = j;
                    max = getMatrixU().valueAt(j, i);
                }
            }
            points.get(i).classe = indexOfMax;
            //    System.out.println(max);
        }
        this.finish = true;
        return this.centroids;
    }

    /**
     * Execute an iteration
     */
    public List<Point> executeAnIteration() {
        if (!initialized) {
            initialize();
        }
        calculateClusterCenters();
        updateAllDataSamples();

        this.finish = !verifyToleranceLevel();
        return this.centroids;
    }

    static public void show(String s) {
        if (showComment) {
            System.out.println(s);
        }
    }

    /**
     * Step 1: Initialize
     */
    private void initialize() {
        initMatrixU();
        this.initialized = true;
    }

    /**
     * Step 2: Calculate cluster centers
     */
    private void calculateClusterCenters() {
        ArrayList<Point> list = new ArrayList<Point>(this.nbCluster);

        for (int clusterCenter = 0; clusterCenter < this.nbCluster; clusterCenter++) {
            double amount = 0.0;
            Point centroid = new Point(this.points.get(0).getLength());
            for (int i = 0; i < this.points.get(0).getLength(); i++) {
                centroid.v[i] = 0.0d;
            }
//            for (int point = 0; point < this.features.size(); point++) {
//                sumX += Math.pow(this.matrixU.valueAt(clusterCenter, point), this.m) * this.features.get(point)[0];
//                sumY += Math.pow(this.matrixU.valueAt(clusterCenter, point), this.m) * this.features.get(point)[1];
//
//                amount += Math.pow(this.matrixU.valueAt(clusterCenter, point), this.m);
//            }

            for (int i = 0; i < this.points.size(); i++) {
                //double v = 0.0d;
                for (int j = 0; j < centroid.getLength(); j++) {
                    centroid.v[j] += Math.pow(this.matrixU.valueAt(clusterCenter, i), this.m) * this.points.get(i).v[j];
                }
                amount += Math.pow(this.matrixU.valueAt(clusterCenter, i), this.m);
            }
            for (int i = 0; i < this.points.get(0).getLength(); i++) {
                centroid.v[i] = centroid.v[i] / amount;
            }


            /* Create the new centroid */
            //double[] centroid = new double[]{sumX / amount, sumY / amount};
            list.add(centroid);
        }
        /* Update list of centroids */
        this.centroids = list;
    }

    /**
     * Step 3: For all clusters and for all data samples, update matrix U.
     */
    private void updateAllDataSamples() {
        Builder builder = Matrix.builder().dimensions(this.nbCluster, this.points.size());
        double exponent = 2.0 / (this.m - 1);
        /* loop over clusters */
        for (int line = 0; line < this.nbCluster; line++) {
            /* loop over features */
            for (int col = 0; col < this.points.size(); col++) {
                double denominator = 0.0;
                /* dik = d(xk - vi) *//* dik = d(xk - vi) */
                /**/ double dik = Point.euclideanDistance(this.points.get(col), this.centroids.get(line));
                if (dik != 0.0) {
                    for (int s = 0; s < this.nbCluster; s++) {
                        /**/ double dsk = Point.euclideanDistance(this.points.get(col), this.centroids.get(s));
                        double ratio = Math.pow((dik / dsk), exponent);
                        denominator += ratio;
                    }
                    builder.valueAt(line, col, 1.0 / denominator);
                } else {
                    builder.valueAt(line, col, 0.0);
                }
            }
        }

        this.oldMatrixU = this.matrixU;
        this.matrixU = builder.build();
    }

    /**
     * Step 4: Tolerance Level
     */
    private boolean verifyToleranceLevel() {
        double sum = 0.0;
        for (int line = 0; line < this.nbCluster; line++) {
            double[] matrixUCol = new double[this.points.size()];
            double[] oldMatrixUCol = new double[this.points.size()];

            for (int col = 0; col < this.points.size(); col++) {
                matrixUCol[col] = this.matrixU.valueAt(line, col);
                oldMatrixUCol[col] = this.oldMatrixU.valueAt(line, col);
            }

            sum += euclideanDistance(matrixUCol, oldMatrixUCol);
        }
        this.actualError = sum;
        return (sum > this.toleranceLevel);
    }

    /**
     * Initialize the U matrix
     */
    private void initMatrixU() {
        Builder builder = Matrix.builder().dimensions(this.nbCluster, this.points.size());
        for (int col = 0; col < this.points.size(); col++) {
            double[] values = getNRandomValues(this.nbCluster);
            for (int line = 0; line < this.nbCluster; line++) {
                builder.valueAt(line, col, values[line]);
            }
        }
        this.matrixU = builder.build();
       // System.out.println(this.matrixU.toString());
    }

    /**
     * Initialize the U matrix
     */
    private void initMatrixU(double[][] matrix) {
        Builder builder = Matrix.builder().dimensions(this.nbCluster, this.points.size());
        for (int line = 0; line < this.nbCluster; line++) {
            for (int col = 0; col < this.points.size(); col++) {
                builder.valueAt(line, col, matrix[line][col]);
            }
        }
        this.matrixU = builder.build();
    }

    /**
     * @param nbCluster
     * @return
     */
    private double[] getNRandomValues(int nbCluster) {
        Random random = new Random();
        double[] values = new double[nbCluster];
        double min = 0.0, sum = 0.0;
        for (int i = 0; i < nbCluster - 1; i++) {
            double nextDouble = min + (random.nextDouble() * (1.0 - min));
            sum += nextDouble - min;
            values[i] = nextDouble - min;
            min = nextDouble;
        }
        values[nbCluster - 1] = 1.0 - sum;
        return values;
    }

    /**
     * Compute euclidian distance
     *
     * @param a
     * @param b
     * @return Euclidian distance between two points
     */
    private static double euclideanDistance(double[] a, double[] b) {
        //System.out.println(a.length + " vs " + b.length);
        if (a.length != b.length) {
            throw new IllegalArgumentException("The dimensions have to be equal!");
        }

        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }

        return Math.sqrt(sum);
    }

    /**
     * @return the matrixU
     */
    public Matrix getMatrixU() {
        return matrixU;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FCM Algorithm [matrixU=" + matrixU + ", features=" + Arrays.toString(points.toArray())
                + ", centroids=" + Arrays.toString(centroids.toArray()) + "]";
    }

    /**
     * @return
     */
    public String toStringCentroids() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        for (Point array : this.centroids) {
            buffer.append(Arrays.toString(array.v));
        }
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * @return the centroids
     */
    public List<Point> getCentroids() {
        return centroids;
    }

    /**
     * @param args
     */
    public static void main_1(String[] args) {
        double[][] input = new double[][]{
            {1.0, 3.0},
            {1.5, 3.2},
            {1.3, 2.8},
            {3.0, 1.0}
        };

        input = new double[][]{
            {1.0, 8.0, 1.5},
            {2.0, 3.5, 1.6},
            {4.0, 0.1, 1.7},
            {1.0, 6.0, 2.5},
            {1.0, 3.5, 0.6},
            {1.0, 12.1, 3.7},
            {6.0, 0.0, 2.5},
            {2.0, 0.5, 0.6},
            {0.5, 0.1, 3.7},
            {8.0, 0.002, 1.5},
            {8.0, 0.001, 0.6},
            {8.02, 0.009, 1.2}
        };

        List<double[]> inputList = new ArrayList<>();
        for (double[] ds : input) {
            inputList.add(ds);
        }

        FuzzyCMeansAlgorithm fcm = new FuzzyCMeansAlgorithm(4, 1.25, Point.RandomPoint0(), 0.001, null);
        fcm.run();
        System.out.println("Centroids: " + fcm.toStringCentroids());
        for (int i = 0; i < fcm.getFeaturesSize(); i++) {
            int indexOfMax = 0;
            double max = Double.MIN_VALUE;
            for (int j = 0; j < fcm.getNbCluster(); j++) {
                if (max < fcm.getMatrixU().valueAt(j, i)) {
                    indexOfMax = j;
                    max = fcm.getMatrixU().valueAt(j, i);
                }
            }
            fcm.points.get(i).classe = indexOfMax;
        }
        for (int i = 0; i < inputList.size(); i++) {
            System.out.print(fcm.points.get(i) + "\t");
            System.out.println(fcm.points.get(i).classe);
        }
    }

    public ArrayList<ArrayList<Object>> toResult() {
        // tableau data
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

            Point center = centroids.get(points.get(i).classe);

            //  System.out.println("Centre de  i =" + i + " " + center);
            resulta.add(center.pointtoDouble());
        }
        return resulta;
    }

    public int getNbCluster() {
        return nbCluster;
    }

    public int getFeaturesSize() {
        return points.size();
    }

    /**
     * @return the finish
     */
    public boolean isFinish() {
        return finish;
    }

    /**
     * @return the iteration
     */
    public int getIteration() {
        return iteration;
    }

    /**
     * @return the actualError
     */
    public double getActualError() {
        return actualError;
    }
}
