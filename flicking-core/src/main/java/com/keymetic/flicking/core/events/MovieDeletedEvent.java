package com.keymetic.flicking.core.events;

import com.keymetic.flicking.core.entity.Movie;

public class MovieDeletedEvent {

	private Movie movie;
	private boolean deletionCompleted;
	private boolean entityFound;	
	
	
	
	public MovieDeletedEvent(Movie movie) {
		this.movie = movie;
		deletionCompleted = true;
		entityFound = true;
	}

	public Movie getMovie() {
		return movie;
	}	
	
	public boolean isDeletionCompleted() {
		return deletionCompleted;
	}

	public boolean isEntityFound() {
		return entityFound;
	}
	
	public static MovieDeletedEvent deletionForbidden(Movie movie) {
	    MovieDeletedEvent ev = new MovieDeletedEvent(movie);
	    ev.entityFound=true;
	    ev.deletionCompleted=false;
	    return ev;
	  }

	  public static MovieDeletedEvent notFound() {
	    MovieDeletedEvent ev = new MovieDeletedEvent(null);
	    ev.entityFound=false;
	    return ev;
	  }
	
	
	
	
	

}
