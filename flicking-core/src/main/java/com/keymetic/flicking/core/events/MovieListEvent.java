package com.keymetic.flicking.core.events;

import java.util.List;

import com.keymetic.flicking.core.entity.Movie;

public class MovieListEvent {
	private List<Movie> movies;
	private boolean entityFound;	

	public MovieListEvent(List<Movie> movies) {
		this.movies = movies;
		this.entityFound = true;
	}

	public List<Movie> getMovies() {
		return movies;
	}
	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}
	public boolean isEntityFound() {
		return entityFound;
	}
	public void setEntityFound(boolean entityFound) {
		this.entityFound = entityFound;
	}
	
	public static MovieListEvent notFound(){
		MovieListEvent ev = new MovieListEvent(null);
		ev.entityFound = false;
		return ev;
	}





}
