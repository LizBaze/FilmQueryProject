# FilmQueryProject

# Description
### Main Functionality

This project interfaces a SQL database with a Java codebase to allow users to look up films by ID or keyword search. Upon successful lookup, the program references tables in the database to construct objects for the film and all actors associated with it. The final output includes the film title, description, release year, rating, language, and cast. This data is extracted from three tables with one bridge table (film_actor) also being utilized.  

### Completed Stretch Goals

Additionally, the films are stored and passed to a submenu allowing for the option to display all the database fields for any films matching the search criteria. 

![image](https://user-images.githubusercontent.com/112978206/205415736-2b2c3392-118c-4487-b654-1aa7a843350b.png)

# Lessons Learned
- I am getting much more comfortable deciding which class any given method should go in to best maintain proper encapsulation of a project. Understanding your basic project layout is paramount. 
- Similar to the above, looking at my code and deciding which parts can be moved to separate methods for increased readability is also getting much easier. 
- It is very important to proofread and test your SQL queries. Writing them in the command line first before inserting them into Java seems like a good way to go.

# Technologies Used
- Java
- SQL
- Git
