package com.chat.domains;

import java.util.List;

public abstract class Group extends BaseEntity{
	private List<User> _users;

	public Group(List<User> users) {
		super();
		this._users = users;
	}

	public List<User> getUsers() {
		return _users;
	}

	public void setUsers(List<User> users) {
		this._users = users;
	}

}
