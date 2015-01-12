/*
 * JAVA Neural Networks (https://bitbucket.org/zdenekdrahos/java-neural-networks)
 * @license New BSD License
 * @author Zdenek Drahos
 */

package kohonenclustering;

import java.util.ArrayList;
import java.util.Random;
import util.Point;


public class KohonenTraining {

    private ArrayList<Point> input;
    private int inputCount, neuronsCount, groupsCount, neighbourhood;
    public int iterationsCount;
    private double learningRate;
    public  Double[][] weights;
    private MinSearch minSearch = new MinSearch();

    public Double[][] getWeights() {
        return weights;
    }

    public int getGroupsCount() {
        return groupsCount;
    }

    public void train(ArrayList<Point> inputData, double alfa, int groups, int iterationsCount) {
        input = inputData;
        groupsCount = groups;
        this.iterationsCount = iterationsCount;
        learningRate = alfa;
        neighbourhood = getOriginNeighbourhood();
        inputCount = inputData.size();
        neuronsCount = inputData.get(0).getLength();
        weights = getRandMatrix();
        train();
    }

    private void train() {
        for (int k = 1; k <= iterationsCount; k++) {
            for (int j = 0; j < inputCount; j++) {
                iteration(input.get(j));
            }
            updateLearningRate();
            updateNeighbourhood(iterationsCount, k);
        }
    }

    private void iteration(Point pattern) {
        minSearch.findClosestWeight(weights, pattern);
        int minIndex = Math.max(minSearch.getIndexOfMin() - neighbourhood, 0);
        int maxIndex = Math.min(minSearch.getIndexOfMin() + neighbourhood, groupsCount - 1);
        for (int groupIndex = minIndex; groupIndex <= maxIndex; groupIndex++) {
            for (int k = 0; k < pattern.getLength(); k++) {
                weights[groupIndex][k] = weights[groupIndex][k] + learningRate * (pattern.v[k] - weights[groupIndex][k]);
            }
        }
    }

    private void updateLearningRate() {
        learningRate = learningRate * 0.99;
    }

    private void updateNeighbourhood(int iterationsCount, int actualIteration) {
        if (neighbourhood > 0) {
            neighbourhood = Math.round((iterationsCount - actualIteration) / (float) (iterationsCount * neighbourhood));
        }
    }

    private int getOriginNeighbourhood() {
        return (int) Math.floor(groupsCount / 2.0);
    }

    private Double[][] getRandMatrix() {
        Random generator = new Random();
        Double[][] matrix = new Double[groupsCount][neuronsCount];
        for (int i = 0; i < groupsCount; i++) {
            for (int j = 0; j < neuronsCount; j++) {
                matrix[i][j] = generator.nextGaussian();
            }
        }
        return matrix;
    }
}
