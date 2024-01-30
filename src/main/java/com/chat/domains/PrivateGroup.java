package com.chat.domains;

import java.util.List;

public class PrivateGroup extends Group {
	private List<User> _admins;

	public PrivateGroup(List<User> users, GroupType type, List<User> _admin) {
		super(users, type);
		this._admins = _admin;
	}

	public List<User> getAdmins() {
		return _admins;
	}

}
