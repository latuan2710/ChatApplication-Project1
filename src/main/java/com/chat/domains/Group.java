package com.chat.domains;

import java.util.List;

public abstract class Group extends ChatEntity {
	private String _name;
	private List<User> _users;
	private GroupType _type;

	public Group(List<User> users, GroupType type, String name) {
		super();
		this._users = users;
		this._type = type;
		this._name = name;
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

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public enum GroupType {
		Private, Public
	}
}
