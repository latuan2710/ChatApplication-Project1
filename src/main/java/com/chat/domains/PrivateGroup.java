package com.chat.domains;

import java.util.List;

public class PrivateGroup extends Group {
	private List<User> admins;

	public PrivateGroup(List<User> users, GroupType type, List<User> admins) {
		super(users, type);
		this.admins = admins;
	} 

	public List<User> getAdmins() {
		return admins;
	}
}
