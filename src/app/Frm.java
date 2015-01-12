/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;

/**
 *
 * @author vector
 */
public class Frm extends JPanel {

    JPanel left = new JPanel();
    JPanel center = new JPanel();

    public Frm() {
        left.setPreferredSize(new Dimension(110, 200));
        center.setPreferredSize(new Dimension(200, 200));
        this.setLayout(new BorderLayout());
        left.setBackground(Color.white);
        center.setBackground(Color.blue);

        this.add(left, BorderLayout.WEST);
        this.add(center, BorderLayout.CENTER);
    }

}
