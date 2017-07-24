/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppi;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.*;

/**
 *
 * @author siviso
 */
class despliegue extends JComponent {

    int inicioCascadaX = 75;
    int inicioCascadaY = 130;
    int sizeCanalX = 0;
    int sizeCanalY;
    int rX, rY;
    String info;
    String modelo;
    archivo a = new archivo();
    private int lineaAX = 0;
    private int lineaAY = 0;
    private int longPPI;
    private int[][] waterfall;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getSize().width, getSize().height);

        String DIR = "resource/ppiData.txt";   //variable estatica que guarda el nombre del archivo donde se guardara la informacion recivida para desplegarse

        int n = 0;  //variable de control int que guarda el numero del color a desplegar
        String box = ""; //variable que guarda de char en char hasta llegar al tope asignado para proceder a convertirlo a int
        int r = 0;
        int p = 50;

        //Panel p = new Panel();
        //this.addMouseListener(this);
        Properties prop = new Properties();
        InputStream input = null;
        int colorUp = 255;
        int colorDw = 0;
        int refPPI = 0;
        int rumboB = 0;
        int rumboP = 0;

        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
            colorUp = Integer.parseInt(a.leerTxtLine("resource/colorUp.txt"));
            colorDw = Integer.parseInt(a.leerTxtLine("resource/colorDw.txt"));
            refPPI = Integer.parseInt(a.leerTxtLine("resource/ppiRef.txt"));
            rumboB = Integer.parseInt(a.leerTxtLine("resource/rumboB.txt"));
            rumboP = Integer.parseInt(a.leerTxtLine("resource/rumboP.txt"));
            this.longPPI = Integer.parseInt(prop.getProperty("longPPI"));
            modelo = prop.getProperty("modelo");
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

        int limWinX = getSize().width;
        int limWinY = getSize().height;

        waterfall = new int[100][longPPI];
        for (int x = 0; x < 100; x++) {                                                 //inicializa el waterfall en cero
            for (int y = 0; y < longPPI; y++) {
                waterfall[x][y] = 0;
            }
        }

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
        int[] anillo = new int[longPPI];
        r = 90;
        p = 250;
        float angI = ((360 / longPPI) * r) + 90;
        int angF = (360 / longPPI);
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
                        //g.drawArc((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2, (int)angI, -angF-inc);  //Esta parte comentada es mucho mas rapida y usa menos recursos 
                        //p++;                                                                                  //que el codigo posterior pero la graficación tiene errores de
                        //g.drawArc((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2, (int)angI, -angF-inc);  //puntos negros, usar este codigo en caso de querer agilizar el
                        //p--;                                                                                  //proceso sacrificando la resolución
                        g.fillArc((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2, (int) angI, -angF - inc);
                        angI -= 4.5;
                    } else {
                        System.out.println("Error #??: el valor a desplegar esta fuera de rango");
                    }
                }
                angI = ((360 / longPPI) * r) + 90;
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
        p = refPPI * 2 + 50;
        g.drawOval((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2);
        for (int x = 0; x < 8; x++) {
            g.drawString(x * 1.25 + "", (limWinX / 2) - 10, ((limWinY / 2) - x * 25) - 45);
        }
        g.drawString("La distancia entre anillos esta en milla nautica", 20, limWinY - 10);

        Graphics2D g2d = (Graphics2D) g;
        float[] dash = {5};
        g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f));
        rX = (int) (Math.cos((rumboB + 90) * Math.PI / 180 - Math.PI) * 278 + (limWinX / 2));
        rY = (int) (Math.sin((rumboB + 90) * Math.PI / 180 - Math.PI) * 278 + (limWinY / 2));
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(limWinX / 2, limWinY / 2, rX, rY);
        rX = (int) (Math.cos((rumboP + 90) * Math.PI / 180 - Math.PI) * 278 + (limWinX / 2));
        rY = (int) (Math.sin((rumboP + 90) * Math.PI / 180 - Math.PI) * 278 + (limWinY / 2));
        g.setColor(new Color(230, 95, 0));
        g.drawLine(limWinX / 2, limWinY / 2, rX, rY);

        lineaAux(g, rumboB);

        g2d.setStroke(new BasicStroke(5.7f));
        g.setColor(Color.GREEN);
        //g.drawString("N", (limWinX/2)-5, 20);
        g2d.drawString("N", (limWinX / 2) - 5, 20);

        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Rumbo del Buque " + rumboB + "°", limWinX - 150, limWinY - 60);
        rX = (int) (Math.cos((rumboB + 90) * Math.PI / 180 - Math.PI) * 40 + (limWinX / 2));
        rY = (int) (Math.sin((rumboB + 90) * Math.PI / 180 - Math.PI) * 40 + (limWinY / 2));
        rumbo(g, rX, rY);
        g.setColor(new Color(230, 95, 0));
        g.drawString("Rumbo del SSPV   " + rumboP + "°", limWinX - 150, limWinY - 40);
        rX = (int) (Math.cos((rumboP + 90) * Math.PI / 180 - Math.PI) * 25 + (limWinX / 2));
        rY = (int) (Math.sin((rumboP + 90) * Math.PI / 180 - Math.PI) * 25 + (limWinY / 2));
        rumbo(g, rX, rY);

    }

    public void rumbo(Graphics g, int rX, int rY) {
        double ang, angSep, tx, ty;
        int dist = 0;
        Point punto1 = null, punto2 = null;
        punto1 = new Point(getSize().width / 2, getSize().height / 2);
        punto2 = new Point(rX, rY);
        dist = 7;
        ty = -(punto1.y - punto2.y) * 1.0;
        tx = (punto1.x - punto2.x) * 1.0;
        ang = Math.atan(ty / tx);
        if (tx < 0) {
            ang += Math.PI;
        }

        Point p1 = new Point(), p2 = new Point(), punto = punto2;
        angSep = 25.0;

        p1.x = (int) (punto.x + dist * Math.cos(ang - Math.toRadians(angSep)));
        p1.y = (int) (punto.y - dist * Math.sin(ang - Math.toRadians(angSep)));
        p2.x = (int) (punto.x + dist * Math.cos(ang + Math.toRadians(angSep)));
        p2.y = (int) (punto.y - dist * Math.sin(ang + Math.toRadians(angSep)));

        Graphics2D g2D = (Graphics2D) g;
        //g.setColor(Color.BLUE);
        g2D.setStroke(new BasicStroke(3.7f));
        g.drawLine(punto1.x, punto1.y, punto2.x, punto2.y);
        g.drawLine(p1.x, p1.y, punto.x, punto.y);
        g.drawLine(p2.x, p2.y, punto.x, punto.y);
    }

    public void lineaAux(Graphics g, int rumboB) {
        if (lineaAX != 0 && lineaAY != 0) {
            g.setColor(Color.YELLOW);
            Point p = new Point(lineaAX - (getSize().width / 2), (getSize().height / 2) - lineaAY);
            double angSep = medirAngSep(p);
            rX = (int) (Math.cos((angSep + 90) * Math.PI / 180 - Math.PI) * 278 + (getSize().width / 2));
            rY = (int) (Math.sin((angSep + 90) * Math.PI / 180 - Math.PI) * 278 + (getSize().height / 2));
            g.drawLine(getSize().width / 2, getSize().height / 2, rX, rY);
            int dist = rumboB - (int) angSep;
            if (dist > 180) {
                dist -= 360;
            }
            if (dist < 0) {
                dist *= -1;
                g.drawString("a Estribor", getSize().width - 70, getSize().height - 5);
            } else {
                g.drawString("a Babor", getSize().width - 58, getSize().height - 5);
            }
            g.drawString("Blanco respecto al Buque " + dist + "°", getSize().width - 200, getSize().height - 20);
        }
    }

    public void setLineaAux(int x, int y) {
        this.lineaAX = x;
        this.lineaAY = y;
    }

    public double medirAngSep(Point p) {
        return Math.toDegrees(Math.atan2(p.x, p.y));
    }
    
    public void setWaterfall(int[][] waterfall) {
        this.waterfall = waterfall;
    }

    public void setInfo(String infoActual) {
        System.out.println("setInfo");
        info = "";
        int[] infoActualNum = new int[longPPI];
        int n = 0;
        char[] charArray = infoActual.toCharArray();
        for (char temp : charArray) {
            if (!(temp == ',') && !(temp == ';')) {
                info += temp;
            } else {
                try {
                    infoActualNum[n] = Integer.parseInt(info);

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                info = "";
                n++;
            }
        }
        //tiempoLocal++;
        //if (tiempoOper < tiempoLocal) {
        waterfall = getWaterfall();
        for (int x = waterfall.length - 1; x > 0; x--) {
            waterfall[x] = waterfall[x - 1];
        }
        waterfall[0] = infoActualNum;
        setWaterfall(waterfall);
        //}
        repaint();
    }
    
    public int[][] getWaterfall() {
        return this.waterfall;
    }
}
