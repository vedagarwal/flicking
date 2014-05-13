package com.keymetic.flicking.core.events;

import com.keymetic.flicking.core.entity.User;

public class UserCreatedEvent {
	private final User user;

	public UserCreatedEvent(final User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	

}
