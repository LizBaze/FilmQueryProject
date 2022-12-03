package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.ArrayList;
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
//		app.test();
		app.launch();
	}

//	private void test() throws SQLException {
//		Film film = db.findFilmById(1);
//		Actor actor = db.findActorById(1);
//		List<Actor> actors = db.findActorsByFilmId(1);
//		System.out.println(film);
//		System.out.println(actor);
//		System.out.println(actors);
//	}

	private void launch() {
		Scanner input = new Scanner(System.in);

		System.out.println("Welcome to the sdvid database management system");
		startUserInterface(input);

		input.close();
	}

	private void startUserInterface(Scanner input) {

		System.out.println("**** Main Menu ****");
		boolean keepGoing = true;
		do {
			printMenu();
			int userChoice = 0;
			try {
				userChoice = input.nextInt();
				input.nextLine();
				switch (userChoice) {
				case 1:
					List<Film> filmsbyID = idSearch(input); 	// Store list of matching films for re-use in filmSearchSubMenu
					filmSearchSubMenu(input, filmsbyID);
					break;
				case 2:
					List<Film> filmsByKeyWord = keywordSearch(input); // Store list of matching films for re-use in filmSearchSubMenu
					filmSearchSubMenu(input, filmsByKeyWord);
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
			} // End of if statement
		} // End of for loop
		System.out.println(actorsOutput);
	} // End of filmPrinter beginning line 86

	// Allow user to input keyword, search films' 'title' and 'description' fields,
	// call filmPrinter() for each, keep track of total films found and output that
	// as well
	private List<Film> keywordSearch(Scanner input) {
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
		return films;
	}

	// Allow user to input film ID, search films table by ID,
	// call filmPrinter to print the film
	private List<Film> idSearch(Scanner input) {
		System.out.println("Enter the id to search");
		Film filmById = db.findFilmById(input.nextInt());
		input.nextLine();
		if (filmById != null) {
			filmPrinter(filmById);
		} else {
			System.out.println("No film found with that ID");
		}
		List<Film> films = new ArrayList<>();
		films.add(filmById);
		return films;
	}
	
		// Give the option to see all database fields for the list of films passed or return to the main menu
	private void filmSearchSubMenu(Scanner input, List<Film> films) {
		int userInput = 0;
		
		boolean validInput = true;

		do {
			try {
				System.out.println("1) List all film details");
				System.out.println("2) Main menu");
				
				userInput = input.nextInt();
				
				input.nextLine();
				switch (userInput) {
				case 1:
					fullDetailFilmPrinter(films);
					break;
				case 2:
					break;
				default: 
					validInput = false;
					System.out.println("Invalid selection");
					break;
					
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid input, please try again");
				input.nextLine();
				validInput = false;
			}
		} while (! validInput);  //End of do while loop beginning line 141
	} // End of filmSearchSubMenu beginning line 136
	
	
	// Print all of the database fields for a list of films
	private void fullDetailFilmPrinter(List<Film> films) {
		for (Film film : films) {

			StringBuilder filmDetails = new StringBuilder();
			filmDetails.append("ID: " + film.getId() + ", Title: " + film.getTitle() + ", Released: "
					+ film.getReleaseYear() + ", Language ID: " + film.getLanguageId() + ", Rental Duration: "
					+ film.getRentalDuration() + ", Rental Rate: " + film.getRentalRate() + ", Length: "
					+ film.getLength() + ", Replacement Cost: " + film.getReplacementCost() + ", \n Rating: "
					+ film.getRating() + ", Special Features: " + film.getFeatures() + ", Category: " + db.findCategoryByFilmId(film.getId()));
			System.out.println(filmDetails);
		} //End of foreach loop
	} // End of fullDetailFilmPrinter
	
	
} // End of program
