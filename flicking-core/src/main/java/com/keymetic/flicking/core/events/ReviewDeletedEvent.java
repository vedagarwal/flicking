package com.keymetic.flicking.core.events;

import com.keymetic.flicking.core.entity.Review;

public class ReviewDeletedEvent {
	private Review review;
	private boolean deletionCompleted;
	private boolean entityFound;		
	
	public ReviewDeletedEvent(Review review) {
		this.review = review;
		deletionCompleted = true;
		entityFound = true;
	}	
	
	public Review getReview() {
		return review;
	}

	public boolean isDeletionCompleted() {
		return deletionCompleted;
	}

	public boolean isEntityFound() {
		return entityFound;
	}
	
	public static ReviewDeletedEvent deletionForbidden(Review review) {
	    ReviewDeletedEvent ev = new ReviewDeletedEvent(review);
	    ev.entityFound=true;
	    ev.deletionCompleted=false;
	    return ev;
	  }

	  public static ReviewDeletedEvent notFound() {
	    ReviewDeletedEvent ev = new ReviewDeletedEvent(null);
	    ev.entityFound=false;
	    return ev;
	  }
	

}
