/*
 * JAVA Neural Networks (https://bitbucket.org/zdenekdrahos/java-neural-networks)
 * @license New BSD License
 * @author Zdenek Drahos
 */
package kohonenclustering;

import util.Point;


public class MinSearch {

    private int indexMinimum;
    private double valueMinimum;

    public void findClosestWeight(Double[][] weights, Point data) {
        int groupsCount = weights.length;
        double[] differences = new double[groupsCount];
        for (int groupIndex = 0; groupIndex < groupsCount; groupIndex++) {
            differences[groupIndex] = getError(weights[groupIndex], data);
        }
        findExtrems(differences);
    }

    public void findExtrems(double[] data) {
        valueMinimum = Double.MAX_VALUE;
        indexMinimum = -1;
        for (int i = 0; i < data.length; i++) {
            if (data[i] <= valueMinimum) {
                valueMinimum = data[i];
                indexMinimum = i;
            }
        }
    }

    public int getIndexOfMin() {
        return indexMinimum;
    }

    public double getValueOfMin() {
        return valueMinimum;
    }

    /**
     * méthode des moindres carrés
     * @param expected
     * @param real
     * @return
     */
    public static double getError(Double[] expected, Point real) {
        double error = 0;
        for (int i = 0; i < expected.length; i++) {
            error += Math.pow(expected[i] - real.v[i], 2);
        }
        return error;
    }

}
