/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppi;

import java.awt.*;
import java.io.*;
import java.util.Properties;
import javax.swing.*;

/**
 *
 * @author siviso
 */
public class PPI extends JComponent {

    int inicioCascadaX = 75;
    int inicioCascadaY = 130;
    int sizeCanalX = 0;
    int sizeCanalY;
    int xi, yi, c;
    String infor;
    //int ml[];
    int gn = 0;
    //String ch = "";
    String info;

    public static void main(String[] args) {
        JFrame window = new JFrame("PPI by SIVISO");
        PPI pi = new PPI();
        window.add(pi);
        window.pack();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        window.setAlwaysOnTop(true);
        window.setFocusable(true);
        Properties prop = new Properties();
        InputStream input = null;
        int posicionX = 0;
        int posicionY = 0;
        try {
            input = new FileInputStream("config.properties");
            //load a properties file
            prop.load(input);
            //get the propperty value and print it out
            posicionX = Integer.parseInt(prop.getProperty("posicionX"));
            posicionY = Integer.parseInt(prop.getProperty("posicionY"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        window.setLocation(posicionX, posicionY);
        comInterfaz c = new comInterfaz();
        c.run(window);
    }

    @Override
    public Dimension getPreferredSize() {
        int dimensionX = 100;
        int dimensionY = 100;
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");
            //load a properties file
            prop.load(input);
            //get the propperty value and print it out
            dimensionX = Integer.parseInt(prop.getProperty("dimensionX"));
            dimensionY = Integer.parseInt(prop.getProperty("dimensionY"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new Dimension(dimensionX, dimensionY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        gn++;
        //System.out.println("paint component ciclo numero: " + gn);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getSize().width, getSize().height);

        archivo a = new archivo();
        String DIR = "resource/ppiData.txt";   //variable estatica que guarda el nombre del archivo donde se guardara la informacion recivida para desplegarse
        int n = 0;  //variable de control int que guarda el numero del color a desplegar
        yi = inicioCascadaY;     //variable de control grafico en Y que guarda la acumulacion del incremento para la graficacion
        xi = inicioCascadaX;     //variable de control grafico en Y que guarda la acumulacion del incremento para la graficacion
        String box = ""; //variable que guarda de char en char hasta llegar al tope asignado para proceder a convertirlo a int
        int r = 0;
        int p = 50;
        c = 0;
        int rX, rY, rX2, rY2, angulo, anguloFin;

        int colorUp = Integer.parseInt(a.leerTxtLine("resource/colorUp.txt"));
        int colorDw = Integer.parseInt(a.leerTxtLine("resource/colorDw.txt"));

        int limWinX = getSize().width;
        int limWinY = getSize().height;

        g.setColor(Color.GREEN);
        for (int x = 0; x < 360; x++) {
            rX = (int) (Math.cos(x * Math.PI / 180 - Math.PI) * 260 + (limWinX / 2));
            rY = (int) (Math.sin(x * Math.PI / 180 - Math.PI) * 260 + (limWinY / 2));
            g.drawLine(limWinX / 2, limWinY / 2, rX, rY);
        }
        for (int x = 90; x < 450; x += 15) {
            rX = (int) (Math.cos(x * Math.PI / 180 - Math.PI) * 270 + (limWinX / 2));
            rY = (int) (Math.sin(x * Math.PI / 180 - Math.PI) * 270 + (limWinY / 2));
            g.drawLine(limWinX / 2, limWinY / 2, rX, rY);
            g.drawString((x - 90) + "°", (int) (Math.cos(x * Math.PI / 180 - Math.PI) * 290 + (limWinX / 2)) - 10, (int) (Math.sin(x * Math.PI / 180 - Math.PI) * 290 + (limWinY / 2)) + 5);
        }
        g.setColor(Color.BLACK);
        p = 251;
        g.fillOval((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2);
        /*for (int r = 0; r < sec; r++) {
            for (int p = 50; p <= 250; p++) {
                //g.setColor(new Color(100, 255-p, (255 / sec) * r));
                /*rX = (int) (Math.cos(r * Math.PI / 40 - Math.PI / 2) * (p) + (limWinX / 2));
                rY = (int) (Math.sin(r * Math.PI / 40 - Math.PI / 2) * (p) + (limWinY / 2));
                rX2 = (int) (Math.cos((r+1) * Math.PI / 40 - Math.PI / 2) * (p) + (limWinX / 2));
                rY2 = (int) (Math.sin((r+1) * Math.PI / 40 - Math.PI / 2) * (p) + (limWinY / 2));
                //g.fillOval(rX-1, rY-1, 2, 2);
                //g.drawLine(rX, rY, rX2, rY2);
                //g.drawArc( (limWinX/2)-p, (limWinY/2) -p, (limWinX/2)+p, (limWinY/2) +p, 360/ (80*r), 360/ (80* (r+1) ) );
                g.setColor(new Color(0,0,0));
                g.drawArc((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2, ((360 / sec) * r) + 90, 360 / sec);
            }
        }*/

        //g.setColor(Color.WHITE);
        //g.drawArc(10, 10, limWinX - 10, limWinY - 10, 0 + 90, 360 / sec);
        //Graphics2D g2d = (Graphics2D) g;
        //g2d.
        /*g.drawLine(inicioCascadaX - 5, 1, inicioCascadaX - 5, inicioCascadaY - 30);
        g.drawLine(inicioCascadaX - 5, inicioCascadaY - 30, getSize().width, inicioCascadaY - 30);
        g.drawLine(inicioCascadaX - 5, inicioCascadaY, inicioCascadaX - 5, getSize().height - 20);
        for (int i = 0; i < 7; i++) {
            g.drawLine(inicioCascadaX + (((getSize().width - inicioCascadaX) / 6) * i), inicioCascadaY - 30, inicioCascadaX + (((getSize().width - inicioCascadaX) / 6) * i), inicioCascadaY - 25);
            if (i != 6) {
                g.drawString((i * 30) + "°", inicioCascadaX + (((getSize().width - inicioCascadaX) / 6) * i), inicioCascadaY - 10);
            } else {
                g.drawString((i * 30) + "°", getSize().width - 30, inicioCascadaY - 10);
            }
        }*/
        int sec = 80;
        r = 90;
        p = 250;
        float angI = ((360 / sec) * r) + 90;
        System.out.println("angI =" + angI);
        int angF = (360 / sec);
        int inc;
        //int angI = ((360 / sec) * r) + 90;
        //int angF = -(360 / sec);
        int limDir = a.getLim(DIR);
        info = a.leerTxtLineReverse(DIR, limDir);
        //info = a.leerTxtLine(DIR);
        char[] charArray = info.toCharArray();
        for (char temp : charArray) {
            if (!(temp == ',') && !(temp == ';')) {
                box += "" + temp;
            } else if (temp == ',' || temp == ';') {
                n = Integer.parseInt(box);
                if (n >= 0 && n <= 255) {
                    if (n < colorDw) {
                        g.setColor(Color.BLACK);
                    } else if (n > colorUp) {
                        g.setColor(Color.GREEN);
                    } else {
                        //g.setColor(new Color(0, (n - colorDw) * 255 / (colorUp - colorDw), 0));
                        g.setColor(new Color(0, n, 0));
                    }
                    if (angI % 9 == 0) {
                        inc = 1;
                    } else {
                        inc = 0;
                    }
                    g.drawArc((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2, (int)angI, -angF-inc);
                    p++;
                    g.drawArc((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2, (int)angI, -angF-inc);
                    p--;
                    //g.fillArc((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2, (int) angI, -angF - inc);
                    angI -= 4.5;
                    box = "";
                    c++;
                } else {
                    System.out.println("Error #??: el valor a desplegar esta fuera de rango");
                }
            } else {
                System.out.println("Error #??: el valor a desplegar no se reconoce");
            }
            if (temp == ';') {
                angI = ((360 / sec) * r) + 90;
                p -= 2;
                /*if ((t % 10) == 0) {
                    g.setColor(Color.WHITE);
                    g.drawLine(inicioCascadaX - 10, yi, inicioCascadaX - 05, yi);
                    g.drawString(topWord + "", 5, yi + 3);
                }*/
            }
        }
        /*xi = (limX / 2) + inicioCascadaX;
        g.setColor(new Color(0, 150, 0));
        for (int i = 0; i < 10; i++) {
            g.drawLine(xi, 95 - (topLine[i] * 90 / 255), xi + limX, 95 - (topLine[i + 1] * 90 / 255));
            xi += limX;
        }*/

        g.setColor(Color.GREEN);
        p = 251;
        g.drawOval((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2);
        g.setColor(new Color(100, 100, 100));
        p = 49;
        g.drawOval((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2);
        for (int x = 100; x < 250; x += 50) {
            g.drawOval((limWinX / 2) - x, (limWinY / 2) - x, x * 2, x * 2);
        }
        /*g.drawLine((limWinX / 2) - 252, (limWinY / 2), (limWinX / 2) + 252, (limWinY / 2));
        g.drawLine((limWinX / 2), (limWinY / 2) - 252, (limWinX / 2), (limWinY / 2) + 252);
        g.setColor(Color.BLACK);
        p = 48;
        g.fillOval((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2);*/
        g.setColor(Color.BLACK);
        p = 50;
        g.fillOval((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2);
    }
}
