package com.chat.domains;

import java.util.List;

public class PrivateGroup extends Group {
	private List<User> _admins;

	public PrivateGroup(List<User> users, GroupType type, String name, List<User> admins) {
		super(users, type, name);
		this._admins = admins;
	}

	public List<User> getAdmins() {
		return _admins;
	}

	public boolean hasAdmin(User user) {
		for (User admin : _admins) {
			if (user.equals(admin)) {
				return true;
			}
		}
		return false;
	}
}
