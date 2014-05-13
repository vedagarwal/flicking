package com.keymetic.flicking.core.events;

import java.util.List;

import com.keymetic.flicking.core.entity.Review;

public class ReviewListEvent {
	private List<Review> reviews;
	private boolean entityFound;	

	public ReviewListEvent(List<Review> reviews) {
		this.reviews = reviews;
		this.entityFound = true;
	}
	
	public List<Review> getReviews() {
		return reviews;
	}
	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	public boolean isEntityFound() {
		return entityFound;
	}
	public void setEntityFound(boolean entityFound) {
		this.entityFound = entityFound;
	}
	
	public static ReviewListEvent notFound(){
		ReviewListEvent ev = new ReviewListEvent(null);
		ev.entityFound = false;
		return ev;
	}



}
