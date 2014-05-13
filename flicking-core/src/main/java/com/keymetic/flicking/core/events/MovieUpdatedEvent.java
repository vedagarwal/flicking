package com.keymetic.flicking.core.events;

import com.keymetic.flicking.core.entity.Movie;

public class MovieUpdatedEvent {
	private Movie movie;
	private boolean entityFound;
	
	public MovieUpdatedEvent(Movie movie) {		
		this.movie = movie;
		this.entityFound = true;
	}	

	public Movie getMovie() {
		return movie;
	}
	public boolean isEntityFound() {
		return entityFound;
	}

	public static MovieUpdatedEvent notFound() {
		MovieUpdatedEvent ev = new MovieUpdatedEvent(null);
		    ev.entityFound=false;
		    return ev;
		  }	
	

}
