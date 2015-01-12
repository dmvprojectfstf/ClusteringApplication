/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author vector
 */
public class Result {

    String type;
    String method;
    Data data;
    BufferedImage im;
    int nbCluster=1;
    HashMap<String, Object> properties;

    public Result() {
        type = "No";
        method="No";
        properties = new HashMap<>();
        im = null;
        data = null;
    }

    public void PrintProperties() {

        Set listKeys = properties.keySet();
        Iterator iterateur = listKeys.iterator();
        System.out.print("#### Propert ####");
        while (iterateur.hasNext()) {
            String key = (String) iterateur.next();
            Object e = properties.get(key);
            System.out.print(key+"\t");
            if (e instanceof Integer) {
                System.out.println(e);
            }
            if(e instanceof Double){
                System.out.println(e);
            }
            System.out.print("#### ####### ####");
        }
    }
}
