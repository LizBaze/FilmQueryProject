package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
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
	  DatabaseAccessorObject dbo = new DatabaseAccessorObject();
	  boolean keepGoing = true;
	  do  {
	  printMenu();
	  int userChoice = input.nextInt();
	  input.nextLine();
	  switch (userChoice) {
	  case 1:
		  System.out.println("Enter the id to search");
		  System.out.println( dbo.findFilmById(input.nextInt()) );
		  input.nextLine();
		  break;
	  
	  
	  case 3:
		  System.out.println("Goodbye");
		  keepGoing = false;
	  }
	  
	  
	  
	  } while(keepGoing);
    
  }
  
  
  private void printMenu () {
	  System.out.println("Welcome to the sdvid database management system");
	  System.out.println("1) Look up a film by ID");
	  System.out.println("2) Look up a film by keyword search");
	  System.out.println("3) Exit");
  }

}
