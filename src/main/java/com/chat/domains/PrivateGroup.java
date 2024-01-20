package com.chat.domains;

import java.util.List;

public class PrivateGroup extends Group {
	private List<User> _admins;

	public PrivateGroup(List<User> users, List<User> admins) {
		super(users);
		this._admins = admins;
	}

	public PrivateGroup(List<User> users) {
		super(users);
	}

	public List<User> getAdmins() {
		return _admins;
	}

	public void setAdmins(List<User> admins) {
		this._admins = admins;
	}
}
