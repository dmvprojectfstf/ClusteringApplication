/*
 * JAVA Neural Networks (https://bitbucket.org/zdenekdrahos/java-neural-networks)
 * @license New BSD License
 * @author Zdenek Drahos
 */

package kohonenclustering;

import java.util.ArrayList;
import java.util.List;
import util.Point;

public class KohonenClassification {

    private List<List<Point>> lists;
    private MinSearch minSearch = new MinSearch();
    private int[] Classes;
    
    public List<List<Point>> classify(Double[][] weights, ArrayList<Point> data) {        
        initLists(weights);
        Classes=new int[data.size()];
        classifyData(weights, data);
        return lists;
    }

    private void initLists(Double[][] weights) {
        lists = new ArrayList<List<Point>>();        
        for (int group = 0; group < weights.length; group++) {
            lists.add(0, new ArrayList<Point>());
        }
    }

    private void classifyData(Double[][] weights,ArrayList<Point> data) {
        int groupIndex;
        for (int i = 0; i < data.size(); i++) {
            groupIndex = classify(weights, data.get(i));
            lists.get(groupIndex).add(data.get(i));
            data.get(i).classe=groupIndex;
            Classes[i]=groupIndex;
        }
    }

    public int[] getClasses() {
        return Classes;
    }
    
    private int classify(Double[][] weights, Point data) {
        minSearch.findClosestWeight(weights, data);
        return minSearch.getIndexOfMin();
    }

}
