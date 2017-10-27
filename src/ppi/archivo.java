    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppi;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author juan
 */
public class archivo {

    String info = "";

    public String leerTxtLine(String dir) {                                     //lee lo que haya en un archivo txt, recibe como parametros la direccion tipo String y devuelve el String del contenido en una sola linea
        try {
            BufferedReader bf = new BufferedReader(new FileReader(dir));
            String temp = "";
            String bfRead;
            while ((bfRead = bf.readLine()) != null) {
                temp += bfRead;
            }
            info = temp;
        } catch (Exception e) {
            System.err.println("SOY READ LINE: No se encontro el archivo en " + dir);
        }
        return info;
    }

    public String leerTxtLine(String dir, int lim) {                 //lee lo que haya en un archivo txt, recibe como parametros la direccion tipo String y devuelve el String del contenido en una sola linea
        try {
            BufferedReader bf = new BufferedReader(new FileReader(dir));
            String temp = "";
            String bfRead;
            while ((bfRead = bf.readLine()) != null && lim > 0) {
                temp += bfRead;
                lim--;
            }
            info = temp;
        } catch (Exception e) {
            System.err.println("SOY READ LINE con lim: No se encontro el archivo en " + dir);
        }
        return info;
    }

    public String leerTxtLineReverse(String dir, int lim) {                 //lee lo que haya en un archivo txt, recibe como parametros la direccion tipo String y devuelve el String del contenido en una sola linea 
        String temp = "";                                                   //de manera inversa es decir del la ultima fila hasta la primera, esta función fue creada para graficar el PPI a mayor resolución
        try {

            BufferedReader bf = new BufferedReader(new FileReader(dir));
            //String temp = "";
            String bfRead;
            String[] str = new String[lim];
            int n = lim;
            while ((bfRead = bf.readLine()) != null && n >= 0) {
                n--;
                str[n] = bfRead;
            }
            for (int x = 0; x < lim; x++) {
                temp += str[x];
            }
            info = temp;
        } catch (Exception e) {
            System.err.println("SOY READ LINE REVERSE: No se encontro el archivo en " + dir);
        }
        return info;
    }

    /*public String leerTxtLineReverseN(String dir, int lim) {                    //lee lo que haya en un archivo txt, recibe como parametros la direccion tipo String y devuelve el String del contenido en una sola linea 
        String temp = "";                                                       //de manera inversa es decir del la ultima fila hasta la primera, esta función fue creada para graficar el PPI a mayor resolución
        String box = "";                                                        //a diferencia de la funcion anterior este romaliza cada fila para que el numero mas bajo sea restado, 
        int num = 0;
        try {                                                                   //y de esta manera realzar el objetivo.
            BufferedReader bf = new BufferedReader(new FileReader(dir));
            //String temp = "";
            String bfRead;
            String[] str = new String[lim];
            int n = lim;
            while ((bfRead = bf.readLine()) != null && n >= 0) {
                n--;
                str[n] = bfRead;
                char[] charArray = str[n].toCharArray();
                for (char temp : charArray) {
                    if (!(temp == ',') && !(temp == ';')) {
                        box += "" + temp;
                    } else if (temp == ',' || temp == ';') {
                        num = Integer.parseInt(box);
                    }
                }
            }
            for (int x = 0; x < lim; x++) {
                temp += str[x];
            }
            info = temp;
        } catch (Exception e) {
            System.err.println("SOY READ LINE REVERSE: No se encontro el archivo en " + dir);
        }
        return info;
    }*/
    public String leerTxt(String dir) {                                         //lee lo que haya en un archivo txt, recibe como parametros la direccion tipo String y devuelve el String del contenido
        try {
            BufferedReader bf = new BufferedReader(new FileReader(dir));
            String temp = "";
            String bfRead;
            int lim = 100;
            while (((bfRead = bf.readLine()) != null) && (lim > 0)) {
                temp += bfRead;
                temp += "\n";
                lim--;
            }
            info = temp;
        } catch (Exception e) {
            System.err.println("SOY READ: No se encontro el archivo en " + dir);
        }
        return info;
    }

    public void escribirTxtLine(String dir, String texto) throws IOException {      //escribe un texto en una archivo existente o lo crea, recibe como parametro la direccion del texto y el texto ambos tipo String
        BufferedWriter bw;
        try {
            File archivo = new File(dir);
            if (archivo.exists()) {
                info = leerTxt(dir);
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write(texto + "\n" + info);
            } else {
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write(texto + "\n");
            }
            bw.close();
        } catch (Exception e) {
            System.err.println("SOY WRITE LINE: hay un error ");
        }
    }

    public void escribirTxt(String dir, String texto) throws IOException {      //escribe un texto en una archivo existente o lo crea, recibe como parametro la direccion del texto y el texto ambos tipo String
        BufferedWriter bw;
        try {
            File archivo = new File(dir);
            bw = new BufferedWriter(new FileWriter(archivo));
            bw.write(texto);
            bw.close();
        } catch (Exception e) {
            System.err.println("SOY WRITE hay un error ");
        }
    }
    
    public void escribirTxt(String dir, int n) throws IOException {      //escribe un texto en una archivo existente o lo crea, recibe como parametro la direccion del texto y el texto ambos tipo String
        BufferedWriter bw;
        try {
            File archivo = new File(dir);
            bw = new BufferedWriter(new FileWriter(archivo));
            bw.write(String.valueOf(n));
            bw.close();
        } catch (Exception e) {
            System.err.println("SOY WRITE hay un error ");
        }
    }

    public void save(String dir) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String date = sdf.format(cal.getTime());
        BufferedWriter bw;
        try {
            File archivo = new File("resource/LfData_" + date + ".txt");
            info = leerTxt(dir);
            bw = new BufferedWriter(new FileWriter(archivo));
            bw.write(info + "\n");
            bw.close();
        } catch (Exception e) {
            System.err.println("SOY SAVE: No se encontro el archivo " + dir);
        }
    }

    public void escribirTxtLine(String dir, String texto, int nLine) throws IOException {
        char[] charArray = leerTxtLine(dir).toCharArray();
        File archivo = new File(dir);
        int lim = 1;
        BufferedWriter bw;

        try {
            bw = new BufferedWriter(new FileWriter(archivo));
            try {
                String info = "";

                for (char temp : charArray) {
                    if (nLine == lim && temp == ';') {
                        info += texto;
                    }
                    if (nLine != lim) {
                        info += temp;
                    }
                    if (temp == ';') {
                        lim++;
                        info += "\n";
                    }
                }
                bw.write(info);
                bw.close();
            } catch (Exception e) {
                System.err.println("SOY READ: No se encontro el archivo en " + dir);
            }
        } catch (Exception e) {
            System.err.println("SOY WRITE LINE: hay un error ");
        }
    }

    public void resetLine(String dir, int nLine, int longLine) throws IOException {
        char[] charArray = leerTxtLine(dir).toCharArray();
        File archivo = new File(dir);
        int lim = 1;
        BufferedWriter bw;

        try {
            bw = new BufferedWriter(new FileWriter(archivo));
            try {
                String info = "";

                for (char temp : charArray) {
                    if (nLine == lim && temp == ';') {
                        for (int n = 0; n < longLine; n++) {
                            if (n < longLine-1) {
                                info += "155,";
                            } else {
                                info += "0;";
                            }
                        }
                    }
                    if (nLine != lim) {
                        info += temp;
                    }
                    if (temp == ';') {
                        lim++;
                        info += "\n";
                    }
                }
                bw.write(info);
                bw.close();
            } catch (Exception e) {
                System.err.println("SOY READ: No se encontro el archivo en " + dir);
            }
        } catch (Exception e) {
            System.err.println("SOY WRITE LINE: hay un error ");
        }
    }

    public int getLim(String dir) {
        int n = 0;
        try {
            BufferedReader bf = new BufferedReader(new FileReader(dir));

            while ((bf.readLine()) != null) {
                n++;
            }
        } catch (Exception e) {
            System.err.println("SOY GET LIM: No se encontro el archivo en " + dir);
        }
        return n;
    }
}
