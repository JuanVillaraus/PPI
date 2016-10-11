/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author juan
 */
public class PPI extends JComponent implements ActionListener {

    int limX, limY;     //define los limites de la ventana
    int rX = 1;         //relativo en Y, para graficar la aguja
    int rY = 1;         //relativo en X, para graficar la aguja
    int cuadrante = 3;  //define en que cuadrante se encuentra la aguja
    int inc = 0;        //incremento del angulo, para la graficacion de la aguja 

    public static void main(String[] args) {
        JFrame w = new JFrame("PPI by SIVISO");
        PPI p = new PPI();
        w.add(p);
        w.pack();
        w.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        w.setLocationRelativeTo(null);
        w.setVisible(true);

        Timer t = new Timer(100, p);
        t.start();
    }

    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }

    @Override
    protected void paintComponent(Graphics g) {
        limX = getSize().width;
        limY = getSize().height;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, limX, limY);

        g.setColor(Color.GREEN);
        g.drawOval(1, 1, limX - 2, limY - 2);
        g.drawOval(limX * 2 / 6, limY * 2 / 6, limX * 1 / 3, limY * 1 / 3);
        g.drawOval(limX * 1 / 6, limY * 1 / 6, limX * 2 / 3, limY * 2 / 3);

        g.setColor(Color.GREEN);
        g.drawLine(limX / 2, limY / 2, rX, rY);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        inc++;
        if (inc > 360) {
            inc = 0;
        }
        rX = (int) (Math.cos(inc * Math.PI / 30 - Math.PI / 2) * (limX / 2) + (limX / 2));
        rY = (int) (Math.sin(inc * Math.PI / 30 - Math.PI / 2) * (limY / 2) + (limY / 2));
        repaint();
    }

}
