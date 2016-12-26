package imdb_java;

import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

public class Config {
	
	private static final String PACKAGE_NAME = "imdb_java";
	private static final Logger _log = Logger.getLogger(Config.class.getName());
	private static final Preferences _pref = Preferences.userRoot().node(PACKAGE_NAME);
	
	public final static String WORDLIST = "word_list";
	public final static String EXTENSIONLIST = "extension_list";
	public final static String URLIMDB = "url_imdb";
	public final static String URLSUBDIVX = "url_subdivx";
	public final static String LASTFOLDER = "last_folder";
	public final static String MRUFOLDERS = "mru_folders";
    
    static {
        if (Config.getPrefs(WORDLIST,false) == null) {
            Config.setPrefs(WORDLIST, "720p,xvid,ac3,dvdscr,mp3,hdrip,vodrip,brrip,480p,1080p,bluray,x264,bdrip,r5,rc,proper,ws,mp4,nfo,dvdrip,hdscr,spanish,dx50,brscr,dual,h264,vhsrip,divx,dvbrip,aac,wma,dts,scr,r6,vhsscr,webrip,hdtv,webdl");
        }
        if (Config.getPrefs(EXTENSIONLIST, false) == null) {
            Config.setPrefs(EXTENSIONLIST, ".m4v,.mkv,.avi,.mpg,.wmv,.mp4,.mpv,.mpeg,.vob");
        }
        if (Config.getPrefs(URLIMDB, false) == null) {
            Config.setPrefs(URLIMDB, "http://www.imdb.com/find?q=TESTSEARCH&amp;s=all");
        }
        if (Config.getPrefs(URLSUBDIVX, false) == null) {
            Config.setPrefs(URLSUBDIVX, "http://www.subdivx.com/index.php?buscar=TESTSEARCH&amp;accion=5&amp;masdesc=&amp;subtitulos=1&amp;realiza_b=1");
        }
    }

	public static String getPrefs(String key) {
        return getPrefs(key, false, true);
    }
	
	public static String getPrefs(String key, boolean alow_input) {
	    return getPrefs(key, alow_input, true);
	}

	public static String getPrefs(String key, boolean alow_input, boolean save) {
		String value = _pref.get(key, null);
		if (value == null && alow_input) {
			//TODO java.awt.GraphicsEnvironment.isHeadless() to check console input
			/*System.out.println(String.format("Enter %s value:",key));
			try(BufferedReader io = new BufferedReader(new InputStreamReader(System.in))) {
			    value = io.readLine();				
			} catch (Exception e) {
				_log.severe(e.getMessage());
			}*/
			value = JOptionPane.showInputDialog(null, "Enter [" + key + "] value:", "Configuracion", JOptionPane.QUESTION_MESSAGE);
			if (value != null) {
			    _pref.put(key, value);    
			}
		} else if (!save) {
		    _pref.remove(key);
		}
		return value;
	}
	
	public static byte[] getPrefsByte(String key) {
        return _pref.getByteArray(key, null);
    }
	
	public static void setPrefs(String key, Object value) {
	    if (value instanceof String) {
	        _pref.put(key, (String)value);    
	    } else if (value instanceof byte[]) {
	        _pref.putByteArray(key, (byte[])value);
	    }	    
	}
    
	public static void clearPrefs() {
		try {
			_pref.clear();
		} catch (BackingStoreException e) {
			_log.severe(e.getMessage());
		}
	}
    
}
