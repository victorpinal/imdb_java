package imdb_java;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Util {    
    
    /**
     * Elimina del nombre de fichero pasado una lista de palabras configurada
     * @param s Cadena con el nombre del fichero
     * @return Cadena con el nombre filtrado
     */
    public static String SplitWords(String s) {
        List<String> words = Arrays.asList(s.replaceAll("^(\\w+([\\.\\s]?\\w+)*).*", "$1").split("\\W+"));
        words.removeAll(Arrays.asList(Config.getPrefs(Config.WORDLIST).split(",")));
        return String.join(" ", words);
    }
    /*
    Private Function SplitWords(ByVal s As String) As String
        Try
            Dim str As String = Regex.Replace(s, "^(\w+([\.\s]?\w+)*).*", "$1")
            Return Trim(Regex.Split(str, "\W+").Where(Function(e) Not My.Settings.WordsList.Split(","c).Contains(e.ToLower) And Not IsNumeric(e)).Aggregate(Function(e, f) e & " " & f))
        Catch ex As Exception
            Errores("SplitWords: " & ex.Message)
            Return s
        End Try
    End Function
    */
    
    public static String whatsMyIp() {
        try { // buscamos la ip externa del equipo
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            return in.readLine(); // you get the IP as a String
        } catch (IOException e) {
            e.printStackTrace();
            return "127.0.0.1";
        }
    }

    public static String whatsMyHostname() {
        try {
            Process proc = Runtime.getRuntime().exec("hostname");
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), StandardCharsets.UTF_8));
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "localhost";
        }
    }
    
    public static byte[] object2ByteArray(Object arg) {  
        try {
            ByteArrayOutputStream bi = new ByteArrayOutputStream();
            ObjectOutputStream oi = new ObjectOutputStream(bi);
            oi.writeObject(arg);
            return bi.toByteArray();
        } catch (Exception e) {
            return null;
        }        
    }
    
    public static Object byteArray2Object(byte[] arg) {        
        try {
            ByteArrayInputStream bi = new ByteArrayInputStream(arg);
            ObjectInputStream oi = new ObjectInputStream(bi);
            return oi.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }        
    }
    
    public static String getExtension(File f) {
        try {
            return f.getName().substring(f.getName().lastIndexOf(".") + 1);   
        } catch (Exception e) {
            return null;
        }        
    }    

}
