package com.keymetic.flicking.core.events;

import com.keymetic.flicking.core.entity.Review;

public class ReviewCreatedEvent {
	
	private final Review review;

	public ReviewCreatedEvent(final Review review) {		
		this.review = review;
	}

	public Review getReview() {
		return review;
	}

}
