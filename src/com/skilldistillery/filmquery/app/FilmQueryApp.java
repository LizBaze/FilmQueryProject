package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) throws SQLException {
		FilmQueryApp app = new FilmQueryApp();
		app.test();
		app.launch();
	}

	private void test() throws SQLException {
		Film film = db.findFilmById(1);
		Actor actor = db.findActorById(1);
		List<Actor> actors = db.findActorsByFilmId(1);
		System.out.println(film);
		System.out.println(actor);
		System.out.println(actors);
	}

	private void launch() {
		Scanner input = new Scanner(System.in);

		startUserInterface(input);

		input.close();
	}

	private void startUserInterface(Scanner input) {

		System.out.println("Welcome to the sdvid database management system");

		boolean keepGoing = true;
		do {
			printMenu();
			int userChoice = 0;
			try {
				userChoice = input.nextInt();
				input.nextLine();
				switch (userChoice) {
				case 1:
					System.out.println("Enter the id to search");
					Film filmById = db.findFilmById(input.nextInt());
					input.nextLine();
					if (filmById != null) {
						filmPrinter(filmById);
					} else {
						System.out.println("No film found with that ID");
					}
					break;
				case 2:
					System.out.println("Enter your keyword:");
					List<Film> films = db.findFilmByKeyword(input.nextLine());
					int numFilmsFound = 0;
					if (!films.isEmpty()) {
						for (Film film : films) {
							filmPrinter(film);
							numFilmsFound++;
						}
						System.out.println("Total films found: " + numFilmsFound);
					} else {
						System.out.println("No films found");
					}
					break;
				case 3:
					System.out.println("Goodbye");
					keepGoing = false;
					break;
				default:
					System.out.println("Invalid entry");
				}
				
			} catch (InputMismatchException e) {
				System.out.println("Invalid selection, please try again");
				input.nextLine();
			}

		} while (keepGoing);

	}

	private void printMenu() {
		System.out.println("1) Look up a film by ID");
		System.out.println("2) Look up a film by keyword search");
		System.out.println("3) Exit");
	}

	// Prints the title, language, and all actors associated with the film
	private void filmPrinter(Film film) {
		System.out.println(film);
		System.out.println("\t Language: " + db.findLanguageByFilm(film));
		StringBuilder actorsOutput = new StringBuilder("\t Actors: ");

		List<Actor> actors = db.findActorsByFilmId(film.getId());
		Iterator<Actor> it = actors.iterator();

		for (int i = 0; i < actors.size(); i++) {
			actorsOutput.append(it.next());
			if (it.hasNext()) {
				actorsOutput.append(", ");
			}
		}
		System.out.println(actorsOutput);
	}

}
