package com.skilldistillery.filmquery.database;

import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;
import com.skilldistillery.filmquery.entities.InventoryItem;

public interface DatabaseAccessor {
  public Film findFilmById(int filmId) ;
  public Actor findActorById(int actorId) ;
  public List<Actor> findActorsByFilmId(int filmId) ;
  public String findLanguageByFilm(Film film);
  public List<Film> findFilmByKeyword(String keyword);
  public String findCategoryByFilmId(int filmId);
  public List<InventoryItem> findInventoryItemByFilm(Film film);
}
