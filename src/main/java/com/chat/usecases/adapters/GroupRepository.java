package com.chat.usecases.adapters;

import com.chat.domains.Group;
import com.chat.domains.User;

public interface GroupRepository extends Repository<Group> {
	String generateJoiningCode();

	void addUser(String id, User user);
}
