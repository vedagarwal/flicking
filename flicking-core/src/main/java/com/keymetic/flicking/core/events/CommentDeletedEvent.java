package com.keymetic.flicking.core.events;

import com.keymetic.flicking.core.entity.Comment;

public class CommentDeletedEvent {
	private Comment comment;
	private boolean deletionCompleted;
	private boolean entityFound;		
	
	public CommentDeletedEvent(Comment comment) {
		this.comment = comment;
		deletionCompleted = true;
		entityFound = true;
	}	
	
	public Comment getComment() {
		return comment;
	}

	public boolean isDeletionCompleted() {
		return deletionCompleted;
	}

	public boolean isEntityFound() {
		return entityFound;
	}
	
	public static CommentDeletedEvent deletionForbidden(Comment comment) {
	    CommentDeletedEvent ev = new CommentDeletedEvent(comment);
	    ev.entityFound=true;
	    ev.deletionCompleted=false;
	    return ev;
	  }

	  public static CommentDeletedEvent notFound() {
	    CommentDeletedEvent ev = new CommentDeletedEvent(null);
	    ev.entityFound=false;
	    return ev;
	  }
	
	
	
}
