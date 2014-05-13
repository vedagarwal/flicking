package com.keymetic.flicking.core.events;

import com.keymetic.flicking.core.entity.Movie;

public class MovieDetailsEvent {
	private Movie movie;
	private boolean entityFound;

	public MovieDetailsEvent(Movie movie) {		
		this.movie = movie;
		this.entityFound = true;
	}

	public Movie getMovie() {
		return movie;
	}
	
	
	
	 public boolean isEntityFound() {
		return entityFound;
	}

	public static MovieDetailsEvent notFound() {
		    MovieDetailsEvent ev = new MovieDetailsEvent(null);
		    ev.entityFound=false;
		    return ev;
		  }
	
	
	
	
}
