package imdb_java;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import imdb_java.gui.mainWin;

public class Principal {

    private static final Logger _log = Logger.getLogger(Principal.class.getName());

    private ArrayList<String> rutasInicio = new ArrayList<>();
    private ArrayList<String> rutasFinal = new ArrayList<>();
    private HashMap<String, String> map = new HashMap<>();
    private MyTableModel table = new MyTableModel();
    private mainWin gui = new mainWin();

    public static void main(String[] args) {

        Principal p = new Principal();

        // importPictureFromOmdb();

        /*
         * try { System.out.println(URLDecoder.decode(String.format(
         * "https://pinternet3.lacaixa.es/apl/formularios/altaClienteImaginOnline/htcvideo.videocall_es.html?REF_ori_vid=%s%%26REF_temp_vid=%s", "12345", "12345"), "UTF-8")); } catch
         * (UnsupportedEncodingException e) { e.printStackTrace(); }
         */

         //p.run();

        try {
        	
            p.CargarRutasMRU();
            p.FiltraYPintaGrid(true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Principal() {
		gui.setTableModel(table);
	}
    
    private void terminalDemo() throws Exception {
        Terminal t = new DefaultTerminalFactory().createTerminal();
        Screen scr = new TerminalScreen(t);
        scr.startScreen();
        Panel p = new Panel(new LinearLayout(Direction.HORIZONTAL));
        p.addComponent(new Label("Hi!"));
        p.addComponent(new Label("Hi!"));
        p.addComponent(new Label("Hi!"));
        p.addComponent(new Button("botón"));
        Window w = new BasicWindow("demo");
        w.setComponent(p);
        MultiWindowTextGUI gui = new MultiWindowTextGUI(scr);
        gui.addWindowAndWait(w);
    }

    private void run() {
    }

    /**
     * Returns de displayname of de drive
     * 
     * @param drive
     * @return
     */
    private String path2deviceName(String ruta) {
        String raiz = ruta.split("[\\\\/]")[0];
        return ruta.replaceFirst(raiz, map.get(raiz));
    }

    /**
     * Returns de letter of the drive
     * 
     * @param drive
     * @return
     */
    private String path2deviceLetter(String ruta) {
        String raiz = ruta.split("[\\\\/]")[0];
        return ruta.replaceFirst(raiz, map.get(raiz) != null ? map.get(raiz) : raiz);
    }

    /**
     * Imports from omdb picture where is null
     */
    private static void importPictureFromOmdb() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        try {

            ResultSet res = MySQL._select("select id,imdb_id from film where picture is null and imdb_id like 'tt%'");
            while (res.next()) {
                JSONObject json = new JSONObject(
                        IOUtils.toString(new URL("http://www.omdbapi.com/?i=" + res.getString("imdb_id") + "&plot=short&r=json"), Charset.forName("UTF-8")));
                System.out.println(json.get("Poster"));
                InputStream io = new URL(json.getString("Poster")).openStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                byte[] b = new byte[2048];
                while (io.read(b) != -1) {
                    out.write(b);
                }
                io.close();
                out.close();
                System.out.println(out);

                PreparedStatement st = MySQL.getCon().prepareStatement("UPDATE film SET picture=? WHERE id=?");
                st.setBytes(1, out.toByteArray());
                st.setInt(2, res.getInt("id"));
                st.execute();

            }

        } catch (Exception e) {
            _log.severe(e.getMessage());
        }

    }

    /*********************************************************/

    /**
     * Carga y valida la lista de ultimos directorios utilizados
     */
    private void CargarRutasMRU() {

        // Si no existe ya pedimos el directorio desde el que se cargará el listado de películas
        String last_folder = Config.getPrefs(Config.LASTFOLDER);

        if (last_folder == null || !Files.isDirectory(Paths.get(last_folder))) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                last_folder = fc.getSelectedFile().getAbsolutePath();
                Config.setPrefs(Config.LASTFOLDER, last_folder);
            }
        }

        // Cargamos como MRU si no existen el LastFolder y todos las distintas rutas de la base de datos
        List<String> mru_folders = new ArrayList<>();
        
        /*  Cargamos los MRU guardados
        byte[] ba = Config.getPrefsByte(Config.MRUFOLDERS);
        if (ba != null) {
            mru_folders = (List<String>)Util.byteArray2Object(ba);
        }
        */
        
        if (!mru_folders.contains(last_folder)) {
            mru_folders.add(last_folder);
        }
                
        //Hacemos un mapa con los nombres de volumen y sus rutas        
        for (File root : File.listRoots()) {
            map.put(Pattern.compile("\\s\\(.+\\)").matcher(FileSystemView.getFileSystemView().getSystemDisplayName(root)).replaceAll(""), root.getPath()+File.separator);
        }
                
        try {
            ResultSet res = MySQL._select("SELECT DISTINCT ruta FROM `film` WHERE ruta IS NOT NULL");
            while (res.next()) {
                String ruta = res.getString("ruta");   
                String raiz = ruta.split("[\\\\/]")[0];
                if (map.containsKey(raiz)) {
                    String ruta_final = ruta.replaceFirst(raiz, map.get(raiz));
                    if (!mru_folders.contains(ruta_final) && Files.isDirectory(Paths.get(ruta_final))) {
                        mru_folders.add(ruta_final);
                        _log.info(ruta_final + " added to mru");                        
                    }                    
                }
            }
        } catch (SQLException e) { 
            e.printStackTrace();
        }
        
        //Guarda el MRU obtenido
        //Config.setPrefs(Config.MRUFOLDERS, Util.object2ByteArray(mru_folders));
        
    }
    
    /**
     * Actualiza el grid, mostrando los ficheros según las opciones seleccionadas
     * @param bRecarga
     */
    private void FiltraYPintaGrid(boolean bRecarga) {
        double media = CalculaMediaRating();
        if (bRecarga) {
        	CargarTabla(false);
        }
    }
    
    /**
     * Devuelve una lista de ficheros filtrados por extensión de los directorios indicados
     * @param sDirs Lista de directorios
     * @return
     */
    private String[] CargaFiles(String[] sDirs) {
        List<String> files = new ArrayList<>();
        List<String> extensions = Arrays.asList(Config.getPrefs(Config.EXTENSIONLIST).split(","));
        for (String d : sDirs) {
            for (File f : new File(d).listFiles()) {
                if (extensions.contains(Util.getExtension(f))) {
                    
                }
            }
        }
        /**
         * Return (From d In sDirs From f In Directory.GetFiles(d) Where My.Settings.ExtensionList.Split(","c).Contains(Path.GetExtension(f)) 
         *         Select Replace(f, Path.GetPathRoot(f), DriveInfo.GetDrives().First(Function(i) i.Name = Path.GetPathRoot(f)).VolumeLabel & Path.DirectorySeparatorChar)).ToArray
         */
        return null;
    }
    
    /**
     * Carga la tabla de datos, realiza el filtrado del directorio actual y actualiza el grid
     * @param bVerTodo
     */
    private void CargarTabla(boolean bVerTodo) {
        String sql = "SELECT * FROM vw_film";

        //Añade los registros (ficheros) que no existen en la base de datos
        if (bVerTodo) {
            sql += " WHERE 1=1";
        } else {
            
        }
        /**
         * Dim sql As String = "SELECT * FROM vw_film"
            'Añade los registros (ficheros) que no existen en la base de datos
            If (uxchkVerTodo.Checked) Then
                sql &= " WHERE 1=1"
            Else
                Dim myFiles As String() = CargaFiles(CType(IIf(uxchkVerMRU.Checked, My.Settings.MRU_Folders.Cast(Of String).ToArray, {My.Settings.LastFolder}), String())).Select(Function(e) QuitaComilla(e)).ToArray
                If (myFiles.Length > 0) Then
                    Dim myTableTmp As DataTable = baseDatos.Select("SELECT id,filename,ruta FROM film WHERE filename IN ('" & If(myFiles.Length = 1, Path.GetFileName(myFiles(0)), myFiles.Aggregate(Function(a, b) Path.GetFileName(a) & "','" & Path.GetFileName(b))) & "')")
                    For Each myFile As String In myFiles
                        Dim myRow() As DataRow = myTableTmp.Select("filename='" & Path.GetFileName(myFile) & "'")
                        If (myRow.Length = 0) Then
                            baseDatos.ExecuteNonQuery("INSERT INTO film (filename,name,ruta,fecha_alta) VALUES ('" & Path.GetFileName(myFile) & "','" & SplitWords(Path.GetFileNameWithoutExtension(myFile)) & "','" & Path.GetDirectoryName(myFile).Replace("\", "\\") & "',NOW())")
                        ElseIf (IsDBNull(myRow(0)("ruta")) OrElse myRow(0)("ruta").ToString <> Path.GetDirectoryName(myFile)) Then
                            baseDatos.ExecuteNonQuery("UPDATE film SET ruta='" & Path.GetDirectoryName(myFile).Replace("\", "\\") & "' WHERE id=" & myRow(0)("id").ToString)
                        End If
                    Next
                    sql &= " WHERE filename IN ('" & If(myFiles.Length = 1, Path.GetFileName(myFiles(0)), myFiles.Aggregate(Function(a, b) Path.GetFileName(a) & "','" & Path.GetFileName(b))) & "')"
                Else
                    sql &= " WHERE 1<>1"
                End If
            End If
            If (uxchkPendientesIMDB.CheckState = CheckState.Checked) Then
                sql &= " AND (LENGTH(imdb_id) = 0 OR imdb_rating IS NULL OR imdb_rating = 0)"
            ElseIf (uxchkPendientesIMDB.CheckState = CheckState.Indeterminate) Then
                sql &= " AND NOT (LENGTH(imdb_id) = 0 OR imdb_rating IS NULL OR imdb_rating = 0)"
            End If
            If (uxchkPendientesOMDB.CheckState = CheckState.Checked) Then
                sql &= " AND tiene_omdb = 0"
            ElseIf (uxchkPendientesOMDB.CheckState = CheckState.Indeterminate) Then
                sql &= " AND tiene_omdb = 1"
            End If
            If (uxchkDuplicados.Checked) Then sql &= " AND duplicados > 1"
            If (uxtxtBuscar.Text.Length > 0) Then sql &= " AND CONCAT(filename ,' ',name,' ',IFNULL(imdb_id,'')) LIKE '%" & uxtxtBuscar.Text & "%'"
            Return baseDatos.Select(sql & " ORDER BY imdb_rating DESC, imdb_ratingcount DESC, Id")
         */
    }
    
    /**
     * Calcula la media de valoraciones de los registros visibles del grid
     * @return Valorarción media
     */
    private double CalculaMediaRating() {
        /*
         * Dim numRatings As Decimal = 0
        Dim sumRatings As Decimal = 0
        Dim media As Decimal = 0
        Try
            If (uxgrd.DataSource IsNot Nothing) Then
                For Each row As DataRow In TryCast(uxgrd.DataSource, DataTable).Rows
                    If (CInt(row("duplicados")) > 0 AndAlso IsNumeric(row(uxColumnRating.DataPropertyName)) AndAlso CDec(row(uxColumnRating.DataPropertyName)) > 0) Then
                        numRatings += CDec(1 / CInt(row("duplicados")))
                        sumRatings += CDec(row(uxColumnRating.DataPropertyName)) / CInt(row("duplicados"))
                    End If
                Next
                media = CDec(IIf(numRatings > 0, sumRatings / numRatings, 0))
            End If
        Catch ex As Exception
        End Try
        Return media
         */
        float numRatings = 0;
        float sumRatings = 0;
        float media = 0;
        try {
            MySQL._select("select * from vw_film_totales" + getFilters());
        } catch (Exception e) {
            // TODO: handle exception
        }
        return media;
        
    }
    
    private String getFilters() {
        return "";
    }
    
    /*********************************************************/
    
}
