package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=US/Mountain";
	private static final String user = "student";
	private static final String pass = "student";

	@Override
	public Film findFilmById(int filmId) {
		Film film = null;

		try {
			String sql = "SELECT * FROM film WHERE id = ?";
			Connection conn = DriverManager.getConnection(URL, user, pass);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				film = new Film(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
						rs.getShort("release_year"), rs.getInt("language_id"), rs.getInt("rental_duration"),
						rs.getDouble("rental_rate"), rs.getInt("length"), rs.getDouble("replacement_cost"),
						rs.getString("rating"), rs.getString("special_features"));

				film.setActors(findActorsByFilmId(filmId));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return film;
	}

	public List<Film> findFilmByKeyword(String keyword) {
		Film film = null;
		List<Film> films = new ArrayList<>();

		try {
			String sql = "SELECT * FROM film WHERE title LIKE CONCAT('%' , ? , '%') "
					+ " OR description LIKE CONCAT('%' , ? , '%')";
			Connection conn = DriverManager.getConnection(URL, user, pass);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, keyword);
			stmt.setString(2, keyword);
			

			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int filmId = rs.getInt("id");
				film = new Film(filmId, rs.getString("title"), rs.getString("description"), rs.getShort("release_year"),
						rs.getInt("language_id"), rs.getInt("rental_duration"), rs.getDouble("rental_rate"),
						rs.getInt("length"), rs.getDouble("replacement_cost"), rs.getString("rating"),
						rs.getString("special_features"));

				film.setActors(findActorsByFilmId(filmId));
				films.add(film);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return films;
	}

	public Actor findActorById(int actorId) {
		Actor actor = null;
		try {
			// ...
			String sql = "SELECT id, first_name, last_name FROM actor WHERE id = ?";
			Connection conn = DriverManager.getConnection(URL, user, pass);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet actorResult = stmt.executeQuery();
			if (actorResult.next()) {
				actor = new Actor(); // Create the object
				// Here is our mapping of query columns to our object fields:
				actor.setId(actorResult.getInt(1));
				actor.setFirstName(actorResult.getString(2));
				actor.setLastName(actorResult.getString(3));
//			actor.setFilms(findFilmsByActorId(actorId)); // An Actor has Films
			}
			// ...
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actor;
	}

//	public List<Film> findFilmsByActorId(int actorId) {
//		List<Film> films = new ArrayList<>();
//		try {
//			Connection conn = DriverManager.getConnection(URL, user, pass);
//			String sql = "SELECT id, title, description, release_year, language_id, rental_duration, ";
//			sql += " rental_rate, length, replacement_cost, rating, special_features "
//					+ " FROM film JOIN film_actor ON film.id = film_actor.film_id " + " WHERE actor_id = ?";
//			PreparedStatement stmt = conn.prepareStatement(sql);
//			stmt.setInt(1, actorId);
//			ResultSet rs = stmt.executeQuery();
//			while (rs.next()) {
//				int filmId = rs.getInt(1);
//				String title = rs.getString(2);
//				String desc = rs.getString(3);
//				short releaseYear = rs.getShort(4);
//				int langId = rs.getInt(5);
//				int rentDur = rs.getInt(6);
//				double rate = rs.getDouble(7);
//				int length = rs.getInt(8);
//				double repCost = rs.getDouble(9);
//				String rating = rs.getString(10);
//				String features = rs.getString(11);
//				Film film = new Film(filmId, title, desc, releaseYear, langId, rentDur, rate, length, repCost, rating,
//						features);
//				films.add(film);
//			}
//			rs.close();
//			stmt.close();
//			conn.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return films;
//	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actors = new ArrayList<>();

		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT id, first_name, last_name FROM  actor "
					+ "JOIN film_actor ON actor.id = film_actor.actor_id WHERE film_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Actor actor = new Actor(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"));
				actors.add(actor);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actors;
	}
	
	public String findLanguageByFilm(Film film) {
		String language = null;
		try {
		Connection conn = DriverManager.getConnection(URL, user, pass);
		String sql = "SELECT name FROM language JOIN film ON language.id = film.language_id";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		
		if (rs.next()) {
			language = rs.getString("name");
		}
		
		conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return language;
	}

}
