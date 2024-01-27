package com.chat.usecases.adapters;

import com.chat.domains.Group;
import com.chat.domains.User;

public interface DataStorage {
	Repository<User> getUserRepository();
	
	Repository<Group> getGroupRepository();

	void cleanAll();
}
