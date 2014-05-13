package com.keymetic.flicking.core.events;

import com.keymetic.flicking.core.entity.Comment;

public class CommentDetailsEvent {
	private Comment comment;
	private boolean entityFound;

	public CommentDetailsEvent(Comment comment) {		
		this.comment = comment;
		this.entityFound = true;
	}	
	
	 public Comment getComment() {
		return comment;
	}

	public boolean isEntityFound() {
		return entityFound;
	}

	public static CommentDetailsEvent notFound() {
		    CommentDetailsEvent ev = new CommentDetailsEvent(null);
		    ev.entityFound=false;
		    return ev;
		  }	
	

}
