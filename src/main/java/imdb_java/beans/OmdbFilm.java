package imdb_java.beans;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class OmdbFilm {
	
	private JsonObject json;

	public OmdbFilm() {
	}
	
	public OmdbFilm(String json) {
		if (json != null) {
			this.json = new Gson().fromJson(json, JsonObject.class);
		}
	}

	public String getTitle() {
		return json.get("Title").getAsString();
	}

	public String getYear() {
		return json.get("Year").getAsString();
	}

	public String getRated() {
		return json.get("Rated").getAsString();
	}

	public String getReleased() {
		return json.get("Released").getAsString();
	}

	public String getRuntime() {
		return json.get("Runtime").getAsString();
	}

	public String getGenre() {
		return json.get("Genre").getAsString();
	}

	public String getDirector() {
		return json.get("Director").getAsString();
	}

	public String getWriter() {
		return json.get("Writer").getAsString();
	}

	public String getActors() {
		return json.get("Actors").getAsString();
	}

	public String getPlot() {
		return json.get("Plot").getAsString();
	}

	public String getLanguage() {
		return json.get("Language").getAsString();
	}

	public String getCountry() {
		return json.get("Country").getAsString();
	}

	public String getPoster() {
		return json.get("Poster").getAsString();
	}

	public String getRatings() {
		return json.get("Ratings").toString();
	}

	public String getImdbRating() {
		return json.get("imdbRating").getAsString();
	}

	public String getImdbVotes() {
		return json.get("imdbVotes").getAsString();
	}

	public String getImdbID() {
		return json.get("imdbID").getAsString();
	}
	
	@Override
	public String toString() {
		return json.toString();
	}
}
