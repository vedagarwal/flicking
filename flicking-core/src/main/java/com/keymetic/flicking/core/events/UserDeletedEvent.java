package com.keymetic.flicking.core.events;

import com.keymetic.flicking.core.entity.User;

public class UserDeletedEvent {
	
	private User user;
	private boolean deletionCompleted;
	private boolean entityFound;	
	
	
	
	public UserDeletedEvent(User user) {
		this.user = user;
		deletionCompleted = true;
		entityFound = true;
	}

	public User getUser() {
		return user;
	}	
	
	public boolean isDeletionCompleted() {
		return deletionCompleted;
	}

	public boolean isEntityFound() {
		return entityFound;
	}
	
	public static UserDeletedEvent deletionForbidden(User user) {
		UserDeletedEvent ev = new UserDeletedEvent(user);
	    ev.entityFound=true;
	    ev.deletionCompleted=false;
	    return ev;
	  }

	  public static UserDeletedEvent notFound() {
	    UserDeletedEvent ev = new UserDeletedEvent(null);
	    ev.entityFound=false;
	    return ev;
	  }
	
	
	

}
