package com.chat.domains;

import java.util.List;

public class PrivateGroup extends Group {
	private List<User> _admins;

	public PrivateGroup(List<User> users, GroupType type, List<User> _admins) {
		super(users, type);
		this._admins = _admins;
	}

	public List<User> getAdmins() {
		return _admins;
	}

	public boolean isAdmin(User user) {
		for (User admin : _admins) {
			if (user.equals(admin)) {
				return true;
			}
		}
		return false;
	}
}
