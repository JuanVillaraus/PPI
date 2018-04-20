/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppi;

import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author juan
 */
class comInterfaz extends Thread {

    DatagramSocket socket;
    InetAddress address;
    byte[] mensaje_bytes = new byte[256];
    String mensaje = "";
    String modelo = "";
    DatagramPacket paquete;
    DatagramPacket paqError;
    int puerto = 0;
    String cadenaMensaje = "";
    DatagramPacket servPaquete;
    byte[] RecogerServidor_bytes;// = new byte[256];
    Properties prop = new Properties();
    InputStream input = null;
    comSPV cspv = new comSPV();
    char[] charArray;
    String word;
    boolean bConfPlusTx = false;
    despliegue desp;
    winListener window = new winListener();
    double oLat = 0.0;
    double oLog = 0.0;
    double aLat = 0.0;
    double aLog = 0.0;
    double pLat = 0.0;
    double pLog = 0.0;
    String sLatLog = "";
    int escalaPPI = 1;

    //@Override
    public void run() {
        try {
            cspv.setWindow(window);
            //cspv.setHabilitado(true);
            try {
                input = new FileInputStream("config.properties");
                prop.load(input);
                modelo = prop.getProperty("modelo");
                if (prop.getProperty("ConfPulsTx").equals("si")) {
                    bConfPlusTx = true;
                }
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
            if (null != modelo) {
                switch (modelo) {
                    case "SSPP":
                        puerto = 5001;
                        desp = new despliegue(window);
                        break;
                    case "SSF":
                        puerto = 5002;
                        desp = new despliegue(window);
                        break;
                    case "SSPV":
                        puerto = 5003;
                        cspv.setPuerto(puerto);
                        //cspv.start();
                        break;
                    default:
                        break;
                }
            }
            address = InetAddress.getByName("localhost");
            mensaje = "ERROR";
            mensaje_bytes = mensaje.getBytes();
            paqError = new DatagramPacket(mensaje_bytes, mensaje.length(), address, puerto);
            mensaje = "runPPI";
            mensaje_bytes = mensaje.getBytes();
            paquete = new DatagramPacket(mensaje_bytes, mensaje.length(), address, puerto);
            socket = new DatagramSocket();
            socket.send(paquete);
            System.out.println("enviamos " + mensaje + " para inicializar la comunicación con el software");
            archivo a = new archivo();

            do {
                RecogerServidor_bytes = new byte[256];
                servPaquete = new DatagramPacket(RecogerServidor_bytes, 256);
                socket.receive(servPaquete);
                cadenaMensaje = new String(RecogerServidor_bytes).trim();   //Convertimos el mensaje recibido en un string
                System.out.println("Recibí: " + cadenaMensaje);
                if (null != cadenaMensaje && !"".equals(cadenaMensaje)) {
                    switch (cadenaMensaje) {
                        case "RUN":
                            cspv.start();
                            try {
                                sleep(300);
                            } catch (Exception e) {
                                Thread.currentThread().interrupt();
                                System.err.println("Error en el sleep del start en comInterfaz" + e.getMessage());
                            }
                        case "OFF":
                            window.setExtendedState(JFrame.ICONIFIED);
                            break;
                        case "ON":
                            window.setExtendedState(JFrame.NORMAL);
                            break;
                        case "PULSO":
                            cspv.setHabilitado(true);
                            break;
                        case "EXIT":
                            System.exit(0);
                        case "SAVE":
                            //a.save("resource/ppiData.txt");
                            break;
                        case "RP":
                            //PPI repaint
                            window.repaint();
                            break;
                        case "LONG":
                            mensaje = "#" + cspv.getLongPPI();
                            mensaje_bytes = mensaje.getBytes();
                            paquete = new DatagramPacket(mensaje_bytes, mensaje.length(), address, 5002);
                            socket.send(paquete);
                            if (puerto == 5003) {
                                cspv.setPuerto(puerto);
                                cspv.start();
                            }
                            break;
                        case "DIS":
                            if ((aLat != 0.0 && aLog != 0.0) && (pLat != 0.0 && pLog != 0.0)) {
                                desp.setDistBoyas(CalculationByDistance(aLat, aLog, pLat, pLog), ((int) (aLat * 10000)), ((int) (aLog * 10000)), ((int) (pLat * 10000)), ((int) (pLog * 10000)));
                            }
                            if ((oLat != 0.0 && oLog != 0.0) && (pLat != 0.0 && pLog != 0.0)) {
                                desp.setDisOrPas(CalculationByDistance(oLat, oLog, pLat, pLog), ((int) (oLat * 10000)), ((int) (oLog * 10000)), ((int) (pLat * 10000)), ((int) (pLog * 10000)));
                            }
                            if ((oLat != 0.0 && oLog != 0.0) && (aLat != 0.0 && aLog != 0.0)) {
                                desp.setDisOrAct(CalculationByDistance(oLat, oLog, aLat, aLog), ((int) (oLat * 10000)), ((int) (oLog * 10000)), ((int) (aLat * 10000)), ((int) (aLog * 10000)));
                            }
                            window.repaint();
                            break;
                        default:
                            if ("SSPV".equals(modelo)) {
                                if (bConfPlusTx) {
                                    charArray = cadenaMensaje.toCharArray();
                                    word = "";
                                    for (char temp : charArray) {
                                        if (temp == 'C' || temp == 'f' || temp == 'l' || temp == 'n' || temp == 'o' || temp == 'P' || temp == 's' || temp == 'T' || temp == 'u' || temp == 'x') {
                                            word += temp;
                                        }
                                    }
                                    if ("ConfPulsTx".equals(word)) {
                                        cspv.setConfPulso(cadenaMensaje);
                                        cspv.setHabilitado(true);
                                    }
                                } else {
                                    socket.send(paqError);
                                }
                            } else {
                                charArray = cadenaMensaje.toCharArray();
                                word = "";
                                if (charArray.length > 1) {
                                    for (int i = 1; i < charArray.length; i++) {
                                        word += charArray[i];
                                    }
                                    switch (charArray[0]) {
                                        case 'a':
                                            desp.setAngBeta(Double.parseDouble(word));
                                            break;
                                        case 'O':
                                            if (charArray[1] == 't') {
                                                sLatLog = "";
                                                for (int i = 2; i < charArray.length; i++) {
                                                    sLatLog += charArray[i];
                                                }
                                                oLat = Double.parseDouble(sLatLog);
                                                System.out.println("Latitud del Origen: " + sLatLog);
                                            } else if (charArray[1] == 'g') {
                                                sLatLog = "";
                                                for (int i = 2; i < charArray.length; i++) {
                                                    sLatLog += charArray[i];
                                                }
                                                oLog = Double.parseDouble(sLatLog);
                                                System.out.println("Longitud del Origen: " + sLatLog);
                                            } else {
                                                socket.send(paqError);
                                            }
                                            break;
                                        case 'A':
                                            if (charArray[1] == 't') {
                                                sLatLog = "";
                                                for (int i = 2; i < charArray.length; i++) {
                                                    sLatLog += charArray[i];
                                                }
                                                aLat = Double.parseDouble(sLatLog);
                                                System.out.println("Latitud de la boya Activa: " + sLatLog);
                                            } else if (charArray[1] == 'g') {
                                                sLatLog = "";
                                                for (int i = 2; i < charArray.length; i++) {
                                                    sLatLog += charArray[i];
                                                }
                                                aLog = Double.parseDouble(sLatLog);
                                                System.out.println("Longitud de la boya Activa: " + sLatLog);
                                            } else {
                                                socket.send(paqError);
                                            }
                                            break;
                                        case 'P':
                                            if (charArray[1] == 't') {
                                                sLatLog = "";
                                                for (int i = 2; i < charArray.length; i++) {
                                                    sLatLog += charArray[i];
                                                }
                                                pLat = Double.parseDouble(sLatLog);
                                                System.out.println("Latitud de la boya Pasiva: " + sLatLog);
                                            } else if (charArray[1] == 'g') {
                                                sLatLog = "";
                                                for (int i = 2; i < charArray.length; i++) {
                                                    sLatLog += charArray[i];
                                                }
                                                pLog = Double.parseDouble(sLatLog);
                                                System.out.println("Longitud de la boya Pasiva: " + sLatLog);
                                            } else {
                                                socket.send(paqError);
                                            }
                                            break;
                                        case 'S':
                                            escalaPPI = Integer.parseInt(word);
                                            desp.setEscalaPPI(escalaPPI);
                                            break;
                                        case 'T':
                                            if ((aLat != 0.0 && aLog != 0.0) && (pLat != 0.0 && pLog != 0.0)) {
                                                desp.setDistBoyas(CalculationByDistance(aLat, aLog, pLat, pLog), ((int) (aLat * 10000)), ((int) (aLog * 10000)), ((int) (pLat * 10000)), ((int) (pLog * 10000)));
                                            }
                                            desp.setTargets(word);
                                            break;
                                        case 'D':
                                            //desp.setDistBoyas(Double.parseDouble(word));
                                            int n = 0;
                                            System.out.println("DistTarget: " + word);
                                            word="";
                                            for (int i = 1; i < charArray.length; i++) {
                                                if (charArray[i] != ',' && charArray[i] != ';') {
                                                    word += charArray[i];
                                                } else {
                                                    switch (n) {
                                                        case 0:
                                                            desp.setDisTargetBTR(Integer.parseInt(word));
                                                            word = "";
                                                            n++;
                                                            break;
                                                        case 1:
                                                            desp.setAngActBTR(Integer.parseInt(word));
                                                            word = "";
                                                            n++;
                                                            break;
                                                        case 2:
                                                            desp.setAngTargetBTR(Integer.parseInt(word));
                                                            word = "";
                                                            n++;
                                                            break;
                                                    }
                                                }
                                            }
                                            break;
                                        case 'd':
                                            desp.setDisDPaso(Integer.parseInt(word));
                                            System.out.println("d paso:     " + word);
                                            desp.repaint();
                                            break;
                                        default:
                                            socket.send(paqError);
                                            break;
                                    }
                                } else {
                                    socket.send(paqError);
                                }
                            }
                            break;
                    }
                } else {
                    socket.send(paqError);
                }
            } while (true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static double CalculationByDistance(double lat1, double lon1, double lat2, double lon2) {
        int Radius = 6371;// radio de la tierra en  kilómetros
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        /*double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        //System.out.println("Radius Value" +"" + valueResult + "   KM  " + kmInDec + " Meter   " + meterInDec);*/

        return (Radius * c) * 1000;
    }
}
