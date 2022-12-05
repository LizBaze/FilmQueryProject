package com.skilldistillery.filmquery.database;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;
import com.skilldistillery.filmquery.entities.InventoryItem;

class DatabaseAccessTests {
  private DatabaseAccessor db;

  @BeforeEach
  void setUp() throws Exception {
    db = new DatabaseAccessorObject();
  }

  @AfterEach
  void tearDown() throws Exception {
    db = null;
  }
  
  @Test
  void test_getFilmById_returns_film_with_id() throws SQLException {
    Film f = db.findFilmById(1);
    assertNotNull(f);
//    assertEquals("ACADEMY DINOSAUR", f.getTitle());
  }

  @Test
  void test_getFilmById_with_invalid_id_returns_null() throws SQLException {
    Film f = db.findFilmById(-42);
    assertNull(f);
  }
  
  @Test
  void test_findActorsByFilmId_with_invalid_id_returns_null() {
	  List<Actor> f = db.findActorsByFilmId(-42);
	  assertNull(f);
  }
  
  @Test
  void test_findCategoryByFilmId_with_invalid_id_returns_null() {
	  String f = db.findCategoryByFilmId(-42);
	  assertNull(f);
  }
  
  @Test
  void test_findInventoryItemByFilm_with_invalid_film_returns_null() {
	  List<InventoryItem> f = db.findInventoryItemByFilm(new Film());
	  assertNull(f);
  }
  
  
  
  
}
