package imdb_java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MySQL {

	private static final Logger _log = Logger.getLogger(MySQL.class.getName());
	private static Connection con;

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			_log.severe(e.getMessage());
		}

		String server = Config.getPrefs("mysql_server",true);
		String port = Config.getPrefs("mysql_port",true);
		String schema = Config.getPrefs("mysql_scheme",true);
		String user = Config.getPrefs("mysql_user",true);
		String pass = Config.getPrefs("mysql_password",true);

		try {
			con = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s",server,port,schema), user, pass);
		} catch (SQLException e) {
			Config.clearPrefs();
			_log.severe(e.getMessage());
		}
	}
	
	public static ResultSet _select(String sql, Object...args) {
		try {
			PreparedStatement stm = getStatement(sql, args);
			if (stm != null) {
				return stm.executeQuery();
			}
		} catch (SQLException e) {
			_log.severe(e.getMessage());
		}
		return null;
	}
	
	public static void _execute(String sql, Object...args) {
		try {
			PreparedStatement stm = getStatement(sql, args);
			if (stm != null) {
				stm.executeUpdate();
			}
		} catch (SQLException e) {
			_log.severe(e.getMessage());
		}
	}
	
	private static PreparedStatement getStatement(String sql, Object...args) throws SQLException {
		if (con == null) { return null; }
		PreparedStatement stm = con.prepareStatement(sql);
		int index = 1;
		for(Object param:args) {
			if (param instanceof String) {
				stm.setString(index,(String)param);
			} else if (Integer.class.isInstance(param)) {
				stm.setInt(index, (Integer)param);
			} else if (param instanceof byte[]) {
				stm.setBytes(index, (byte[])param);
			}
			index++;
		}
		return stm;
	}
}
