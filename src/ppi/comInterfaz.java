/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppi;

import java.io.*;
import java.net.*;
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
    DatagramPacket paquete;
    int puerto = 0;
    String cadenaMensaje = "";
    DatagramPacket servPaquete;
    byte[] RecogerServidor_bytes = new byte[256];
    Properties prop = new Properties();
    InputStream input = null;
    comSPV cspv = new comSPV();
    char[] charArray;
    String word;
    boolean bConfPlusTx = false;

    //@Override
    public void run(JFrame window) {
        try {
            cspv.setWindow(window);
            //cspv.setHabilitado(true);
            try {
                input = new FileInputStream("config.properties");
                prop.load(input);
                mensaje = prop.getProperty("modelo");
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
            if (null != mensaje) {
                switch (mensaje) {
                    case "SSPP":
                        puerto = 5001;
                        break;
                    case "SSF":
                        puerto = 5002;
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
                if (null != cadenaMensaje) {
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
                            a.save("resource/ppiData.txt");
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
                        default:
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
                                cspv.setHabilitado(true);
                            }
                            break;
                    }
                }
            } while (true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
