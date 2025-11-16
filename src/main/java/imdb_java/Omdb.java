package imdb_java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import imdb_java.beans.OmdbFilm;

public class Omdb {

	public static void main(String[] args) {
		Omdb omdb = new Omdb();
		OmdbFilm film = omdb.cargar(3);
		System.out.println(film.getTitle());
		System.out.println(film.getYear());
		System.out.println(film.getDirector());
		System.out.println(film.getPlot());
		System.out.println(film.getPoster());
		System.out.println(film.getRatings());
		System.out.println(film.getImdbRating());
		System.out.println(film.getImdbVotes());
		System.out.println(film.getImdbID());
		System.out.println(film);
		System.out.println(omdb.loadImage(film.getPoster()));
	}

	public OmdbFilm cargar(Integer id) {
		// Perform database operations using the connection
		try {
			PreparedStatement stmt = MySQL.getCon()
					.prepareStatement("SELECT imdb_id FROM film WHERE id=? AND ifnull(imdb_id,'') <> ''");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return getOmdb(rs.getString("imdb_id"));
			} else {
				System.out.println("No se ha encontrado el id " + id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new OmdbFilm();
	}

	private OmdbFilm getOmdb(String imdb_id) {
		String apiKey = Config.getApiKey();
		if (apiKey == null || apiKey.isEmpty()) {
			System.err.println("OMDB API key not set. Define OMDB_API_KEY or create config.properties");
			return new OmdbFilm();
		}
        String urlString = "http://www.omdbapi.com/?i=" + imdb_id + "&plot=full&r=json&tomatoes=true&apikey=" + apiKey;
		try {
			URL url = new URI(urlString).toURL();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				return new OmdbFilm(response.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new OmdbFilm();
	}

	private byte[] loadImage(String url) {
		try {
			URL u = new URI(url).toURL();
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setRequestMethod("GET");
			// accept images
			conn.setRequestProperty("Accept", "image/*");
			conn.connect();
			return conn.getInputStream().readAllBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void updateDB(OmdbFilm omdbReg, byte[] poster, Integer id) {
		try {
			PreparedStatement stmt = MySQL.getCon()
					.prepareStatement("UPDATE film SET picture=?, omdb=? WHERE id=?");
			stmt.setBytes(1, poster);
			stmt.setString(2, omdbReg.toString());
			stmt.setInt(3, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
