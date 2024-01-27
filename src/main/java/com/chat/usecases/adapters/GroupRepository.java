package com.chat.usecases.adapters;

import java.util.List;

import com.chat.domains.Group;
import com.chat.domains.PrivateGroup;
import com.chat.domains.PublicGroup;
import com.chat.domains.User;

public interface GroupRepository extends Repository<Group> {
	String generateJoiningCode();

	void addUser(String id, User user);

	List<PublicGroup> getAllPublicGroup();

	List<PrivateGroup> getAllPrivateGroup();
}
