package com.keymetic.flicking.core.events;

import com.keymetic.flicking.core.entity.User;

public class UserUpdatedEvent {
	private User user;
	private boolean entityFound;
	
	public UserUpdatedEvent(User user) {		
		this.user = user;
		this.entityFound = true;
	}

	public User getUser() {
		return user;
	}

	public boolean isEntityFound() {
		return entityFound;
	}

	public static UserUpdatedEvent notFound() {
		    UserUpdatedEvent ev = new UserUpdatedEvent(null);
		    ev.entityFound=false;
		    return ev;
		  }	
	

}
