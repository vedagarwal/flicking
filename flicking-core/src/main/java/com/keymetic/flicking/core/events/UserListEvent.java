package com.keymetic.flicking.core.events;

import java.util.List;

import com.keymetic.flicking.core.entity.User;

public class UserListEvent {

	private List<User> users;
	private boolean entityFound;	

	public UserListEvent(List<User> users) {
		this.users = users;
		this.entityFound = true;
	}


	public List<User> getUsers() {
		return users;
	}


	public void setUsers(List<User> users) {
		this.users = users;
	}


	public boolean isEntityFound() {
		return entityFound;
	}
	public void setEntityFound(boolean entityFound) {
		this.entityFound = entityFound;
	}

	public static UserListEvent notFound(){
		UserListEvent ev = new UserListEvent(null);
		ev.entityFound = false;
		return ev;
	}




}
