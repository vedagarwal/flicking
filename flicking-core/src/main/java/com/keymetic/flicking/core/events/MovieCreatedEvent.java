package com.keymetic.flicking.core.events;

import com.keymetic.flicking.core.entity.Movie;

public class MovieCreatedEvent {
	
	private final Movie movie;

	public MovieCreatedEvent(final Movie movie) {
		this.movie = movie;
	}

	public Movie getMovie() {
		return movie;
	}
	
	
}
