/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author vector
 */
public class En extends JComponent {

    Pl pl;
    int id = 0;
    boolean clicked = false;
    volatile int screenX = 0;
    volatile int screenY = 0;
    volatile int myX = 0;
    volatile int myY = 0;
    Color scol = new Color(163, 73, 164), nscol = new Color(255, 127, 39);
    Color vcol = new Color(34, 177, 76);
//    Color pcol = new Color(50, 200, 95);
    Color pcol = new Color(66 , 191,235);
    Color shcol = new Color(80, 80, 80);
    static int w = 120;
    static int h = 120;

    static String FILE = "File";
    static String IMAGE = "Image";
    static String DATA = "Data";
    static String METHOD = "Method";
    static String PLOT = "Plot";
    static String VISUALISER = "Visualiser";
    static String VISUALISERTAB = "Visualiser";
    static String VISUALISERIM = "Visualiser";

    String name = "No Name";
    String type = "Data";
    BufferedImage im = null;
    public OpenFile opf;
    public Method met;
    public Visualiser vis;
    public Plot plot;
    public Data data;

    public Result result;

    //public Visualiser tab;
    public En(Pl pl) {
        this.pl = pl;
        //   opf = new OpenFile(this);
        setOpaque(false);
        setBounds(0, 0, w, h);
        addMouseListener(new EnMouse());
        addMouseMotionListener(new EnMotion());
    }

    public En(Pl pl, String name) {
        this(pl);
        this.name = name;
        this.type = name;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D s = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        s.setRenderingHints(rh);
        s.setColor(Color.WHITE);
        s.fillOval(10, 10, getWidth() - 20, getHeight() - 20);
//        if (clicked) {
//            s.setColor(scol);
//        } else {
//            s.setColor(nscol);
//        }
        if (type.equals(DATA) || type.equals(IMAGE)) {
            s.setColor(scol);
        } else if (type.equals(METHOD)) {
            s.setColor(nscol);
        } else if (type.equals(VISUALISER)) {
            s.setColor(vcol);
        } else if (type.equals(PLOT)) {
            s.setColor(pcol);
        }
        s.setStroke(new BasicStroke(4));
        s.drawOval(10, 10, getWidth() - 20, getHeight() - 20);
        s.setFont(new Font("Calibri", Font.PLAIN, 18));

        FontMetrics fm = s.getFontMetrics(s.getFont());
        java.awt.geom.Rectangle2D rect = fm.getStringBounds(name, s);

        int textHeight = (int) (rect.getHeight());
        int textWidth = (int) (rect.getWidth());
        int panelHeight = this.getHeight();
        int panelWidth = this.getWidth();

// Center text horizontally and vertically
        int x = (panelWidth - textWidth) / 2;
        int y = (panelHeight - textHeight) / 2 + fm.getAscent();
        s.drawString(name, x, y);
        if (type.equals(IMAGE)) {
            s.drawImage(im, 27, 27, 66, 66, this);
        }
    }

    public ArrayList<En> getFathers() {
        ArrayList<En> f = new ArrayList<>();
        Set listKeys = pl.Re.keySet();
        Iterator iterateur = listKeys.iterator();
        while (iterateur.hasNext()) {
            En key = (En) iterateur.next();
            ArrayList<En> e = pl.Re.get(key);
            if (e.contains(this)) {
                f.add(key);
                System.out.println(key.id);
            }
        }
        return f;
    }

    class EnMouse extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            clicked = !clicked;
            if (e.getClickCount() == 2) {
                System.out.println("Double click");
                if (En.this.type.equals(DATA) || En.this.type.equals(IMAGE)) {
                    System.out.println("Data");
                    if (opf == null) {
                        opf = new OpenFile(En.this);
                        opf.setIconImage(App.icon.getImage());
                    }
                    opf.setLocation(200, 200);
                    opf.setVisible(true);
                } else if (En.this.type.equals(METHOD)) {
                    System.out.println("method");
                    if (met == null) {
                        met = new Method(En.this);
                        met.setIconImage(App.icon.getImage());
                    }
                    if (En.this.getFathers().isEmpty()) {
                        met.jButton2.setEnabled(false);
                    } else {
                        met.jButton2.setEnabled(true);
                    }
                    met.setLocation(200, 200);
                    met.setVisible(true);
                } else if (En.this.type.equals(PLOT)) {
                    System.out.println("plot");
                    if (plot == null) {
                        plot = new Plot(En.this);
                        plot.setIconImage(App.icon.getImage());
                    }
                    if (!En.this.getFathers().isEmpty()) {
                        En fe = En.this.getFathers().get(0);
                        //plot.setF(fe);
                        plot.jComboBox1.removeAllItems();
                        plot.jComboBox2.removeAllItems();
                        if (fe.type.equals(METHOD) && fe.result != null) {
                            plot.data = fe.result.data;
                            System.out.println("Size : "+plot.data.lines.size());
                            plot.type = fe.type;
                            plot.getDifferentColors(fe.result.nbCluster);
                            for (int i = 0; i < fe.result.data.lines.get(0).size()-1; i++) {
                                plot.jComboBox1.addItem(i);
                                plot.jComboBox2.addItem(i);
                            }
                            plot.setTitle("Plot "+fe.name);
                        } else if (fe.type.equals(DATA) && fe.data != null) {
                            plot.data = fe.data;
                            plot.type = fe.type;
                            for (int i = 0; i < fe.data.lines.get(0).size(); i++) {
                                plot.jComboBox1.addItem(i);
                                plot.jComboBox2.addItem(i);
                            }
                            plot.setTitle("Plot "+fe.opf.chooser.getSelectedFile().getName());
                        }
                        // plot.paintPlot();
                    }
                    plot.repaint();
                    plot.pp.repaint();
                    plot.setLocation(200, 200);
                    plot.setVisible(true);
                } else if (En.this.type.equals(VISUALISER)) {
                    System.out.println("visualiser");

                    if (vis == null) {
                        vis = new Visualiser(En.this);
                        vis.setIconImage(App.icon.getImage());
                    }
                    En fe = En.this.getFathers().get(0);
                    System.out.println(fe.type);
                    //System.out.println(fe.data.lines.size());
                    if (fe.type.equals(IMAGE)) {
                        vis.jLabel1.setIcon(new ImageIcon(fe.im));
                        vis.pack();
                        vis.setTitle("Visualiser " + fe.opf.chooser.getSelectedFile().getName());
                    } else if (fe.type.equals(DATA)) {
                        OpenFile.printfile(fe.data);
                        Visualiser.MonModel mc = new Visualiser.MonModel(fe.data);
                        JTable j = new JTable(mc);
                        JScrollPane ss = new JScrollPane(j);
                        vis.jPanel1.removeAll();
                        vis.jPanel1.add(ss);
                        vis.pack();
                        vis.setTitle("Visualiser " + fe.opf.chooser.getSelectedFile().getName());
                    } else if (fe.type.equals(METHOD) && fe.result != null) {
                        if (fe.result.type.equals(DATA)) {
                            OpenFile.printfile(fe.result.data);
                            Visualiser.MonModel mc = new Visualiser.MonModel(fe.result.data);
                            JTable j = new JTable(mc);
                            JScrollPane ss = new JScrollPane(j);
                            vis.jPanel1.removeAll();
                            vis.jPanel1.add(ss);
                            vis.pack();
                            vis.setTitle("Visualiser " + fe.name);
                        } else if (fe.result.type.equals(IMAGE)) {
                            vis.jLabel1.setIcon(new ImageIcon(fe.result.im));
                            vis.setTitle("Visualiser " + fe.name);
                            vis.pack();
                        }
                        fe.result.PrintProperties();
                    }
                    vis.setLocation(200, 200);
                    vis.setVisible(true);
                }
                //  opf.setVisible(true);
                return;
            } else if (App.etat.equals("Relation")) {
                if (App.en0 != null) {
                    if (App.en0 != En.this) {
                        App.en1 = En.this;
                        pl.Re.get(App.en0).add(App.en1);
                        App.en0 = null;
                        App.en1 = null;
                        App.etat = "No";
                    } else {
                        System.out.println("Choos other En");
                    }
                } else {
                    App.en0 = En.this;
                }
            } else if (App.etat.equals("Delete")) {
                System.out.println("Delete ---");
                ArrayList<En> fe = En.this.getFathers();
                for (En en : fe) {
                    pl.Re.get(en).remove(En.this);
                }
                pl.Re.remove(En.this);
                pl.remove(En.this);
                pl.revalidate();
                pl.repaint();
            }

            System.out.println("Click");
            repaint();
            pl.repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            screenX = e.getXOnScreen();
            screenY = e.getYOnScreen();
            clicked = true;
            myX = getX();
            myY = getY();
        }
    }

    class EnMotion extends MouseMotionAdapter {

        @Override
        public void mouseDragged(MouseEvent e) {
            int deltaX = e.getXOnScreen() - screenX;
            int deltaY = e.getYOnScreen() - screenY;
            setLocation(myX + deltaX, myY + deltaY);
            pl.repaint();
        }
    }

}
