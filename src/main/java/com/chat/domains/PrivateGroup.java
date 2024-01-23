package com.chat.domains;

import java.util.List;

public class PrivateGroup extends Group {
	private User _admin;

	public PrivateGroup(List<User> users, User admin) {
		super(users);
		this._admin = admin;
	}

	public PrivateGroup(List<User> users) {
		super(users);
	}

	public User getAdmins() {
		return _admin;
	}

}
