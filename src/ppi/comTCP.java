/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppi;

import java.net.*;
import java.io.*;

/**
 *
 * @author siviso
 */
public class comTCP extends Thread {

    ServerSocket socket;
    boolean fin = false;

    public void run() {
        System.out.println("comTCP run");
        try {
            socket = new ServerSocket(6001);
            Socket socket_cli = socket.accept();
            DataInputStream in = new DataInputStream(socket_cli.getInputStream());
            do {
                String mensaje = "";
                mensaje = in.readUTF();
                System.out.println(mensaje);
            } while (true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
