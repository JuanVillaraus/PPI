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
    winListener window;
    //String DIR = "resource/ppiData.txt";
    String REF = "resource/ppiRef.txt";
    String rumboB = "resource/rumboB.txt";
    String rumboP = "resource/rumboP.txt";
    int puerto = 0;
    String ConfPulso;
    char[] charArray;
    despliegue desp;
    Properties prop = new Properties();
    InputStream input = null;
    String DIR = "";
    int PORT = 0;

    public void comSPV() {
        System.out.println("comSPV");
        desp = new despliegue(this.window);
        desp.addMouseListener(window);
        System.out.println("desp in cpmSPV");
    }

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
        return longPPI;
    }

    public String getConfPulso() {
        return this.ConfPulso;
    }

    public void setHabilitado(boolean h) {
        this.habilitado = h;
    }

    public void setWindow(winListener window) {
        this.window = window;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public void setConfPulso(String ConfPulso) {
        this.ConfPulso = ConfPulso;
    }

    public void run() {
        try {
            try {
                input = new FileInputStream("config.properties");
                prop.load(input);
                DIR = prop.getProperty("dirSSPV");
                PORT = Integer.parseInt(prop.getProperty("portPPI"));
                System.out.println("PPI comSPV " + DIR + " " + PORT);
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
            desp = new despliegue(this.window);
            desp.addMouseListener(window);
            while(!getHabilitado()) {
                try {
                    sleep(t);                                //espera un segundo
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }
            comSend cspps = new comSend();
            cspps.setPuerto(puerto);
            socket = new Socket(DIR, PORT);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader inp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            archivo a = new archivo();
            sleep(1000);
            while (true) {
                if (getHabilitado()) {
                    setHabilitado(false);
                    System.out.println("cspv while true");
                    error = false;
                    texto = "";
                    word = "";
                    nDatos = 0;

                    /*out.writeUTF(getConfPulso());
                    System.out.println("Envie: " + mensaje);

                    mensaje = "RUMBO\n";
                    out.writeUTF(mensaje);
                    System.out.println("Envie: " + mensaje);
                    msn = inp.readLine();
                    System.out.println("Recibí: " + msn);
                    charArray = msn.toCharArray();
                    for (char temp : charArray) {
                        if (temp == '1' || temp == '2' || temp == '3' || temp == '4' || temp == '5' || temp == '6' || temp == '7' || temp == '8' || temp == '9' || temp == '0') {
                            word += temp;
                        }
                        if (temp == ',') {
                            a.escribirTxt(rumboB, word);
                            word = "";
                        }
                        if (temp == ';') {
                            a.escribirTxt(rumboP, word);
                            word = "";
                        }
                    }
                    window.repaint();*/
                    mensaje = "ModoPPI";
                    out.writeUTF(mensaje);
                    System.out.println("Envie: " + mensaje);
                    msn = inp.readLine();
                    System.out.println("Recibí: " + msn);
                    if ("Modo PPI OK".equals(msn)) {
                        /*mensaje = "ConfPulsTx " + getConfPulso();
                        out.writeUTF(mensaje);
                        System.out.println("Envie: " + mensaje);
                        msn = inp.readLine();
                        System.out.println("Recibí: " + msn);
                        if ("ConfPulsTx OK".equals(msn)) {*/
                        mensaje = "ActivarPPI";
                        out.writeUTF(mensaje);
                        System.out.println("Envie: " + mensaje);
                        msn = inp.readLine();
                        System.out.println("Recibí: " + msn);
                        if ("Activar PPI OK".equals(msn)) {
                            for (int n = 1; n <= 100; n++) {
                                texto = "";
                                nDatos = 0;
                                mensaje = "DatosPPI " + n;
                                out.writeUTF(mensaje);
                                System.out.println("Envie: " + mensaje);
                                msn = inp.readLine();
                                System.out.println("Recibí: " + msn);
                                if ("Datos PPI OK".equals(msn)) {
                                    mensaje = "PPI1";
                                    out.writeUTF(mensaje);
                                    System.out.println("Envie: " + mensaje);
                                    msn = inp.readLine();
                                    System.out.println("Recibí: " + msn);
                                    charArray = msn.toCharArray();
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
                                        mensaje = "PPI2";
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
                                                    if (word != null) {
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
                                                System.out.println("Enviaré: " + texto);
                                                desp.setAnillo(texto, n);
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
                        /*} else {
                            System.out.println("Error: esperba <ConfPulsOK OK> y recibí <" + msn + ">, Compruebe la comunicación");
                        }*/
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
