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
import com.skilldistillery.filmquery.entities.InventoryItem;

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
		List<Film> films = null;

		try {
			// CONCAT('%' , ? , '%') is how I chose to escape single quotes within the query
			// The query outputs "WHERE title LIKE %?%" where ? is the user's input
			String sql = "SELECT * FROM film WHERE title LIKE CONCAT('%' , ? , '%') "
					+ " OR description LIKE CONCAT('%' , ? , '%')";
			Connection conn = DriverManager.getConnection(URL, user, pass);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, keyword);
			stmt.setString(2, keyword);

			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {  // If there is a match, initialize films, add first film
				films = new ArrayList<>();
				int filmId = rs.getInt("id");
				film = filmGenerator(rs);
				film.setActors(findActorsByFilmId(filmId));
				films.add(film);
			}
			
			while (rs.next()) {  // add the rest of the films
				int filmId = rs.getInt("id");
				film = filmGenerator(rs);
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
		List<Actor> actors = null;

		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT id, first_name, last_name FROM  actor "
					+ "JOIN film_actor ON actor.id = film_actor.actor_id WHERE film_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) { // if there are matches, initialize actors ArrayList and add first match
								// This ensures the list is null when no matches are found
				actors = new ArrayList<>();
				Actor actor = new Actor(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"));
				actors.add(actor);
			}
			while (rs.next()) { // add the rest of the matches
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

	@Override
	public String findCategoryByFilmId(int filmId) {
		String category = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT name FROM category JOIN film_category ON category.id = film_category.category_id "
					+ "JOIN film ON film.id = film_category.film_id WHERE film.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				category = rs.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return category;
	}

	@Override // Accepting a Film object as parameter rather than filmID makes it
				// easier to add the film title to the InventoryItem object for readability
	public List<InventoryItem> findInventoryItemByFilm(Film film) {
		List<InventoryItem> inventory = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT * FROM inventory_item JOIN film ON inventory_item.film_id = film.id WHERE film.title = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, film.getTitle());
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) { // If there is a match, initialize list, generate first item
				inventory = new ArrayList<>();
				InventoryItem item = inventoryItemGenerator(rs, film);
				inventory.add(item);
			}
			while (rs.next()) { // generate the rest of the items
				InventoryItem item = inventoryItemGenerator(rs, film);
				inventory.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return inventory;
	}

	
	private InventoryItem inventoryItemGenerator(ResultSet rs, Film film) {
		InventoryItem item = null;
		try {
			item = new InventoryItem();
			item.setFilmTitle(film.getTitle());
			item.setId(rs.getInt("id"));
			item.setFilmId(rs.getInt("film_id"));
			item.setStoreId(rs.getInt("store_id"));
			item.setMediaCondition(rs.getString("media_condition"));
			item.setLastUpdate(rs.getString("last_update"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return item;
	}
	
	private Film filmGenerator(ResultSet rs) {
		Film film = null;
		try {
			film = new Film(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
					rs.getShort("release_year"), rs.getInt("language_id"), rs.getInt("rental_duration"),
					rs.getDouble("rental_rate"), rs.getInt("length"), rs.getDouble("replacement_cost"),
					rs.getString("rating"), rs.getString("special_features"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}
}
