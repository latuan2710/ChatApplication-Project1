package com.chat.domains;

import java.util.List;

public class PrivateGroup extends Group {
	private User _admin;

	public PrivateGroup(List<User> users, GroupType type, User _admin) {
		super(users, type);
		this._admin = _admin;
	}

	public User getAdmins() {
		return _admin;
	}

}
