package com.keymetic.flicking.core.events;

import com.keymetic.flicking.core.entity.Review;

public class ReviewUpdatedEvent {
	private Review review;
	private boolean entityFound;
	
	public ReviewUpdatedEvent(Review review) {		
		this.review = review;
		this.entityFound = true;
	}	
	
	 public Review getReview() {
		return review;
	}

	public boolean isEntityFound() {
		return entityFound;
	}

	public static ReviewUpdatedEvent notFound() {
		    ReviewUpdatedEvent ev = new ReviewUpdatedEvent(null);
		    ev.entityFound=false;
		    return ev;
		  }	
	
	
	

}
