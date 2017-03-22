/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppi;

//import com.sun.xml.internal.ws.api.message.Message;
import java.net.*;
import java.io.*;
import java.util.Properties;
import javax.swing.JFrame;

/**
 *
 * @author siviso
 */
public class comSPV extends Thread {

    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    Socket socket;
    byte[] mensaje_bytes = new byte[256];
    String mensaje = "";
    String msn;
    String texto;
    String word;
    int nDatos;
    boolean error;
    boolean habilitado = false;
    int t = 1000;
    JFrame window;
    String DIR = "resource/ppiData.txt";
    String REF = "resource/ppiRef.txt";

    public boolean getHabilitado() {
        return this.habilitado;
    }

    public String getLongPPI() {
        Properties prop = new Properties();
        InputStream input = null;
        String longPPI = null;
        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
            longPPI = prop.getProperty("longPPI");
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
        return longPPI;
    }

    public void setHabilitado(boolean h) {
        this.habilitado = h;
    }

    public void setWindow(JFrame window) {
        this.window = window;
    }

    public void run() {
        try {
            comSPPsend cspps = new comSPPsend();
            socket = new Socket("192.168.1.10", 30000);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader inp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            archivo a = new archivo();
            sleep(1000);

            while (true) {
                if (getHabilitado()) {
                    setHabilitado(false);
                    error = false;
                    texto = "";
                    word = "";
                    nDatos = 0;
                    mensaje = "ModoPPI\n";
                    out.writeUTF(mensaje);
                    System.out.println("Envie: " + mensaje);
                    msn = inp.readLine();
                    System.out.println("Recibí: " + msn);
                    if ("Modo PPI OK".equals(msn)) {
                        mensaje = "ActivarPPI\n";
                        out.writeUTF(mensaje);
                        System.out.println("Envie: " + mensaje);
                        msn = inp.readLine();
                        System.out.println("Recibí: " + msn);
                        if ("Activar PPI OK".equals(msn)) {
                            for (int n = 1; n <= 100; n++) {
                                texto = "";
                                nDatos = 0;
                                mensaje = "DatosPPI " + Integer.toString(n) + "\n";
                                out.writeUTF(mensaje);
                                System.out.println("Envie: " + mensaje);
                                msn = inp.readLine();
                                System.out.println("Recibí: " + msn);
                                if ("Datos PPI OK".equals(msn)) {
                                    mensaje = "PPI1\n";
                                    out.writeUTF(mensaje);
                                    System.out.println("Envie: " + mensaje);
                                    msn = inp.readLine();
                                    System.out.println("Recibí: " + msn);
                                    char[] charArray = msn.toCharArray();
                                    for (char temp : charArray) {
                                        if (temp == '1' || temp == '2' || temp == '3' || temp == '4' || temp == '5' || temp == '6' || temp == '7' || temp == '8' || temp == '9' || temp == '0') {
                                            word += temp;
                                        }
                                        if (temp == ',' || temp == ';') {
                                            nDatos++;
                                            if (word != "") {
                                                texto += word;
                                                texto += ",";
                                                word = "";
                                            } else {
                                                error = true;
                                                System.out.println("Error: dato en la posicion " + nDatos + " no fue encontrado");
                                            }
                                        }
                                        if (temp == ';') {
                                            if (nDatos != 40) {
                                                error = true;
                                                System.out.println("Error: esperaba recibir 40 datos y recibí " + nDatos);
                                            }
                                        }
                                    }
                                    if (!error) {
                                        nDatos = 0;
                                        error = false;
                                        word = "";
                                        mensaje = "PPI2\n";
                                        System.out.println("Envie: " + mensaje);
                                        out.writeUTF(mensaje);
                                        msn = inp.readLine();
                                        System.out.println("Recibí: " + msn);
                                        charArray = msn.toCharArray();
                                        for (char temp : charArray) {
                                            if (temp == ',' || temp == ';') {
                                                nDatos++;
                                            }
                                            if (temp == ';') {
                                                if (nDatos != 40) {
                                                    error = true;
                                                    System.out.println("Error: esperaba recibir 40 datos y recibí " + nDatos);
                                                }
                                            }
                                        }
                                        if (!error) {
                                            nDatos = 0;
                                            for (char temp : charArray) {
                                                if (temp == '1' || temp == '2' || temp == '3' || temp == '4' || temp == '5' || temp == '6' || temp == '7' || temp == '8' || temp == '9' || temp == '0') {
                                                    word += temp;
                                                }
                                                if (temp == ',' || temp == ';') {
                                                    nDatos++;
                                                    if (word != "") {
                                                        texto += word;
                                                        texto += temp;
                                                        word = "";
                                                    } else {
                                                        error = true;
                                                        System.out.println("Error: dato en la posicion " + nDatos + " no fue encontrado");
                                                    }

                                                }
                                            }
                                            if (!error) {
                                                System.out.println("Guardaré: " + texto);
                                                a.escribirTxtLine(DIR, texto, n);
                                                if (n < 100) {
                                                    a.escribirTxt(REF, n + 1);
                                                    //a.resetLine(DIR, n + 1, Integer.parseInt(getLongPPI()));
                                                }
                                                window.repaint();
                                                sleep(200);
                                            }
                                        }
                                    }
                                } else {
                                    error = true;
                                    System.out.println("Error: esperba <Datos PPI OK> y recibí <" + msn + ">, Compruebe la comunicación");
                                }
                            }
                        } else {
                            System.out.println("Error: esperba <Activar PPI OK> y recibí <" + msn + ">, Compruebe la comunicación");
                        }
                    } else {
                        System.out.println("Error: esperba <Modo PPI OK> y recibí <" + msn + ">, Compruebe la comunicación");
                    }
                    cspps.enviar("PPI OK");
                }
                try {
                    sleep(t);                                //espera un segundo
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

}
