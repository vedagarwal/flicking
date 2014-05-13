package com.keymetic.flicking.core.events;

import com.keymetic.flicking.core.entity.Review;

public class ReviewDetailsEvent {
	private Review review;
	private boolean entityFound;

	public ReviewDetailsEvent(Review review) {		
		this.review = review;
		this.entityFound = true;
	}	
	
	 public Review getReview() {
		return review;
	}

	public boolean isEntityFound() {
		return entityFound;
	}

	public static ReviewDetailsEvent notFound() {
		    ReviewDetailsEvent ev = new ReviewDetailsEvent(null);
		    ev.entityFound=false;
		    return ev;
		  }	
	
}
