package com.chat.domains;

import java.util.List;

public abstract class Group extends ChatEntity {
	private List<User> _users;
	private GroupType _type;

	public Group(List<User> users, GroupType type) {
		super();
		this._users = users;
		this._type = type;
	}

	public List<User> getUsers() {
		return _users;
	}

	public void setUsers(List<User> users) {
		this._users = users;
	}

	public void addMember(User user) {
		if (!this._users.contains(user)) {
			this._users.add(user);
		}
	}

	public void removeMember(User user) {
		this._users.remove(user);
	}

	public GroupType getType() {
		return _type;
	}

	public void setType(GroupType type) {
		this._type = type;
	}

	public enum GroupType {
		Private, Public
	}
}
