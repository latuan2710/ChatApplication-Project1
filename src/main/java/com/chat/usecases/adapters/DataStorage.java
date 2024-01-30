package com.chat.usecases.adapters;

import com.chat.domains.User;

public interface DataStorage {
	Repository<User> getUserRepository();
	
	GroupRepository getGroupRepository();

	void cleanAll();
}
