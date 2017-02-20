package ppi;


import java.io.*;
import java.net.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author siviso
 */
public class sendTCP extends Thread {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    Socket socket;
    byte[] mensaje_bytes = new byte[256];
    String mensaje = "";
    int n = 0;

    public void run() {
        System.out.println("sendTCP run");

        try {
            comTCP ct = new comTCP();
            ct.start();
            socket = new Socket("192.168.1.10", 6001);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            do {
                //mensaje = in.readLine();
                out.writeUTF("ModoPPI");
                out.writeUTF("ActivarPPI");
                out.writeUTF("DatosPPI 1");
                out.writeUTF("PPI1");
                //out.writeUTF("PPI2");
                sleep(2000);
                System.out.println("mensaje tcp enviado " + n);
                n++;
            } while (true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
