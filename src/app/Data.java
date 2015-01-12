/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author vector
 */
public class Data {

    ArrayList<String> names;
    ArrayList<ArrayList<Object>> lines;

    public Data() {
        names = new ArrayList<>();
        lines = new ArrayList<>();
    }

    static public Data readFile(File f, boolean title, String separator) {
        Data d = new Data();
        BufferedReader br = null;
        String line;
        String[] country = null;
        try {
            br = new BufferedReader(new FileReader(f));
//            if (title && (line = br.readLine()) != null) {
//                String[] country = line.split(separator);
//                for (String s : country) {
//                    d.names.add(s);
//                }
//            }
            while (true) {
                line = br.readLine();
                if (line == null || line.equals("")) {
                    break;
                }
                country = line.split(separator);
                //System.out.println(line);
                d.lines.add(new ArrayList<>());
                for (String s : country) {
                    d.lines.get(d.lines.size() - 1).add(new Double(s));
                }
                //System.out.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Size = " + (d.lines.size() - 1));
            System.out.println("Size = " + country[0] + country[0] + country[0]);
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return d;
    }

    public double getMin(int k) {
        double d = Double.MAX_VALUE;
        for (int i = 0; i < lines.size(); i++) {
            if ((Double) lines.get(i).get(k) < d) {
                d = (Double) lines.get(i).get(k);
            }
        }
        return d;
    }

    public double getMax(int k) {
        double d = Double.MIN_VALUE;
        for (int i = 0; i < lines.size(); i++) {
            if ((Double) lines.get(i).get(k) > d) {
                d = (Double) lines.get(i).get(k);
            }
        }
        return d;
    }

}
