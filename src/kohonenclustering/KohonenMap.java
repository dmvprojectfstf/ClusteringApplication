/*
 * JAVA Neural Networks (https://bitbucket.org/zdenekdrahos/java-neural-networks)
 * @license New BSD License
 * @author Zdenek Drahos
 */
package kohonenclustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import util.Point;

public class KohonenMap {

    //private Double[][] data;
    private ArrayList<Point> points;

    private List<List<Point>> clusters;
    public KohonenTraining training = new KohonenTraining();
    private KohonenClassification classification = new KohonenClassification();
    private int groupsCount, iterations;
    private double learningRate;

    public KohonenMap() {
    }
    
    public int clusterCount(){
        return groupsCount;
    }

    public KohonenMap(ArrayList<Point> data, int groupsCount, double learningRate, int iterations) {
        this.points = data;
        this.learningRate = learningRate;
        this.groupsCount = groupsCount;
        this.iterations = iterations;

    }

    public void setData(ArrayList<Point> data) {
        this.points = data;
    }

    public int[] getClasses() {
        return classification.getClasses();
    }

    public void train() {
        if (points != null) {
            training.train(points, learningRate, groupsCount, iterations);
        } else {
            throw new NullPointerException("Data not set");
        }
    }

    public int getGroupsCount() {
        return training.getGroupsCount();
    }

    public Double[][] getWeights() {
        return training.getWeights();
    }

    public void classifyData() {
        clusters = classification.classify(training.getWeights(), points);
    }

//    public void sortClassifiedData() {
//        for (int group = 0; group < groups.size(); group++) {
//            Collections.sort(groups.get(group), comparer());
//        }
//    }
    public List<List<Point>> getClassifiedData() {
        return clusters;
    }

//    private Comparator<Point> comparer() {
//        return new Comparator<Point>() {
//
//            @Override
//            public int compare(Point a, Point b) {
//                return a[0].compareTo(b[0]);
//            }
//
//        };
//    }
    public void run() {
        train();
        classifyData();
    }

    public ArrayList<ArrayList<Object>> toResult() {
        ArrayList<ArrayList<Object>> resulta=new ArrayList<>();
       // System.out.println("#########################");
        for (int i = 0; i < points.size(); i++) {
        //    System.out.println(points.get(i) + "    " + classes[i]);
            resulta.add(points.get(i).pointtoDouble());
            resulta.get(i).add((int)points.get(i).classe);
        }
        return resulta;
    }
    public ArrayList<ArrayList<Object>> toResultImage() {
        ArrayList<ArrayList<Object>> resulta=new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Point center=new Point(training.weights[points.get(i).classe]);
          //  System.out.println("Centre de  i =" +i+" "+center);
            resulta.add(center.pointtoDouble());
        }
        return resulta;
    }
    public static void main_1(String[] args) {
        Double[][] inputData = new Double[][]{
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
        double learningRate = 0.75;
        int groupsCount = 4;
        int iterationsCount = 200;

        KohonenMap som = new KohonenMap(Point.RandomPoint0(), groupsCount, learningRate, iterationsCount);
        //som.setData(Point.RandomPoint0());
        som.run();
//        kohonenMap.sortClassifiedData();
        for (int i = 0; i < som.points.size(); i++) {
            System.out.println(som.points.get(i) + "\t class = " + som.points.get(i).classe);
        }
        System.out.println("#####END#######");
//        List<List<Double[]>> result = kohonenMap.getClassifiedData();
//        int i=0;
//        for (List<Double[]> list : result) {
//            System.out.println("#### class "+i+" ####");
//            for (Double[] doubles : list) {
//                for (Double double1 : doubles) {
//                    System.out.print(double1+"\t");
//                }
//                System.out.println("");
//            }
//            //System.out.println(list.get(600));
//            i++;
//        }
    }

}
