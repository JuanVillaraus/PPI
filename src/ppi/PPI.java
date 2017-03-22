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
    String infor;
    String info;
    archivo a = new archivo();

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
        //a.escribirTxt(REF, "0");
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
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getSize().width, getSize().height);

        String DIR = "resource/ppiData.txt";   //variable estatica que guarda el nombre del archivo donde se guardara la informacion recivida para desplegarse

        int n = 0;  //variable de control int que guarda el numero del color a desplegar
        String box = ""; //variable que guarda de char en char hasta llegar al tope asignado para proceder a convertirlo a int
        int r = 0;
        int p = 50;
        int rX, rY;

        int colorUp = Integer.parseInt(a.leerTxtLine("resource/colorUp.txt"));
        int colorDw = Integer.parseInt(a.leerTxtLine("resource/colorDw.txt"));
        int refPPI = Integer.parseInt(a.leerTxtLine("resource/ppiRef.txt"));

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
        int sec = 80;
        int[] anillo = new int[sec];
        r = 90;
        p = 250;
        float angI = ((360 / sec) * r) + 90;
        int angF = (360 / sec);
        int inc;
        int down;
        info = a.leerTxtLineReverse(DIR, a.getLim(DIR));
        char[] charArray = info.toCharArray();
        for (char temp : charArray) {
            if (!(temp == ',') && !(temp == ';')) {
                box += "" + temp;
            } else if (temp == ',' || temp == ';') {
                anillo[n] = Integer.parseInt(box);
                n++;
                box = "";
            } else {
                System.out.println("Error #??: el valor a desplegar no se reconoce");
            }
            if (temp == ';') {
                down = 255;
                for (int x = 0; x < anillo.length; x++) {
                    if (down > anillo[x]) {
                        down = anillo[x];
                    }
                }
                for (int x = 0; x < anillo.length; x++) {
                    anillo[x] -= down;
                }
                for (int x = 0; x < anillo.length; x++) {
                    if (anillo[x] >= 0 && anillo[x] <= 255) {
                        if (anillo[x] < colorDw) {
                            g.setColor(Color.BLACK);
                        } else if (anillo[x] > colorUp) {
                            g.setColor(Color.GREEN);
                        } else {
                            g.setColor(new Color(0, (anillo[x] - colorDw) * 255 / (colorUp - colorDw), 0));
                        }
                        if (angI % 9 == 0) {
                            inc = 1;
                        } else {
                            inc = 0;
                        }
                        /*g.drawArc((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2, (int)angI, -angF-inc);  //Esta parte comentada es mucho mas rapida y usa menos recursos 
                        p++;                                                                                    //que el codigo posterior pero la graficación tiene errores de
                        g.drawArc((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2, (int)angI, -angF-inc);    //puntos negros, usar este codigo en caso de querer agilizar el
                        p--;*/                                                                                  //proceso sacrificando la resolución
                        g.fillArc((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2, (int) angI, -angF - inc);
                        angI -= 4.5;
                    } else {
                        System.out.println("Error #??: el valor a desplegar esta fuera de rango");
                    }
                }
                angI = ((360 / sec) * r) + 90;
                p -= 2;
                n = 0;
            }
        }

        g.setColor(Color.GREEN);
        p = 251;
        g.drawOval((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2);
        g.setColor(new Color(100, 100, 100));
        p = 49;
        g.drawOval((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2);
        for (int x = 50; x < 250; x += 25) {
            g.drawOval((limWinX / 2) - x, (limWinY / 2) - x, x * 2, x * 2);
        }
        g.drawLine((limWinX / 2) - 252, (limWinY / 2), (limWinX / 2) + 252, (limWinY / 2));
        g.drawLine((limWinX / 2), (limWinY / 2) - 252, (limWinX / 2), (limWinY / 2) + 252);
        g.setColor(Color.BLACK);
        p = 50;
        g.fillOval((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2);
        g.setColor(Color.GRAY);
        g.drawOval((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2);

        g.setColor(Color.YELLOW);
        p = refPPI*2 + 50;
        g.drawOval((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2);
    }
}
