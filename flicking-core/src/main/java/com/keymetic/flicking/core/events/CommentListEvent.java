package com.keymetic.flicking.core.events;

import java.util.List;

import com.keymetic.flicking.core.entity.Comment;

public class CommentListEvent {
	private List<Comment> comments;
	private boolean entityFound;	

	public CommentListEvent(List<Comment> comments) {
		this.comments = comments;
		this.entityFound = true;
	}
	
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public boolean isEntityFound() {
		return entityFound;
	}
	public void setEntityFound(boolean entityFound) {
		this.entityFound = entityFound;
	}
	
	public static CommentListEvent notFound(){
		CommentListEvent ev = new CommentListEvent(null);
		ev.entityFound = false;
		return ev;
	}





}
