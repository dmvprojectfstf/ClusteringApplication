/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;

/**
 *
 * @author vector
 */
public class PlotPanel extends JPanel {

    Plot p;
    Color[] clusterColor;

    public PlotPanel(Plot p) {
        this.p = p;
        setBackground(Color.white);
        setPreferredSize(p.jPanel1.getPreferredSize());
        //  System.out.println("Ssssssssssssss");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D s = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        s.setRenderingHints(rh);
        if (p.jComboBox1.getItemCount() != 0) {
            int vx = (Integer) p.jComboBox1.getSelectedItem();
            int vy = (Integer) p.jComboBox2.getSelectedItem();
            g.setColor(Color.black);
            double maxX = p.data.getMax(vx);
            double minX = p.data.getMin(vx);
            double maxY = p.data.getMax(vy);
            double minY = p.data.getMin(vy);
            Ellipse2D.Double shape;
            int cc = p.data.lines.get(0).size() - 1;
            int cluster = 0;
            for (int i = 0; i < p.data.lines.size(); i++) {
                if (p.type.equals(En.METHOD)) {
                    cluster = (int) p.data.lines.get(i).get(cc);
                    if (cluster == -1) {
                        s.setColor(Color.black);
                    } else {
                        s.setColor(clusterColor[cluster]);
                    }
                }
                shape = new Ellipse2D.Double(
                        (int) ((((double) p.data.lines.get(i).get(vx)) - minX) * getWidth() / (maxX - minX)),
                        (int) ((((double) p.data.lines.get(i).get(vy)) - minY) * getHeight() / (maxY - minY)),
                        5,
                        5);
                s.fill(shape);

//                System.out.println(" x = " + (double) p.f.data.lines.get(i).get(vx)
//                        + " wx = " + ((((double) p.f.data.lines.get(i).get(vx)) - minX) * getWidth() / (maxX - minX)));
//                System.out.println(" y = " + (double) p.f.data.lines.get(i).get(vy)
//                        + " hy = " + ((((double) p.f.data.lines.get(i).get(vy)) - minY) * getHeight() / (maxY - minY)));
            }
//            System.out.println("vx " + vx + " vy " + vy + " -- h " + getHeight() + " w " + getWidth());
//            System.out.println(" max " + maxX + " " + maxY);
//            System.out.println(" min " + minX + " " + minY);
        }
    }
}
