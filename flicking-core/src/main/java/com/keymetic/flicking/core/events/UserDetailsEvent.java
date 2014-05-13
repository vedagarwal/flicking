package com.keymetic.flicking.core.events;

import com.keymetic.flicking.core.entity.User;

public class UserDetailsEvent {
	private User user;
	private boolean entityFound;

	public UserDetailsEvent(User user) {		
		this.user = user;
		this.entityFound = true;
	}	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}



	public boolean isEntityFound() {
		return entityFound;
	}

	public static UserDetailsEvent notFound() {
		UserDetailsEvent ev = new UserDetailsEvent(null);
		ev.entityFound=false;
		return ev;
	}




}
