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
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    String cadenaMensaje = "";
    DatagramPacket servPaquete;
    byte[] RecogerServidor_bytes = new byte[256];
    String texto = "";
    int opcion;
    boolean bTopWord;
    String topWord;

    //@Override
    public void run(JFrame window) {
        try {
            mensaje_bytes = mensaje.getBytes();
            address = InetAddress.getByName("localhost");
            mensaje = "runPPI";
            mensaje_bytes = mensaje.getBytes();
            paquete = new DatagramPacket(mensaje_bytes, mensaje.length(), address, 5002);
            socket = new DatagramSocket();
            socket.send(paquete);
            System.out.println("enviamos runPPI para inicializar la comunicación con el software");
            comSPPsend cspps = new comSPPsend();
            cspps.start();
            archivo a = new archivo();

            int i;
            do {
                RecogerServidor_bytes = new byte[256];
                servPaquete = new DatagramPacket(RecogerServidor_bytes, 256);
                socket.receive(servPaquete);
                cadenaMensaje = new String(RecogerServidor_bytes).trim();   //Convertimos el mensaje recibido en un string
                texto = "";
                bTopWord = true;
                topWord = "";
                if ("OFF".equals(cadenaMensaje)) {
                    window.setExtendedState(JFrame.ICONIFIED);
                    //System.out.println("PPI esta deshabilitado");
                    if (cspps.getHabilitado()) {
                        cspps.setHabilitado(false);
                    }
                } else if ("ON".equals(cadenaMensaje)) {
                    window.setExtendedState(JFrame.NORMAL);
                    //System.out.println("PPI esta habilitado");
                    if (!cspps.getHabilitado()) {
                        cspps.setHabilitado(true);
                    }
                } else if ("EXIT".equals(cadenaMensaje)) {
                    System.exit(0);
                } else if ("SAVE".equals(cadenaMensaje)) {
                    a.save("resource/ppiData.txt");
                } else if ("RP".equals(cadenaMensaje)) {                     //PPI repaint
                    window.repaint();
                /*} else if ("LONG".equals(cadenaMensaje)) {                    
                    Properties prop = new Properties();
                    InputStream input = null;
                    try {
                        input = new FileInputStream("config.properties");
                        prop.load(input);
                        mensaje = "LONG" + prop.getProperty("longPPI") + ";";
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
                    mensaje_bytes = mensaje.getBytes();
                    paquete = new DatagramPacket(mensaje_bytes, mensaje.length(), address, 5002);
                    socket.send(paquete);*/
                } else if (!("START OK!".equals(cadenaMensaje))) {
                    char[] charArray = cadenaMensaje.toCharArray();
                    for (char temp : charArray) {
                        if (bTopWord) {
                            topWord += temp;
                        } else {
                            texto += "" + temp;
                        }

                    }
                    //Calendar cal = Calendar.getInstance();
                    //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    //texto = sdf.format(cal.getTime()) + ",";
                    a.escribirTxtLine("resource/ppi.txt", texto, Integer.parseInt(topWord));

                    window.repaint();
                }
            } while (true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
