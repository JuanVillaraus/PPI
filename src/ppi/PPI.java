/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppi;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import static java.lang.Thread.sleep;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author siviso
 */
public class PPI extends JFrame implements MouseListener {

    public JLabel etiqueta;
    despliegue desp = new despliegue();

    public static void main(String[] args) {
        PPI pi = new PPI();
    }
    archivo a = new archivo();
    String dirSound = "resource/dirSound.txt";

    public PPI() {
        //pack();
        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //setLocationRelativeTo(null);
        setVisible(true);
        setAlwaysOnTop(true);
        //setFocusable(true);
        Properties prop = new Properties();
        InputStream input = null;
        int posicionX = 0;
        int posicionY = 0;
        int dimensionX = 100;
        int dimensionY = 100;
        String modelo = "";
        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
            posicionX = Integer.parseInt(prop.getProperty("posicionX"));
            posicionY = Integer.parseInt(prop.getProperty("posicionY"));
            dimensionX = Integer.parseInt(prop.getProperty("dimensionX"));
            dimensionY = Integer.parseInt(prop.getProperty("dimensionY"));
            modelo = prop.getProperty("modelo");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        switch (modelo) {
            case "SSF":
                setSize(dimensionX, dimensionY + 30);
                break;
            default:
                setSize(dimensionX, dimensionY);
                break;
        }
        setLocation(posicionX, posicionY);
        desp.addMouseListener(this);                                            //Se le asigna un escuchador al despligue
        this.add(desp);
        desp.run();
        repaint();
        /*pg = new PlayGraphics();
        pg.setWindow(this);*/
        comInterfaz c = new comInterfaz();
        c.run(this);
    }

    @Override
    public Dimension getPreferredSize() {
        int dimensionX = 100;
        int dimensionY = 100;
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
            dimensionX = Integer.parseInt(prop.getProperty("dimensionX"));
            dimensionY = Integer.parseInt(prop.getProperty("dimensionY"));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        return new Dimension(dimensionX, dimensionY);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(e.getX() + " " + e.getY());
        if (e.getY() < 620) {
            desp.setLineaAux(e.getX(), e.getY());
        } else if (e.getY() < 645) {
            desp.setRangoSound(e.getX());
        } else {
            desp.resetRangoSound();
            for (int i = 0; i < 11; i++) {
                desp.run();
                try {
                    sleep(400);
                } catch (InterruptedException ex) {
                    System.err.println("play: " + ex.getMessage());
                }
            }
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
