/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import javax.swing.JPanel;

/**
 *
 * @author vector
 */
public class Pl extends JPanel {

    HashMap<En, ArrayList<En>> Re = new HashMap<>();

    public Pl() {
        setLayout(null);
        setBackground(Color.white);
        setSize(600, 600);
        EnMouse enm = new EnMouse();
        this.addMouseListener(enm);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D s = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        s.setRenderingHints(rh);
        s.drawImage(App.icon_panel, getWidth()-121, getHeight()-43, this);
        s.setColor(new Color(150, 150, 150));
        s.setStroke(new BasicStroke(4));
        Set<En> r = Re.keySet();
        for (En en : r) {
            int xf = en.getBounds().x;
            int yf = en.getBounds().y;
            ArrayList<En> rs = Re.get(en);
            for (En en1 : rs) {
                int xt = en1.getX();
                int yt = en1.getY();
                CubicCurve2D cb = new CubicCurve2D.Double(xf + En.w - 10, yf + (En.h / 2), xf + En.w + 60, yf + (En.h / 2),
                        xt - 60, yt + (En.h / 2), xt + 10, yt + (En.h / 2));
//                CubicCurve2D cb = new CubicCurve2D.Double(xf, yf, xf + 120, yf,
//                        xt, yt, xt + 120, yt);
                s.draw(cb);
            }
        }
    }

    class EnMouse extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (App.etat.equals("Data")) {
                En e1 = new En(Pl.this, "Data");
                e1.setBounds(e.getX() - 60, e.getY() - 60, En.w, En.h);
                Re.put(e1, new ArrayList<En>());
                Pl.this.add(e1);
                Pl.this.repaint();
                System.out.println("Add New Data");
            }
            if (App.etat.equals("Method")) {
                En e1 = new En(Pl.this, "Method");
                e1.setBounds(e.getX() - 60, e.getY() - 60, En.w, En.h);
                Re.put(e1, new ArrayList<En>());
                Pl.this.add(e1);
                Pl.this.repaint();
                System.out.println("Add New Methode");
            }
            if (App.etat.equals("Visualiser")) {
                En e1 = new En(Pl.this, "Visualiser");
                
                e1.setBounds(e.getX() - 60, e.getY() - 60, En.w, En.h);
                Re.put(e1, new ArrayList<En>());
                Pl.this.add(e1);
                Pl.this.repaint();
                System.out.println("Add New Visualiser");
            }
            if (App.etat.equals("Plot")) {
                En e1 = new En(Pl.this, "Plot");
                
                e1.setBounds(e.getX() - 60, e.getY() - 60, En.w, En.h);
                Re.put(e1, new ArrayList<En>());
                Pl.this.add(e1);
                Pl.this.repaint();
                System.out.println("Add New Plot");
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

    }

}
