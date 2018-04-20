/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppi;

import java.awt.*;
import java.io.*;
import java.text.DecimalFormat;
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
    int limWinX;
    int limWinY;
    String info;
    String modelo;
    archivo a = new archivo();
    private int lineaAX = 0;
    private int lineaAY = 0;
    int longPPI;
    private int[][] waterfall;
    int[] sound = new int[600000];
    int limSound = 0;
    int rangoSound = 0;
    int angMarc = 0;
    String targets;
    double[] target = new double[100];
    int[] disTarget = new int[100];
    int refPPI = 100;
    int escalaPPI = 1;
    //PPI SSPF 2.0-----------------------------------------------------------------
    double R = 30.0;          //Distancia entre boyas
    double ATta = 0.0;       //Diferencia de tiempo entre el pulso y el target
    double C = 1500;          //Velocidad del sonido
    double Beta = 0;          //Angulo entre SSPF y SSAF respecto al norte
    double Alfa = 0;          //Barrido angular del target respecto al norte
    double Ori = 0;        //Orientacion de la boya Pasiva respeto al norte
    private int[][] waterfallTargets;
    double distOrPas = 0.0;
    double distOrAct = 0.0;
    double angOrPas = 0.0;
    double angOrAct = 0.0;
    double angActPas = 0.0;
    DecimalFormat df = new DecimalFormat("#.00");
    private int dist = 0;
    private int paso = 0;
    private int angActBTR = 0;
    private int angTargetBTR = 0;
    private int disTargetBTR = 0;
    private int disDPaso = 0;

    public despliegue(JFrame window) {
        window.add(this);
        Properties prop = new Properties();
        InputStream input = null;

        int posicionX = 0;
        int posicionY = 0;
        int dimensionX = 0;
        int dimensionY = 0;

        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
            posicionX = Integer.parseInt(prop.getProperty("posicionX"));
            posicionY = Integer.parseInt(prop.getProperty("posicionY"));
            dimensionX = Integer.parseInt(prop.getProperty("dimensionX"));
            dimensionY = Integer.parseInt(prop.getProperty("dimensionY"));
            longPPI = Integer.parseInt(prop.getProperty("longPPI"));
            modelo = prop.getProperty("modelo");
            R = Double.parseDouble(prop.getProperty("distBoyaAct"));
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
        //dimensionX = 650;
        window.setSize(dimensionX, dimensionY);
        window.setLocation(posicionX, posicionY);
        waterfall = new int[100][longPPI];
        waterfallTargets = new int[1000][longPPI];
        for (int x = 0; x < 100; x++) {                                         //inicializa el waterfall en cero
            for (int y = 0; y < longPPI; y++) {
                waterfall[x][y] = 0;
                //waterfallTargets[x][y] = 0;
            }
        }
        for (int x = 0; x < 1000; x++) {                                         //inicializa el waterfall en cero
            for (int y = 0; y < longPPI; y++) {
                //waterfall[x][y] = 0;
                waterfallTargets[x][y] = 0;
            }
        }
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getSize().width, getSize().height);

        String DIR = "resource/ppiData.txt";   //variable estatica que guarda el nombre del archivo donde se guardara la informacion recivida para desplegarse

        int n = 0;  //variable de control int que guarda el numero del color a desplegar
        String box = ""; //variable que guarda de char en char hasta llegar al tope asignado para proceder a convertirlo a int
        int r = 0;
        int p = 0;

        //Panel p = new Panel();
        //this.addMouseListener(this);
        Properties prop = new Properties();
        InputStream input = null;
        int colorUp = 255;
        int colorDw = 0;
        int rumboB = 0;
        int rumboP = 0;

        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
            modelo = prop.getProperty("modelo");
            longPPI = Integer.parseInt(prop.getProperty("longPPI"));
            colorUp = Integer.parseInt(a.leerTxtLine("resource/colorUp.txt"));
            colorDw = Integer.parseInt(a.leerTxtLine("resource/colorDw.txt"));
            //refPPI = Integer.parseInt(a.leerTxtLine("resource/ppiRef.txt"));
            if ("SSPV".equals(modelo)) {
                rumboB = Integer.parseInt(a.leerTxtLine("resource/rumboB.txt"));
                rumboP = Integer.parseInt(a.leerTxtLine("resource/rumboP.txt"));
            }
            /*else {
                angMarc = Integer.parseInt(a.leerTxtLine("resource/angMarc.txt"));
            }*/
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
        if ("SSPV".equals(modelo)) {
            p = 50;
        }

        limWinX = getSize().width;
        limWinY = getSize().height;
        /*switch (modelo) {
            case "SSF":
                limWinY = getSize().height - 80;
                g.setColor(Color.GREEN);
                graficarAudio(g);
                break;
            default:
                limWinY = getSize().height;
                break;
        }*/

        p = 250;
        if ("SSF".equals(modelo)) {
            p += 50;
        }
        g.setColor(Color.GREEN);
        for (int x = 0; x < 360; x++) {
            rX = (int) (Math.cos(x * Math.PI / 180 - Math.PI) * (p + 10) + (limWinX / 2));
            rY = (int) (Math.sin(x * Math.PI / 180 - Math.PI) * (p + 10) + (limWinY / 2));
            g.drawLine(limWinX / 2, limWinY / 2, rX, rY);
        }

        if ("SSPV".equals(modelo)) {
            for (int x = 90; x < 450; x += 15) {
                rX = (int) (Math.cos(x * Math.PI / 180 - Math.PI) * (p + 20) + (limWinX / 2));
                rY = (int) (Math.sin(x * Math.PI / 180 - Math.PI) * (p + 20) + (limWinY / 2));
                g.drawLine(limWinX / 2, limWinY / 2, rX, rY);
                g.drawString((x - 90) + "°", (int) (Math.cos(x * Math.PI / 180 - Math.PI) * 290 + (limWinX / 2)) - 10, (int) (Math.sin(x * Math.PI / 180 - Math.PI) * 290 + (limWinY / 2)) + 5);
            }
        } else {
            for (int x = 105; x < 450; x += 15) {
                rX = (int) (Math.cos(x * Math.PI / 180 - Math.PI) * (p + 20) + (limWinX / 2));
                rY = (int) (Math.sin(x * Math.PI / 180 - Math.PI) * (p + 20) + (limWinY / 2));
                g.drawLine(limWinX / 2, limWinY / 2, rX, rY);
                g.drawString((x - 90) + "°", (int) (Math.cos(x * Math.PI / 180 - Math.PI) * 340 + (limWinX / 2)) - 10, (int) (Math.sin(x * Math.PI / 180 - Math.PI) * 340 + (limWinY / 2)) + 5);
            }
        }

        g.setColor(Color.BLACK);
        p = 251;
        if ("SSF".equals(modelo)) {
            p += 50;
        }
        g.fillOval((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2);
        int angF = (360 / longPPI);
        int inc;
        info = "";
        int angMarc = Integer.parseInt(a.leerTxtLine("resource/angMarc.txt"));

        char[] charArray = info.toCharArray();

        r = 90;
        p = 250;
        if ("SSF".equals(modelo)) {
            p += 50;
        }
        float angI;
        for (int x = waterfall.length - 1; x >= 0; x--) {
            angI = ((360 / longPPI) * r) + 90;
            for (int y = 0; y < waterfall[x].length; y++) {
                if (waterfall[x][y] >= 0 && waterfall[x][y] <= 255) {
                    if (angI % 9 == 0) {
                        inc = 1;
                    } else {
                        inc = 0;
                    }
                    if (waterfallTargets[x][y] != 0) {
                        g.setColor(new Color(0, 0, 155));
                    } else {
                        g.setColor(Color.BLACK);
                    }
                    //g.setColor(Color.BLACK);
                    g.fillArc((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2, (int) angI, -angF - inc);
                    //}
                    if (waterfall[x][y] > 0) {
                        if (waterfall[x][y] < colorDw) {
                            g.setColor(Color.BLACK);
                        } else if (waterfall[x][y] > colorUp) {
                            g.setColor(Color.GREEN);
                        } else {
                            g.setColor(new Color(0, (waterfall[x][y] - colorDw) * 255 / (colorUp - colorDw), 0));
                        }

                        g.fillArc((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2, (int) angI, -angF - inc);
                    }
                    angI -= 4.5;
                } else {
                    System.out.println("Error #??: el valor a desplegar esta fuera de rango");
                }
            }
            if ("SSF".equals(modelo)) {
                p -= 3;
            } else if ("SSPV".equals(modelo)) {
                p -= 2;
            }
            n = 0;
        }

        g.setColor(Color.GREEN);
        p = 251;
        if ("SSF".equals(modelo)) {
            p += 50;
        }

        g.drawOval((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2);
        g.setColor(new Color(100, 100, 100));
        p = 0;
        if ("SSPV".equals(modelo)) {
            p += 50;
            g.drawOval((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2);
            for (int x = p; x < (200 + p); x += 35) {
                g.drawOval((limWinX / 2) - x, (limWinY / 2) - x, x * 2, x * 2);
            }
            g.drawLine((limWinX / 2) - 252, (limWinY / 2), (limWinX / 2) + 252, (limWinY / 2));
            g.drawLine((limWinX / 2), (limWinY / 2) - 252, (limWinX / 2), (limWinY / 2) + 252);
        } else {
            g.drawOval((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2);
            for (float x = p; x < (300 + p); x += 37.5) {
                g.drawOval((int) ((limWinX / 2) - x), (int) ((limWinY / 2) - x), (int) (x * 2), (int) (x * 2));
            }
            g.drawLine((limWinX / 2) - 302, (limWinY / 2), (limWinX / 2) + 302, (limWinY / 2));
            g.drawLine((limWinX / 2), (limWinY / 2) - 302, (limWinX / 2), (limWinY / 2) + 302);
        }

        g.setColor(Color.BLACK);
        p = 0;
        if ("SSPV".equals(modelo)) {
            p += 50;
        }

        //g.fillOval((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2);
        g.setColor(Color.GRAY);

        //g.drawOval((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2);
        g.setColor(Color.YELLOW);
        p = refPPI * 2 + 50;
        if ("SSF".equals(modelo)) {
            p += 50;
        }

        g.drawOval((limWinX / 2) - p, (limWinY / 2) - p, p * 2, p * 2);
        if ("SSPV".equals(modelo)) {
            for (int x = 0; x < 8; x++) {
                g.drawString(((x * 12.5) * escalaPPI) / 10 + "", (limWinX / 2) - 10, ((limWinY / 2) - x * 25) - 45);
            }
        } else {
            for (int x = 1; x < 8; x++) {
                g.drawString(((x * 12.5) * escalaPPI) / 10 + "", (limWinX / 2) - 10, ((limWinY / 2) - x * 38));
            }
        }

        g.drawString("La distancia entre anillos esta", 20, 20);
        if ("SSPV".equals(modelo)) {
            g.drawString("en milla nautica", 20, 35);
        } else {
            g.drawString("en yardas", 20, 37);
        }
        Graphics2D g2d = (Graphics2D) g;
        float[] dash = {5};
        g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f));
        lineaAux(g, rumboB);
        if ("SSPV".equals(modelo)) {
            rX = (int) (Math.cos((rumboB + 90) * Math.PI / 180 - Math.PI) * 278 + (limWinX / 2));
            rY = (int) (Math.sin((rumboB + 90) * Math.PI / 180 - Math.PI) * 278 + (limWinY / 2));
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(limWinX / 2, limWinY / 2, rX, rY);
            rX = (int) (Math.cos((rumboP + 90) * Math.PI / 180 - Math.PI) * 278 + (limWinX / 2));
            rY = (int) (Math.sin((rumboP + 90) * Math.PI / 180 - Math.PI) * 278 + (limWinY / 2));
            g.setColor(new Color(230, 95, 0));
            g.drawLine(limWinX / 2, limWinY / 2, rX, rY);
        } else {
            /*rX = (int) (Math.cos((angMarc + 90) * Math.PI / 180 - Math.PI) * 278 + (limWinX / 2));
            rY = (int) (Math.sin((angMarc + 90) * Math.PI / 180 - Math.PI) * 278 + (limWinY / 2));
            g.setColor(new Color(230, 95, 0));
            g.drawLine(limWinX / 2, limWinY / 2, rX, rY);*/
        }

        g2d.setStroke(new BasicStroke(5.7f));
        g.setColor(Color.GREEN);
        //g.drawString("N", (limWinX/2)-5, 20);

        g2d.drawString("N", (limWinX / 2) - 5, 20);

        if ("SSPV".equals(modelo)) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("Rumbo del Buque " + rumboB + "°", limWinX - 150, 20);
            rX = (int) (Math.cos((rumboB + 90) * Math.PI / 180 - Math.PI) * 40 + (limWinX / 2));
            rY = (int) (Math.sin((rumboB + 90) * Math.PI / 180 - Math.PI) * 40 + (limWinY / 2));
            rumbo(g, rX, rY);
            g.setColor(new Color(230, 95, 0));
            g.drawString("Rumbo del SSPV   " + rumboP + "°", limWinX - 150, 40);
            rX = (int) (Math.cos((rumboP + 90) * Math.PI / 180 - Math.PI) * 25 + (limWinX / 2));
            rY = (int) (Math.sin((rumboP + 90) * Math.PI / 180 - Math.PI) * 25 + (limWinY / 2));
            rumbo(g, rX, rY);
        } else {
            /*g.setColor(new Color(230, 95, 0));
            rX = (int) (Math.cos((angMarc + 90) * Math.PI / 180 - Math.PI) * 25 + (limWinX / 2));
            rY = (int) (Math.sin((angMarc + 90) * Math.PI / 180 - Math.PI) * 25 + (limWinY / 2));
            rumbo(g, rX, rY);
            if (angMarc < 0) {
                angMarc += 360;
            }
            g.drawString("Angulo de marcación   " + angMarc + "°", limWinX - 180, 20);*/
        }
        System.out.println("TARGET: " + getAngActBTR() + " " + getAngTargetBTR() + " " + getDisTargetBTR() + " " + getDisDPaso());
        if (getAngActBTR() != 0 && getAngTargetBTR() != 0 && getDisTargetBTR() != 0 && getDisDPaso() != 0) {
            int angActBTR = getAngActBTR();
            int angTargetBTR = getAngTargetBTR();
            int disTargetBTR = getDisTargetBTR() * getDisDPaso();
            rX = (int) (Math.cos((angActBTR + 90) * Math.PI / 180 - Math.PI) * ((this.R * 30) / this.escalaPPI) + (limWinX / 2));
            rY = (int) (Math.sin((angActBTR + 90) * Math.PI / 180 - Math.PI) * ((this.R * 30) / this.escalaPPI) + (limWinY / 2));
            g.setColor(new Color(230, 95, 0));
            g.fillOval(rX - 6, rY - 6, 12, 12);
            g.setFont(new Font("Calibri", 0, 8));
            g.setColor(Color.WHITE);
            g.drawString("A", rX - 2, rY + 3);
            
            rX = (int) (Math.cos((angTargetBTR + 90) * Math.PI / 180 - Math.PI) * ((disTargetBTR * 30) / this.escalaPPI) + (limWinX / 2));
            rY = (int) (Math.sin((angTargetBTR + 90) * Math.PI / 180 - Math.PI) * ((disTargetBTR * 30) / this.escalaPPI) + (limWinY / 2));
            g.setColor(new Color(230, 95, 0));
            g.fillOval(rX - 6, rY - 6, 12, 12);
            g.setColor(Color.WHITE);
            g.drawString("T", rX - 2, rY + 3);
            System.out.println("DIS TARGET: " + rX + " " + rY);
        }//----------------------------------------------------------------------------------------------------------------------------------------------------------------

    }

    public void rumbo(Graphics g, int rX, int rY) {
        double ang, angSep, tx, ty;
        int dist = 0;
        Point punto1 = null, punto2 = null;
        punto1 = new Point(limWinX / 2, limWinY / 2);
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
            Point p = new Point(lineaAX - (limWinX / 2), (limWinY / 2) - lineaAY);

            if ("SSPV".equals(modelo)) {
                double angSep = medirAngSep(p);
                rX = (int) (Math.cos((angSep + 90) * Math.PI / 180 - Math.PI) * 278 + (limWinX / 2));
                rY = (int) (Math.sin((angSep + 90) * Math.PI / 180 - Math.PI) * 278 + (limWinY / 2));
                g.drawLine(limWinX / 2, limWinY / 2, rX, rY);
                int dist = rumboB - (int) angSep;
                if (dist > 180) {
                    dist -= 360;
                }
                if (dist < 0) {
                    dist *= -1;
                    g.drawString("a Estribor", limWinX - 70, 75);
                } else {
                    g.drawString("a Babor", limWinX - 58, 75);
                }
                g.drawString("Blanco respecto al Buque " + dist + "°", limWinX - 200, 60);
            }
            /*else {
                try {
                    angMarc = (int) medirAngSep(p);
                    a.escribirTxt("resource/angMarc.txt", angMarc);
                } catch (IOException ex) {
                    System.err.println("Error al guardar arnMarc " + ex.getMessage());
                }
            }*/
        }
        if ("SSF".equals(modelo)) {
            if (distOrPas != 0) {
                g.drawString("Boya Pasiva a: " + df.format(distOrPas * 1.09361) + " Yardas", limWinX - 190, 50);
            }
            if (distOrAct != 0) {
                g.drawString("Boya Activa a: " + df.format(distOrAct * 1.09361) + " Yardas", limWinX - 190, 35);
            }
            if (R != 0) {
                //g.drawString("Distancia entre boyas: " + df.format(R * 1.09361) + " Yardas", limWinX - 240, 20);
                g.drawString("Distancia entre boyas: " + df.format(R) + " Mts", limWinX - 240, 20);
                g.setColor(new Color(230, 95, 0));
                //rX = (int) (Math.cos((angActPas + 90) * Math.PI / 180 - Math.PI) * (((R * 20 * 1.09361) / escalaPPI) + 50) + (limWinX / 2));
                rX = (int) (Math.cos((angActPas + 90) * Math.PI / 180 - Math.PI) * ((R * 30) / escalaPPI) + (limWinX / 2));
                //rX = (int) (Math.cos((angOrAct + 90) * Math.PI / 180 - Math.PI) * 200 + (limWinX / 2));
                //rY = (int) (Math.sin((angActPas + 90) * Math.PI / 180 - Math.PI) * (((R * 20 * 1.09361) / escalaPPI) + 50) + (limWinY / 2));
                rY = (int) (Math.sin((angActPas + 90) * Math.PI / 180 - Math.PI) * ((R * 30) / escalaPPI) + (limWinY / 2));
                //rY = (int) (Math.sin((angOrAct + 90) * Math.PI / 180 - Math.PI) * 200 + (limWinY / 2));
                g.drawLine(limWinX / 2, limWinY / 2, rX, rY);
                g.fillOval(rX - 3, rY - 3, 6, 6);
                g.setColor(Color.YELLOW);
            }
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

    public int[][] getWaterfall() {
        return this.waterfall;
    }

    /*public void graficarAudio(Graphics g) {
        int limX = 1;
        int xi = 0;
        
                
        for (int i = 0; i < 760; i++) {
            g.drawLine(xi, getSize().height - (((sound[i * 680]) * 10) / 255) - 90, xi + limX, getSize().height - (((sound[(i * 680) + 680]) * 10) / 255) - 90);
            xi += limX;
        }
        xi = 0;
        for (int i = 0; i < 760; i++) {
            g.drawLine(xi, getSize().height - (sound[i + ((int) (rangoSound * 680))]) + 80, xi + limX, getSize().height - (sound[i + 1 + ((int) (rangoSound * 680))]) + 80);
            xi += limX;
        }
        g.setColor(Color.WHITE);
        System.out.println("repaint");
        g.drawLine(rangoSound, 620, rangoSound, 645);
    }*/
    public void leerTxtArrayInt(String dir) {                                     //lee lo que haya en un archivo txt, recibe como parametros la direccion tipo String y devuelve el String del contenido en una sola linea
        String info = "";
        int lim = 0;
        limSound = 0;
        try {
            BufferedReader bf = new BufferedReader(new FileReader(dir));
            String bfRead;
            while ((bfRead = bf.readLine()) != null && lim < 20) {
                info += bfRead;
                lim++;
            }
        } catch (Exception e) {
            System.err.println("SOY READ LINE: No se encontro el archivo en " + dir);
        }
        char[] charArray = info.toCharArray();
        info = "";
        for (char temp : charArray) {
            if (!(temp == ',') && !(temp == ';')) {
                info += temp;
            } else {
                try {
                    sound[limSound] = Integer.parseInt(info);
                } catch (Exception e) {
                    System.err.println("ParseInt: " + e.getMessage());
                }
                info = "";
                limSound++;
            }
        }
        System.out.println(limSound);
    }

    /*public void setRangoSound(int rangoSound) {
        this.rangoSound = rangoSound;
        repaint();
    }

    public void resetRangoSound() {
        this.rangoSound = 0;
    }

    public void run() {
        rangoSound++;
        repaint();
        //this.repaint(0, 645, getSize().width, getSize().height-645);
        System.out.println(rangoSound + " run");
    }*/
    public void setAnillo(String info, int x) {
        String box = "";
        int y = 0;
        char[] charArray = info.toCharArray();
        for (char temp : charArray) {
            if (!(temp == ',') && !(temp == ';')) {
                box += "" + temp;
            } else if (temp == ',' || temp == ';') {
                waterfall[x - 1][y] = Integer.parseInt(box);
                y++;
                box = "";
            }
        }
        refPPI = x;
        repaint();
    }

    public void setAng(String targets) {
        int angMarc = Integer.parseInt(a.leerTxtLine("resource/angMarc.txt"));
        int n = 0;
        for (int x = 0; x < 100; x++) {
            target[x] = 0;
            disTarget[x] = 0;
        }
        char[] charArray = targets.toCharArray();
        for (char temp : charArray) {
            if (n < 10) {
                if (!(temp == ',') && !(temp == ';')) {
                    info += temp;
                } else {
                    try {
                        target[n] = Double.parseDouble(info);
                    } catch (Exception e) {
                        System.err.println("Error catch del setInfo: " + e.getMessage());
                    }
                    info = "";
                    n++;
                }
            }
        }
        if (n > 1) {
            for (int i = 0; i < n; i++) {
                if (target[i + 1] > target[0]) {
                    disTarget[i] = (int) ((target[i + 1] - target[0]) * 10);
                    System.out.println("disTarget " + (disTarget[i] / 10));
                }
            }
        }
        n = 0;
        int ang = (int) (angMarc * longPPI / 360);
        System.out.println("ang " + ang);
        info = "";

        for (int x = 0; x < waterfall.length; x--) {                                                 //inicializa el waterfall en cero
            n = 0;
            for (int y = 0; y < longPPI; y++) {
                if (disTarget[n] < x && n < 100) {
                    n++;
                }
                if (y == ang && x == disTarget[n]) {
                    waterfall[x][y] = 255;
                    //info += "250";
                    /*if (disTarget[n] < disTarget[n+1]) {
                            n++;
                        }*/
                } else {
                    waterfall[x][y] = 0;
                    //info += "0";
                }
                /*if (y != longPPI - 1) {
                    info += ",";
                } else {
                    info += ";";
                }*/
            }
        }
    }

    public void setTargets(String targets) {
        this.targets = targets;
        boolean bOri = true;
        int dist = 0;
        int rest = 0; //valor que se guardará del pulso para restarselo a los targets
        int disAnt = 0; //distancia anterior usado para eliminar rebotes del pulso
        int hMAnt = 0; //hidrofono anterior con mayor intenciadad
        double RtpMts = 0;
        int Rtp = 0;
        int hMdw, hMup, secAng;
        int[] h = new int[4];
        int[] hM = new int[2];//esta variable es para saber el Hidrofono con mas intencidad: en la posicion 0 guarda la intencidad y en la pocision 1 guarda el numero de Hidrofono
        int[] Act = new int[4];
        int disAct = 0;
        boolean bAct = true;
        int n = 0;
        char[] charArray = targets.toCharArray();
        for (int x = 0; x < waterfall.length; x++) {                                         //inicializa el waterfall en cero
            for (int y = 0; y < longPPI; y++) {
                waterfall[x][y] = 0;
                waterfallTargets[x][y] = 0;
            }
        }
        try {
            for (char temp : charArray) {
                if (!(temp == '$') && !(temp == ',') && !(temp == ';')) {
                    info += temp;
                } else if (bOri) {
                    Ori = Integer.parseInt(info);
                    info = "";
                    bOri = false;
                } else {
                    switch (n) {
                        case 0:
                            h[n] = Integer.parseInt(info);
                            //System.out.println("El hidrofono 1 tiene: " + h[n]);
                            if (bAct) {
                                Act[n] = h[n];
                            }
                            hM[0] = h[n];
                            hM[1] = 1;
                            info = "";
                            n++;
                            break;
                        case 1:
                            h[n] = Integer.parseInt(info);
                            //System.out.println("El hidrofono 2 tiene: " + h[n]);
                            if (bAct) {
                                Act[n] = h[n];
                            }
                            if (h[n] > hM[0]) {
                                hM[0] = h[n];
                                hM[1] = 2;
                            }
                            info = "";
                            n++;
                            break;
                        case 2:
                            h[n] = Integer.parseInt(info);
                            //System.out.println("El hidrofono 3 tiene: " + h[n]);
                            if (bAct) {
                                Act[n] = h[n];
                            }
                            if (h[n] > hM[0]) {
                                hM[0] = h[n];
                                hM[1] = 3;
                            }
                            info = "";
                            n++;
                            break;
                        case 3:
                            h[n] = Integer.parseInt(info);
                            //System.out.println("El hidrofono 4 tiene: " + h[n]);
                            if (bAct) {
                                Act[n] = h[n];
                            }
                            if (h[n] > hM[0]) {
                                hM[0] = h[n];
                                hM[1] = 4;
                            }
                            info = "";
                            n++;
                            break;
                        case 4:
                            dist = (int) (Double.parseDouble(info) * 10);
                            if (bAct) {
                                rest = dist;
                                dist = (int) (R * 10);
                                disAnt = dist;
                                System.out.println("R: " + R);
                                for (int a = 20; a > 0; a--) {
                                    //secAng = (hM[1] * 20) - a;
                                    secAng = a;
                                    secAng += (int) (((Ori * 80) / 360));
                                    if (secAng >= 80) {
                                        secAng -= 80;
                                    }
                                    //System.out.println("W sec: " + secAng + " hM: " + hM[0]);
                                    waterfallTargets[(int) R * 10 / escalaPPI][secAng] = hM[0];
                                }
                            } else {
                                dist -= rest;
                                dist += (int) (R * 10);
                            }
                            hMdw = hM[1] - 1;
                            if (hMdw == 0) {
                                hMdw = 4;
                            }
                            hMup = hM[1] + 1;
                            if (hMup == 5) {
                                hMup = 1;
                            }
                            //if(hM[hMdw]==hM[hMup]){   //falta calcular mejor el angulo

                            //System.out.println("dist: " + dist);
                            if ((dist / escalaPPI) < 10000) {// && (disAnt + 3) != dist && hM[1] != hMAnt) {
                                /*System.out.println("La distancia es: " + dist);
                                System.out.println("disAnt es:       " + (disAnt + 3));
                                System.out.println("hM es:           " + hM[1]);
                                System.out.println("hMAnt es:        " + hMAnt);*/
                                for (int a = 20; a > 0; a--) {
                                    secAng = (hM[1] * 20) - a;
                                    //secAng = a;
                                    //Ori -= (Ori - Beta);
                                    //Ori -= 45;
                                    if (Ori < 0) {
                                        Ori += 360;
                                    }
                                    secAng += (int) (((Ori * 80) / 360));
                                    if (secAng >= 80) {
                                        secAng -= 80;
                                    }
                                    //waterfallTargets[(int) ((dist * 1.09361) / escalaPPI)][secAng] = hM[0];//falata agregar la orientación
                                    if (bAct) {
                                        if (a == 1) {
                                            disAct = dist;
                                            bAct = false;
                                        }
                                    } else if ((disAnt + 3) == dist && hM[1] == hMAnt) {
                                        if ((disAnt + 3) == dist) {
                                            if (a == 1) {
                                                System.out.println("distancia igual");
                                            }
                                        } else if (hM[1] == hMAnt) {
                                            if (a == 1) {
                                                System.out.println("angulo igual");
                                            }
                                        }
                                    } else {
                                        //System.out.println("La distancia es: " + dist + " disAct es: " + disAct);
                                        //System.out.println("hM es: " + hM[1] + " hMAnt es: " + hMAnt);
                                        //System.out.println("-");
                                        ATta = (dist - disAct) / C;
                                        R *= 10;
                                        //System.out.println("R: " + R);
                                        //RtpMts = ((R * ATta) + ((ATta * ATta) * (C * C) * 0.5)) / (R + ATta * C - 2 * R * Math.cos(Math.toRadians(Beta - ((secAng * 360) / 80))));
                                        RtpMts = ((0.5 * (ATta * ATta) * (C * C)) + (R * ATta * C)) / (R + ATta * C - R * Math.cos(Math.toRadians(((secAng * 360) / 80) - Beta)));
                                        //RtpMts = ((0.5 * (ATta * ATta) * (C * C)) + (R * ATta * C)) / (R + ATta * C - R * Math.cos(Math.toRadians(Ori - Beta)));
                                        R /= 10;

                                        //System.out.println("  RtpMts: " + RtpMts);
                                        //System.out.println("Cos: " + Math.cos(Math.toRadians(Beta - ((secAng * 360) / 80))));
                                        //System.out.println("  Rtp: " + (RtpMts * 1.09361));
                                        Rtp = (int) (RtpMts / escalaPPI); //se convierte de Metros a Yerdas
                                        if (a == 1) {
                                            //System.out.println("  Rtp: " + Rtp + "\tRtpMts: " + RtpMts / 10);
                                            System.out.println("La orientación es: " + Ori + "\tsecAng: " + secAng + "\t  Rtp: " + Rtp + "\tRtpMts: " + RtpMts / 10);
                                        }
                                        if (Rtp >= 0 && Rtp < 100) {
                                            //waterfall[Rtp][secAng] = hM[0];
                                            //System.out.println("waterfall " + Rtp + " " + secAng);
                                            waterfall[Rtp][secAng] = 200;
                                        }
                                    }
                                }
                                //System.out.println();
                            }
                            //} else {
                            //}
                            disAnt = dist;
                            hMAnt = hM[1];
                            info = "";
                            n = 0;
                            break;

                        //target[n] = Double.parseDouble(info);
                    }
                }
            }
            repaint();
        } catch (Exception e) {
            System.err.println("PPI/despliegue - Error catch del setInfo: " + e.getMessage() + " N: " + n);
        }
    }

    public void setAngBeta(String info) {
        char[] charArray = info.toCharArray();
        String Angulo = "";
        boolean bA = true;
        for (char temp : charArray) {
            if (bA) {
                bA = false;
            } else {
                Angulo += temp;
            }
        }
        Beta = Integer.parseInt(Angulo);
        repaint();
    }

    public void setAngBeta(double Beta) {
        this.angActPas = Beta;
        this.Beta = Beta;
        if (targets != null) {
            setTargets(targets);
        }
        repaint();
    }

    public void setDistBoyas(double dist) {
        System.out.println("Dist entre boyas: " + dist);
        this.R = dist;
        if (targets != null) {
            setTargets(targets);
        }
        repaint();
    }

    public void setDistBoyas(double dist, int aLat, int aLog, int pLat, int pLog) {
        this.R = dist;
        Point p = new Point(aLat - pLat, aLog - pLog);
        this.angActPas = medirAngSep(p);
        this.Beta = medirAngSep(p);
        System.out.println("Dist entre Boyas: " + dist + " " + angActPas + "°");
        repaint();
    }

    public void setDisOrPas(double dist, int oLat, int oLog, int pLat, int pLog) {
        this.distOrPas = dist;
        Point p = new Point(pLat - oLat, pLog - oLog);
        this.angOrPas = medirAngSep(p);
        System.out.println("Dist entre Origen y Boya Pasiva: " + dist + " " + angOrPas + "°");
        repaint();
    }

    public void setDisOrAct(Double dist, int oLat, int oLog, int aLat, int aLog) {
        this.distOrAct = dist;
        Point p = new Point(aLat - oLat, aLog - oLog);
        this.angOrAct = medirAngSep(p);
        System.out.println("Dist entre Origen y Boya Activa: " + dist + " " + angOrAct + "°");
        repaint();
    }

    public void setEscalaPPI(int escalaPPI) {
        this.escalaPPI = escalaPPI;
        if (targets != null) {
            setTargets(targets);
        }
    }

    public void save() {
        if ("SSPV".equals(modelo)) {
            String s = "";
            for (int x = 0; x < waterfall.length; x++) {
                for (int y = 0; y < waterfall[0].length; y++) {
                    s += waterfall[x][y];
                    if (y < waterfall[0].length - 1) {
                        s += ",";
                    }
                }
                s += ";";
                if (x < waterfall.length - 1) {
                    s += "\n";
                }
            }
            a.save("resource/ppi", s);
        } else {
            a.save("resource/ppi", targets);
        }
    }

    public void setAngActBTR(int angActBTR) {
        this.angActBTR = angActBTR;
    }

    public void setAngTargetBTR(int angTargetBTR) {
        this.angTargetBTR = angTargetBTR;
    }

    public void setDisTargetBTR(int disTargetBTR) {
        this.disTargetBTR = disTargetBTR;
    }

    public void setDisDPaso(int disDPaso) {
        this.disDPaso = disDPaso;
    }

    public int getAngActBTR() {
        return this.angActBTR;
    }

    public int getAngTargetBTR() {
        return this.angTargetBTR;
    }

    public int getDisTargetBTR() {
        return this.disTargetBTR;
    }

    public int getDisDPaso() {
        return this.disDPaso;
    }
}
