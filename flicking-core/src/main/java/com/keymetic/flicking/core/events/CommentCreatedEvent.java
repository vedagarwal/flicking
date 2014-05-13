package com.keymetic.flicking.core.events;

import com.keymetic.flicking.core.entity.Comment;

public class CommentCreatedEvent {
	
	private final Comment comment;

	public CommentCreatedEvent(final Comment comment) {		
		this.comment = comment;
	}

	public Comment getComment() {
		return comment;
	}
		

}
