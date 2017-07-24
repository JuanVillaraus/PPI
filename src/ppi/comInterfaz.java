/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;
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
    /*String texto = "";
    int opcion;
    boolean bTopWord;
    String topWord;*/
    Properties prop = new Properties();
    InputStream input = null;
    comSPV cspv = new comSPV();
    char[] charArray;
    String word;

    //@Override
    public void run(JFrame window) {
        try {
            cspv.setWindow(window);
            //cspv.setHabilitado(true);
            try {
                input = new FileInputStream("config.properties");
                prop.load(input);
                mensaje = prop.getProperty("modelo");
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
            if ("SSPP".equals(mensaje)) {
                puerto = 5001;
            } else if ("SSF".equals(mensaje)) {
                puerto = 5002;
            } else if ("SSPV".equals(mensaje)) {
                puerto = 5003;
                cspv.setPuerto(puerto);
                //cspv.start();
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
                System.out.println("Esto recibí: " + cadenaMensaje);
                if ("OFF".equals(cadenaMensaje)) {
                    window.setExtendedState(JFrame.ICONIFIED);
                } else if ("ON".equals(cadenaMensaje)) {
                    window.setExtendedState(JFrame.NORMAL);
                } else if ("PULSO".equals(cadenaMensaje)) {
                    cspv.setHabilitado(true);
                } else if ("EXIT".equals(cadenaMensaje)) {
                    System.exit(0);
                } else if ("SAVE".equals(cadenaMensaje)) {
                    a.save("resource/ppiData.txt");
                } else if ("RP".equals(cadenaMensaje)) {                     //PPI repaint
                    window.repaint();
                } else if ("LONG".equals(cadenaMensaje)) {
                    mensaje = "#" + cspv.getLongPPI();
                    mensaje_bytes = mensaje.getBytes();
                    paquete = new DatagramPacket(mensaje_bytes, mensaje.length(), address, 5002);
                    socket.send(paquete);
                    if (puerto == 5003) {
                        cspv.setPuerto(puerto);
                        cspv.start();
                    }
                } else {
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
                }
            } while (true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
