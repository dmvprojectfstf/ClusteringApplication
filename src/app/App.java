/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author vector
 */
public class App {

    JFrame f = new JFrame("Application");
    Pl pl = new Pl();
    static ImageIcon icon = null;
    static Image icon_panel = null;

    public static String etat = "No";
    public static En en0 = null;
    public static En en1 = null;
    Color scol = new Color(163, 73, 164), nscol = new Color(255, 127, 39);
    Color vcol = new Color(34, 177, 76);
//    Color pcol = new Color(50, 200, 95);
    Color pcol = new Color(66 , 191,235);
    Color shcol = new Color(150, 150, 150);
    Color sscol = Color.red;

    public App() {
        icon = new ImageIcon(getClass().getResource("icon.png"));
        icon_panel = new ImageIcon(getClass().getResource("logo_panel.PNG")).getImage();
        f.setSize(790, 570);
        f.setLocation(200, 200);
        App.positionFentre(f);
//
//        En e1 = new En(pl, "Data");
//        En e2 = new En(pl, "Method");
//        En e3 = new En(pl, "Data");
//        En e4 = new En(pl, "Method");
//        
//        e1.id=1;
//        e2.id=2;
//        e3.id=3;
//        e4.id=4;
//        pl.Re.put(e1, new ArrayList<En>());
//        pl.Re.put(e2, new ArrayList<En>());
//        pl.Re.put(e3, new ArrayList<En>());
//        pl.Re.put(e4, new ArrayList<En>());
//        pl.Re.get(e1).add(e2);
//        pl.Re.get(e1).add(e4);
//        pl.Re.get(e2).add(e3);
//        pl.Re.get(e4).add(e3);
//        pl.add(e1);
//        pl.add(e2);
//        pl.add(e3);
//        pl.add(e4);
        // //***
        Frm frm = new Frm();
        frm.add(pl, BorderLayout.CENTER);

        JLabel lab = new JLabel("   Menu  ");
        lab.setForeground(shcol);
        lab.setFont(new Font("Calibri", Font.BOLD, 22));

        JButton bdata = new JButton("Data");
        bdata = createSimpleButton("Data", scol);
        JButton bmethod = new JButton("Method");
        bmethod = createSimpleButton("Method", nscol);
        JButton bvisualiser = new JButton("Visualiser");
        bvisualiser = createSimpleButton("Visualiser", vcol);
        JButton brelation = new JButton("Relation");
        brelation = createSimpleButton("Relation", shcol);
        JButton bsupprimer = new JButton("Delete");
        JButton bplot = new JButton("Plot");
        bsupprimer = createSimpleButton("Delete", sscol);
        bplot = createSimpleButton("Plot", pcol);

        buttList bt = new buttList();
        bdata.addActionListener(bt);
        bmethod.addActionListener(bt);
        brelation.addActionListener(bt);
        bvisualiser.addActionListener(bt);
        bsupprimer.addActionListener(bt);
        bplot.addActionListener(bt);

        lab.setPreferredSize(bvisualiser.getPreferredSize());
        bdata.setPreferredSize(bvisualiser.getPreferredSize());
        bmethod.setPreferredSize(bvisualiser.getPreferredSize());
        brelation.setPreferredSize(bvisualiser.getPreferredSize());
        bsupprimer.setPreferredSize(bvisualiser.getPreferredSize());
        bvisualiser.setPreferredSize(bvisualiser.getPreferredSize());
        bplot.setPreferredSize(bvisualiser.getPreferredSize());

        frm.left.add(lab);
        frm.left.add(bdata);
        frm.left.add(bmethod);
        frm.left.add(bvisualiser);
        frm.left.add(brelation);
        frm.left.add(bplot);
        frm.left.add(new JLabel("     "));
        frm.left.add(bsupprimer);
        frm.left.setBorder(new LineBorder(new Color(180, 180, 180)));

        f.add(frm, BorderLayout.CENTER);
        // //***
        // f.add(pl, BorderLayout.CENTER);
        f.setVisible(true);

//        System.out.println(e1.id);
//        System.out.println(e2.id);
//        System.out.println(e3.id);
//        System.out.println(e4.id);
//        e3.getFathers();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class buttList implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton bs = (JButton) e.getSource();
            if (e.getActionCommand().equals("Data")) {
                etat = "Data";
            } else if (e.getActionCommand().equals("Method")) {
                etat = "Method";
            } else if (e.getActionCommand().equals("Relation")) {
                etat = "Relation";
            } else if (e.getActionCommand().equals("Visualiser")) {
                etat = "Visualiser";
            } else if (e.getActionCommand().equals("Delete")) {
                etat = "Delete";
            } else if (e.getActionCommand().equals("Plot")) {
                etat = "Plot";
            }
            System.out.println(e.getActionCommand());
        }

    }

    private static JButton createSimpleButton(String text, Color c) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(c);
        Border line = new LineBorder(c);
        Border margin = button.getBorder();
        Border compound = new CompoundBorder(line, margin);
        button.setBorder(compound);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        //button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static void positionFentre(JFrame jf) {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //jf.setSize(screenSize.width * 1 / 2, screenSize.height * 1 / 2);
        jf.setIconImage(App.icon.getImage());
        Dimension frameSize = jf.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        jf.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
    }
}
